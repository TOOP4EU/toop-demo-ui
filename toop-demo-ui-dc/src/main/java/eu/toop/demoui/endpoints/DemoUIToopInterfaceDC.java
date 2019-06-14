/**
 * Copyright (C) 2018-2019 toop.eu
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.toop.demoui.endpoints;

import java.io.IOException;
import java.util.AbstractMap;

import java.util.AbstractMap.SimpleEntry;
import javax.annotation.Nonnull;

import com.helger.commons.collection.impl.ICommonsList;
import com.helger.commons.error.level.EErrorLevel;
import com.vaadin.navigator.Navigator;
import com.vaadin.ui.UI;

import eu.toop.commons.dataexchange.v140.TDEConceptRequestType;
import eu.toop.commons.dataexchange.v140.TDEDataElementRequestType;
import eu.toop.commons.dataexchange.v140.TDEDataElementResponseValueType;
import eu.toop.commons.dataexchange.v140.TDEDataProviderType;
import eu.toop.commons.dataexchange.v140.TDETOOPResponseType;
import eu.toop.commons.exchange.AsicReadEntry;
import eu.toop.commons.exchange.ToopResponseWithAttachments140;
import eu.toop.demoui.DCToToopInterfaceMapper;
import eu.toop.demoui.bean.ToopDataBean;
import eu.toop.demoui.layouts.DynamicRequestPage;
import eu.toop.demoui.layouts.RegisterWithWEEEMainPage;
import eu.toop.demoui.view.DynamicRequest;
import eu.toop.demoui.view.PhaseTwo;
import eu.toop.demoui.view.RequestToItalyOne;
import eu.toop.demoui.view.RequestToSlovakiaOne;
import eu.toop.demoui.view.RequestToSlovakiaTwo;
import eu.toop.demoui.view.RequestToSloveniaOne;
import eu.toop.demoui.view.RequestToSwedenOne;
import eu.toop.demoui.view.RequestToSwedenTwo;
import eu.toop.iface.IToopInterfaceDC;
import eu.toop.kafkaclient.ToopKafkaClient;
import oasis.names.specification.ubl.schema.xsd.unqualifieddatatypes_21.IdentifierType;

public class DemoUIToopInterfaceDC implements IToopInterfaceDC {
  public DemoUIToopInterfaceDC () {
  }

  public void onToopResponse (@Nonnull final ToopResponseWithAttachments140 aResponseWA) throws IOException {
    final TDETOOPResponseType aResponse = aResponseWA.getResponse ();
    final ICommonsList<AsicReadEntry> attachments = aResponseWA.attachments ();

    DemoUIToopInterfaceHelper.dumpResponse (aResponse);

    final String sRequestID = aResponse.getDataRequestIdentifier ().getValue ();
    final String sLogPrefix = "[" + sRequestID + "] [DC] ";

    final TDEDataProviderType aDP = aResponse.hasNoDataProviderEntries () ? null : aResponse.getDataProviderAtIndex (0);

    ToopKafkaClient.send (EErrorLevel.INFO,
                          () -> sLogPrefix + "Received data from Data Provider: "
                                + (aDP == null ? "null"
                                               : " DPIdentifier: " + aDP.getDPIdentifier ().getValue () + ", "
                                                 + " DPName: " + aDP.getDPName ().getValue () + ", "
                                                 + " DPElectronicAddressIdentifier: "
                                                 + aResponse.getRoutingInformation ()
                                                            .getDataProviderElectronicAddressIdentifier ()
                                                            .getValue ()));

    ToopKafkaClient.send (EErrorLevel.INFO, () -> sLogPrefix + "Number of attachments: " + attachments.size ());
    for (final AsicReadEntry attachment : attachments) {
      ToopKafkaClient.send (EErrorLevel.INFO, () -> sLogPrefix + "Received document: " + attachment.getEntryName ()
                                                    + ", size: " + attachment.payload ().length);
      // attachment.payload(); <-- this is the byte[]
    }

    // Push a new organization bean to the UI
    try {
      // Find the correct UI
      final UI aUI = DCToToopInterfaceMapper.getAndRemoveUI (sRequestID);
      if (aUI == null)
        throw new IllegalStateException ("Having no Vaadin UI for request ID '" + sRequestID + "'");

      ToopKafkaClient.send (EErrorLevel.INFO, () -> sLogPrefix + "Current UI: " + aUI);
      final Navigator threadUINavigator = aUI.getNavigator ();
      ToopKafkaClient.send (EErrorLevel.INFO, () -> sLogPrefix + "Current Navigator: " + threadUINavigator);

      final ToopDataBean bean = new ToopDataBean (attachments);

      // Get requested documents
      if (aResponse.getDocumentRequestCount () > 0) {
        ToopKafkaClient.send (EErrorLevel.INFO, () -> sLogPrefix + "Contains requested documents");
        aResponse.getDocumentRequest().forEach( dRec -> {
          dRec.getDocumentResponse().forEach(dResp -> {
            bean.getKeyValList().add(new SimpleEntry<>("Document Name:",dResp.getDocumentName().getValue()));
            bean.getKeyValList().add(new SimpleEntry<>("Document Issue Date:",dResp.getDocumentIssueDate().getValue().toString()));
            bean.getKeyValList().add(new SimpleEntry<>("Document Description:",dResp.getDocumentDescription().getValue()));
            bean.getKeyValList().add(new SimpleEntry<>("Document Identifier:",dResp.getDocumentIdentifier().getValue()));
          });
        });
      }

      // Inspect all mapped values
      for (final TDEDataElementRequestType aDER : aResponse.getDataElementRequest ()) {

        final TDEConceptRequestType aFirstLevelConcept = aDER.getConceptRequest ();

        final String sourceConceptName = aFirstLevelConcept.getConceptName ().getValue ();

        for (final TDEConceptRequestType aSecondLevelConcept : aFirstLevelConcept.getConceptRequest ()) {

          final String toopConceptName = aSecondLevelConcept.getConceptName ().getValue ();

          for (final TDEConceptRequestType aThirdLevelConcept : aSecondLevelConcept.getConceptRequest ()) {

            final String destinationConceptName = aThirdLevelConcept.getConceptName ().getValue ();

            final String mappedConcept = sourceConceptName + " - " + toopConceptName + " - " + destinationConceptName;

            for (final TDEDataElementResponseValueType aThirdLevelConceptDERValue : aThirdLevelConcept.getDataElementResponseValue ()) {
              ToopKafkaClient.send (EErrorLevel.INFO, () -> sLogPrefix + "Received a mapped concept ( " + mappedConcept
                                                            + " ), response: " + aThirdLevelConceptDERValue);

              String aValue = "";
              if (aThirdLevelConceptDERValue.getResponseCode () != null) {
                aValue = aThirdLevelConceptDERValue.getResponseCode ().getValue ();
              } else if (aThirdLevelConceptDERValue.getResponseIdentifier () != null) {
                aValue = aThirdLevelConceptDERValue.getResponseIdentifier ().getValue ();
              } else if (aThirdLevelConceptDERValue.getResponseNumeric () != null
                         && aThirdLevelConceptDERValue.getResponseNumeric ().getValue () != null) {
                aValue = aThirdLevelConceptDERValue.getResponseNumeric ().getValue ().toString ();
              } else if (aThirdLevelConceptDERValue.getResponseDescription () != null
                         && aThirdLevelConceptDERValue.getResponseDescription ().getValue () != null) {
                aValue = aThirdLevelConceptDERValue.getResponseDescription ().getValue ();
              } else {
                ToopKafkaClient.send (EErrorLevel.WARN, () -> sLogPrefix + "Unsupported response value provided: "
                                                              + aThirdLevelConceptDERValue.toString ());
              }

              bean.getKeyValList ().add (new AbstractMap.SimpleEntry<> (sourceConceptName, aValue));

              if (sourceConceptName.equals ("FreedoniaStreetAddress")) {
                bean.setAddress (aValue);
              } else if (sourceConceptName.equals ("FreedoniaSSNumber")) {
                bean.setSSNumber (aValue);
              } else if (sourceConceptName.equals ("FreedoniaCompanyCode")) {
                bean.setBusinessCode (aValue);
              } else if (sourceConceptName.equals ("FreedoniaVATNumber")) {
                bean.setVATNumber (aValue);
              } else if (sourceConceptName.equals ("FreedoniaCompanyType")) {
                bean.setCompanyType (aValue);
              } else if (sourceConceptName.equals ("FreedoniaRegistrationDate")) {
                bean.setRegistrationDate (aValue);
              } else if (sourceConceptName.equals ("FreedoniaRegistrationNumber")) {
                bean.setRegistrationNumber (aValue);
              } else if (sourceConceptName.equals ("FreedoniaCompanyName")) {
                bean.setCompanyName (aValue);
              } else if (sourceConceptName.equals ("FreedoniaNaceCode")) {
                bean.setCompanyNaceCode (aValue);
              } else if (sourceConceptName.equals ("FreedoniaActivityDescription")) {
                bean.setActivityDeclaration (aValue);
              } else if (sourceConceptName.equals ("FreedoniaRegistrationAuthority")) {
                bean.setRegistrationAuthority (aValue);
              } else if (sourceConceptName.equals ("FreedoniaLegalStatus")) {
                bean.setLegalStatus (aValue);
              } else if (sourceConceptName.equals ("FreedoniaLegalStatusEffectiveDate")) {
                bean.setLegalStatusEffectiveDate (aValue);
              } else {
                ToopKafkaClient.send (EErrorLevel.WARN, () -> sLogPrefix + "Unsupported source concept name: '"
                                                              + sourceConceptName + "'");
              }
            }
          }
        }
      }

      if (threadUINavigator.getCurrentView () instanceof PhaseTwo) {
        final PhaseTwo homeView = (PhaseTwo) threadUINavigator.getCurrentView ();
        if (homeView.getCurrentPage () instanceof RegisterWithWEEEMainPage) {
          homeView.setToopDataBean (bean);
          final RegisterWithWEEEMainPage page = (RegisterWithWEEEMainPage) homeView.getCurrentPage ();
          page.addMainCompanyForm ();
        }
      } else if (threadUINavigator.getCurrentView () instanceof RequestToSwedenOne) {
        final RequestToSwedenOne homeView = (RequestToSwedenOne) threadUINavigator.getCurrentView ();
        if (homeView.getCurrentPage () instanceof RegisterWithWEEEMainPage) {
          homeView.setToopDataBean (bean);
          final RegisterWithWEEEMainPage page = (RegisterWithWEEEMainPage) homeView.getCurrentPage ();
          page.addMainCompanyForm ();
        }
      } else if (threadUINavigator.getCurrentView () instanceof RequestToSwedenTwo) {
        final RequestToSwedenTwo homeView = (RequestToSwedenTwo) threadUINavigator.getCurrentView ();
        if (homeView.getCurrentPage () instanceof RegisterWithWEEEMainPage) {
          homeView.setToopDataBean (bean);
          final RegisterWithWEEEMainPage page = (RegisterWithWEEEMainPage) homeView.getCurrentPage ();
          page.addMainCompanyForm ();
        }
      } else if (threadUINavigator.getCurrentView () instanceof RequestToSloveniaOne) {
        final RequestToSloveniaOne homeView = (RequestToSloveniaOne) threadUINavigator.getCurrentView ();
        if (homeView.getCurrentPage () instanceof RegisterWithWEEEMainPage) {
          homeView.setToopDataBean (bean);
          final RegisterWithWEEEMainPage page = (RegisterWithWEEEMainPage) homeView.getCurrentPage ();
          page.addMainCompanyForm ();
        }
      } else if (threadUINavigator.getCurrentView () instanceof RequestToSlovakiaOne) {
        final RequestToSlovakiaOne homeView = (RequestToSlovakiaOne) threadUINavigator.getCurrentView ();
        if (homeView.getCurrentPage () instanceof RegisterWithWEEEMainPage) {
          homeView.setToopDataBean (bean);
          final RegisterWithWEEEMainPage page = (RegisterWithWEEEMainPage) homeView.getCurrentPage ();
          page.addMainCompanyForm ();
        }
      } else if (threadUINavigator.getCurrentView () instanceof RequestToSlovakiaTwo) {
        final RequestToSlovakiaTwo homeView = (RequestToSlovakiaTwo) threadUINavigator.getCurrentView ();
        if (homeView.getCurrentPage () instanceof RegisterWithWEEEMainPage) {
          homeView.setToopDataBean (bean);
          final RegisterWithWEEEMainPage page = (RegisterWithWEEEMainPage) homeView.getCurrentPage ();
          page.addMainCompanyForm ();
        }
      } else if (threadUINavigator.getCurrentView () instanceof RequestToItalyOne) {
        final RequestToItalyOne homeView = (RequestToItalyOne) threadUINavigator.getCurrentView ();
        if (homeView.getCurrentPage () instanceof RegisterWithWEEEMainPage) {
          homeView.setToopDataBean (bean);
          final RegisterWithWEEEMainPage page = (RegisterWithWEEEMainPage) homeView.getCurrentPage ();
          page.addMainCompanyForm ();
        }
      } else if (threadUINavigator.getCurrentView () instanceof DynamicRequest) {
        final DynamicRequest homeView = (DynamicRequest) threadUINavigator.getCurrentView ();
        if (homeView.getCurrentPage () instanceof DynamicRequestPage) {
          homeView.setToopDataBean (bean);
          final DynamicRequestPage page = (DynamicRequestPage) homeView.getCurrentPage ();

          final String expectedUuid = page.getRequestId ();

          if (aResponse.getDataRequestIdentifier () != null && expectedUuid != null
              && expectedUuid.equals (aResponse.getDataRequestIdentifier ().getValue ())) {
            if (!aResponse.hasErrorEntries ()) {

              final IdentifierType documentTypeIdentifier = aResponse.getRoutingInformation ()
                                                                     .getDocumentTypeIdentifier ();

              if (documentTypeIdentifier.getValue ().contains ("registeredorganization")) {
                page.addMainCompanyForm ();
              } else {
                page.addKeyValueForm ();
              }
            } else {
              page.setError (aResponse.getError ());
            }

            final String conceptErrors = getConceptErrors (aResponse);
            if (!conceptErrors.isEmpty ()) {
              page.setConceptErrors (conceptErrors);
            }
          }
        }
      }

      ToopKafkaClient.send (EErrorLevel.INFO, () -> sLogPrefix + "Pushed new bean data to the Demo UI: " + bean);
    } catch (final Exception e) {
      ToopKafkaClient.send (EErrorLevel.ERROR, () -> sLogPrefix + "Failed to push new bean data to the Demo UI", e);
    }
  }

  private static String getConceptErrors (@Nonnull final TDETOOPResponseType aResponse) {

    final StringBuilder sb = new StringBuilder ();

    // Inspect all mapped values
    for (final TDEDataElementRequestType aDER : aResponse.getDataElementRequest ()) {

      final TDEConceptRequestType aFirstLevelConcept = aDER.getConceptRequest ();

      final String sourceConceptName = aFirstLevelConcept.getConceptName ().getValue ();

      for (final TDEConceptRequestType aSecondLevelConcept : aFirstLevelConcept.getConceptRequest ()) {

        final String toopConceptName = aSecondLevelConcept.getConceptName ().getValue ();

        for (final TDEConceptRequestType aThirdLevelConcept : aSecondLevelConcept.getConceptRequest ()) {

          final String destinationConceptName = aThirdLevelConcept.getConceptName ().getValue ();

          final String mappedConcept = sourceConceptName + " - " + toopConceptName + " - " + destinationConceptName;

          for (final TDEDataElementResponseValueType aThirdLevelConceptDERValue : aThirdLevelConcept.getDataElementResponseValue ()) {

            if (aThirdLevelConceptDERValue.getErrorIndicator () != null
                && aThirdLevelConceptDERValue.getErrorIndicator ().isValue ()) {
              sb.append (" - Concept Error (").append (mappedConcept).append ("):\n");
              if (aThirdLevelConceptDERValue.getErrorCode () != null) {
                sb.append ("     ").append (aThirdLevelConceptDERValue.getErrorCode ().getValue ()).append ("\n");
              } else if (aThirdLevelConceptDERValue.getResponseDescription () != null) {
                sb.append ("     ").append (aThirdLevelConceptDERValue.getResponseDescription ().getValue ())
                  .append ("\n");
              }
            }
          }
        }
      }
    }
    return sb.toString ();
  }
}
