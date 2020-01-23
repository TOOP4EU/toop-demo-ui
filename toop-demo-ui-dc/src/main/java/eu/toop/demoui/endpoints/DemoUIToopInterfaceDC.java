/**
 * Copyright (C) 2018-2020 toop.eu
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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.AbstractMap.SimpleEntry;
import java.util.List;

import javax.annotation.Nonnull;

import com.helger.commons.collection.impl.ICommonsList;
import com.helger.commons.datetime.PDTFactory;
import com.helger.commons.error.level.EErrorLevel;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.UI;

import eu.toop.commons.dataexchange.v140.TDEConceptRequestType;
import eu.toop.commons.dataexchange.v140.TDEDataElementRequestType;
import eu.toop.commons.dataexchange.v140.TDEDataElementResponseValueType;
import eu.toop.commons.dataexchange.v140.TDEDataProviderType;
import eu.toop.commons.dataexchange.v140.TDETOOPResponseType;
import eu.toop.commons.exchange.AsicReadEntry;
import eu.toop.commons.exchange.ToopResponseWithAttachments140;
import eu.toop.demoui.DCToToopInterfaceMapper;
import eu.toop.demoui.bean.DocumentDataBean;
import eu.toop.demoui.bean.ToopDataBean;
import eu.toop.demoui.layouts.DynamicRequestPage;
import eu.toop.demoui.layouts.MaritimePage;
import eu.toop.demoui.layouts.RegisterWithWEEEMainPage;
import eu.toop.demoui.view.DynamicRequest;
import eu.toop.demoui.view.Maritime;
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

public class DemoUIToopInterfaceDC implements IToopInterfaceDC
{
  public DemoUIToopInterfaceDC ()
  {}

  private static boolean _canUseConcept (@Nonnull final TDEConceptRequestType aConcept)
  {
    // This class can only deliver to:
    // - leaf entries
    // - that have already a response
    return aConcept.hasNoConceptRequestEntries () && aConcept.getDataElementResponseValueCount () > 0;
  }

  private void _extractValue (@Nonnull final String sLogPrefix,
                              @Nonnull final String sConceptName1,
                              @Nonnull final String mappedConcept,
                              @Nonnull final TDEConceptRequestType aConcept,
                              @Nonnull final ToopDataBean bean)
  {
    String sValue = null;
    for (final TDEDataElementResponseValueType aDER : aConcept.getDataElementResponseValue ())
    {
      ToopKafkaClient.send (EErrorLevel.INFO,
                            () -> sLogPrefix +
                                  "Received a mapped concept ( " +
                                  mappedConcept +
                                  " ), response: " +
                                  aDER.toString ());

      if (aDER.getResponseIdentifier () != null)
        sValue = aDER.getResponseIdentifier ().getValue ();
      else
        if (aDER.getResponseDescription () != null)
          sValue = aDER.getResponseDescription ().getValue ();
        else
          if (aDER.getResponseAmount () != null && aDER.getResponseAmount ().getValue () != null)
            sValue = aDER.getResponseAmount ().getValue ().toString ();
          else
            if (aDER.getResponseCode () != null)
              sValue = aDER.getResponseCode ().getValue ();
            else
              if (aDER.getResponseDate () != null)
                sValue = PDTFactory.createLocalDate (aDER.getResponseDate ().toGregorianCalendar ()).toString ();
              else
                if (aDER.getResponseTime () != null)
                  sValue = PDTFactory.createLocalTime (aDER.getResponseTime ().toGregorianCalendar ()).toString ();
                else
                  if (aDER.getResponseIndicator () != null)
                    sValue = Boolean.toString (aDER.getResponseIndicator ().isValue ());
                  else
                    if (aDER.getResponseMeasure () != null && aDER.getResponseMeasure ().getValue () != null)
                      sValue = aDER.getResponseMeasure ().getValue ().toString ();
                    else
                      if (aDER.getResponseNumeric () != null && aDER.getResponseNumeric ().getValue () != null)
                        sValue = aDER.getResponseNumeric ().getValue ().toString ();
                      else
                        if (aDER.getResponseQuantity () != null && aDER.getResponseQuantity ().getValue () != null)
                          sValue = aDER.getResponseQuantity ().getValue ().toString ();
                        // TODO ResponseURI is an indicator making no sense to
                        // me atm
                        else
                          ToopKafkaClient.send (EErrorLevel.WARN,
                                                () -> sLogPrefix +
                                                      "Unsupported response value provided: " +
                                                      aDER.toString ());
      if (sValue != null)
        break;
    }

    if (sValue == null)
      return;

    bean.getKeyValList ().add (new SimpleEntry <> (sConceptName1, sValue));
    if (sConceptName1.equals ("FreedoniaStreetAddress"))
      bean.setAddress (sValue);
    else
      if (sConceptName1.equals ("FreedoniaSSNumber"))
        bean.setSSNumber (sValue);
      else
        if (sConceptName1.equals ("FreedoniaCompanyCode"))
          bean.setBusinessCode (sValue);
        else
          if (sConceptName1.equals ("FreedoniaVATNumber"))
            bean.setVATNumber (sValue);
          else
            if (sConceptName1.equals ("FreedoniaCompanyType"))
              bean.setCompanyType (sValue);
            else
              if (sConceptName1.equals ("FreedoniaRegistrationDate"))
                bean.setRegistrationDate (sValue);
              else
                if (sConceptName1.equals ("FreedoniaRegistrationNumber"))
                  bean.setRegistrationNumber (sValue);
                else
                  if (sConceptName1.equals ("FreedoniaCompanyName"))
                    bean.setCompanyName (sValue);
                  else
                    if (sConceptName1.equals ("FreedoniaNaceCode"))
                      bean.setCompanyNaceCode (sValue);
                    else
                      if (sConceptName1.equals ("FreedoniaActivityDescription"))
                        bean.setActivityDeclaration (sValue);
                      else
                        if (sConceptName1.equals ("FreedoniaRegistrationAuthority"))
                          bean.setRegistrationAuthority (sValue);
                        else
                          if (sConceptName1.equals ("FreedoniaLegalStatus"))
                            bean.setLegalStatus (sValue);
                          else
                            if (sConceptName1.equals ("FreedoniaLegalStatusEffectiveDate"))
                              bean.setLegalStatusEffectiveDate (sValue);
                            else
                              if (sConceptName1.equals ("FreedoniaBirthDate"))
                                bean.setBirthDate (sValue);
                              else
                                if (sConceptName1.equals ("FreedoniaCapitalType"))
                                  bean.setCapitalType (sValue);
                              else
                                if (sConceptName1.equals ("FreedoniaCountryName"))
                                  bean.setCountryName (sValue);
                                else
                                  if (sConceptName1.equals ("FreedoniaEmailAddress"))
                                    bean.setEmailAddress (sValue);
                                  else
                                    if (sConceptName1.equals ("FreedoniaFamilyName"))
                                      bean.setFamilyName (sValue);
                                    else
                                      if (sConceptName1.equals ("FreedoniaFaxNumber"))
                                        bean.setFaxNumber (sValue);
                                      else
                                        if (sConceptName1.equals ("FreedoniaFoundationDate"))
                                          bean.setFoundationDate (sValue);
                                        else
                                          if (sConceptName1.equals ("FreedoniaGivenName"))
                                            bean.setGivenName (sValue);
                                          else
                                            if (sConceptName1.equals ("FreedoniaHasLegalRepresentative"))
                                              bean.setHasLegalRepresentative (sValue);
                                            else
                                              if (sConceptName1.equals ("FreedoniaLocality"))
                                                bean.setLocality (sValue);
                                              else
                                                if (sConceptName1.equals ("FreedoniaPerson"))
                                                  bean.setPerson (sValue);
                                                else
                                                  if (sConceptName1.equals ("FreedoniaPostalCode"))
                                                    bean.setPostalCode (sValue);
                                                  else
                                                    if (sConceptName1.equals ("FreedoniaRegion"))
                                                      bean.setRegion (sValue);
                                                    else
                                                      if (sConceptName1.equals ("FreedoniaRegisteredOrganization"))
                                                        bean.setRegisteredOrganization(sValue);
                                                      else
                                                        if (sConceptName1.equals ("FreedoniaTelephoneNumber"))
                                                          bean.setTelephoneNumber (sValue);
                                                        else
                                                          {
                                                            ToopKafkaClient.send (EErrorLevel.WARN,
                                                                      () -> sLogPrefix +
                                                                            "Unsupported source concept name: '" +
                                                                            sConceptName1 +
                                                                            "'");
                                                          }
  }

  public void onToopResponse (@Nonnull final ToopResponseWithAttachments140 aResponseWA) throws IOException
  {
    final TDETOOPResponseType aResponse = aResponseWA.getResponse ();
    final ICommonsList <AsicReadEntry> attachments = aResponseWA.attachments ();

    DemoUIToopInterfaceHelper.dumpResponse (aResponse);

    final String sRequestID = aResponse.getDataRequestIdentifier ().getValue ();
    final String sLogPrefix = "[" + sRequestID + "] [DC] ";

    final TDEDataProviderType aDP = aResponse.hasNoDataProviderEntries () ? null : aResponse.getDataProviderAtIndex (0);

    ToopKafkaClient.send (EErrorLevel.INFO,
                          () -> sLogPrefix +
                                "Received data from Data Provider: " +
                                (aDP == null ? "null"
                                             : " DPIdentifier: " +
                                               aDP.getDPIdentifier ().getValue () +
                                               ", " +
                                               " DPName: " +
                                               aDP.getDPName ().getValue () +
                                               ", " +
                                               " DPElectronicAddressIdentifier: " +
                                               aResponse.getRoutingInformation ()
                                                        .getDataProviderElectronicAddressIdentifier ()
                                                        .getValue ()));

    ToopKafkaClient.send (EErrorLevel.INFO, () -> sLogPrefix + "Number of attachments: " + attachments.size ());
    for (final AsicReadEntry attachment : attachments)
    {
      ToopKafkaClient.send (EErrorLevel.INFO,
                            () -> sLogPrefix +
                                  "Received document: " +
                                  attachment.getEntryName () +
                                  ", size: " +
                                  attachment.payload ().length);
      // attachment.payload(); <-- this is the byte[]
    }

    // Push a new organization bean to the UI
    try
    {
      // Find the correct UI
      final UI aUI = DCToToopInterfaceMapper.getAndRemoveUI (sRequestID);
      if (aUI == null)
        throw new IllegalStateException ("Having no Vaadin UI for request ID '" + sRequestID + "'");

      ToopKafkaClient.send (EErrorLevel.INFO, () -> sLogPrefix + "Current UI: " + aUI);
      final Navigator threadUINavigator = aUI.getNavigator ();
      ToopKafkaClient.send (EErrorLevel.INFO, () -> sLogPrefix + "Current Navigator: " + threadUINavigator);

      final ToopDataBean bean = new ToopDataBean (attachments);
      // DocumentDataBean documentBean = new DocumentDataBean(attachments);

      // Get requested documents
      if (aResponse.getDocumentRequestCount () > 0)
      {
        ToopKafkaClient.send (EErrorLevel.INFO, () -> sLogPrefix + "Contains requested documents");
        aResponse.getDocumentRequest ().forEach (dRec -> {
          dRec.getDocumentResponse ().forEach (dResp -> {
            bean.getKeyValList ().add (new SimpleEntry <> ("Document Name:", dResp.getDocumentName ().getValue ()));
            bean.getKeyValList ()
                .add (new SimpleEntry <> ("Document Issue Date:",
                                          dResp.getDocumentIssueDate ().getValue ().toString ()));
            bean.getKeyValList ()
                .add (new SimpleEntry <> ("Document Issue Place:", dResp.getDocumentIssuePlace ().getValue ()));
            bean.getKeyValList ()
                .add (new SimpleEntry <> ("Document Description:", dResp.getDocumentDescription ().getValue ()));
            bean.getKeyValList ()
                .add (new SimpleEntry <> ("Document Identifier:", dResp.getDocumentIdentifier ().getValue ()));
            dResp.getDocument ().forEach (doc -> {
              bean.getKeyValList ().add (new SimpleEntry <> ("Document URI:", doc.getDocumentURI ().getValue ()));
              bean.getKeyValList ()
                  .add (new SimpleEntry <> ("Document MIME Type:", doc.getDocumentMimeTypeCode ().getValue ()));
            });
          });
        });
      }

      // Inspect all mapped values
      for (final TDEDataElementRequestType aDER : aResponse.getDataElementRequest ())
      {
        final TDEConceptRequestType aConcept1 = aDER.getConceptRequest ();
        final String sConceptName1 = aConcept1.getConceptName ().getValue ();
        if (_canUseConcept (aConcept1))
        {
          // Apply value from this concept
          _extractValue (sLogPrefix, sConceptName1, sConceptName1, aConcept1, bean);
        }
        else
          for (final TDEConceptRequestType aConcept2 : aConcept1.getConceptRequest ())
          {
            final String sConceptName2 = aConcept2.getConceptName ().getValue ();
            if (_canUseConcept (aConcept2))
            {
              // Apply value from this concept
              _extractValue (sLogPrefix, sConceptName1, sConceptName1 + " - " + sConceptName2, aConcept2, bean);
            }
            else
              for (final TDEConceptRequestType aConcept3 : aConcept2.getConceptRequest ())
              {
                final String sConceptName3 = aConcept3.getConceptName ().getValue ();

                // Three layers is all we have
                // Apply value from this concept
                _extractValue (sLogPrefix,
                               sConceptName1,
                               sConceptName1 + " - " + sConceptName2 + " - " + sConceptName3,
                               aConcept3,
                               bean);
              }
          }
      }

      if (threadUINavigator.getCurrentView () instanceof PhaseTwo)
      {
        final PhaseTwo homeView = (PhaseTwo) threadUINavigator.getCurrentView ();
        if (homeView.getCurrentPage () instanceof RegisterWithWEEEMainPage)
        {
          homeView.setToopDataBean (bean);
          final RegisterWithWEEEMainPage page = (RegisterWithWEEEMainPage) homeView.getCurrentPage ();
          page.addMainCompanyForm ();
        }
      }
      else
        if (threadUINavigator.getCurrentView () instanceof RequestToSwedenOne)
        {
          final RequestToSwedenOne homeView = (RequestToSwedenOne) threadUINavigator.getCurrentView ();
          if (homeView.getCurrentPage () instanceof RegisterWithWEEEMainPage)
          {
            homeView.setToopDataBean (bean);
            final RegisterWithWEEEMainPage page = (RegisterWithWEEEMainPage) homeView.getCurrentPage ();
            page.addMainCompanyForm ();
          }
        }
        else
          if (threadUINavigator.getCurrentView () instanceof RequestToSwedenTwo)
          {
            final RequestToSwedenTwo homeView = (RequestToSwedenTwo) threadUINavigator.getCurrentView ();
            if (homeView.getCurrentPage () instanceof RegisterWithWEEEMainPage)
            {
              homeView.setToopDataBean (bean);
              final RegisterWithWEEEMainPage page = (RegisterWithWEEEMainPage) homeView.getCurrentPage ();
              page.addMainCompanyForm ();
            }
          }
          else
            if (threadUINavigator.getCurrentView () instanceof RequestToSloveniaOne)
            {
              final RequestToSloveniaOne homeView = (RequestToSloveniaOne) threadUINavigator.getCurrentView ();
              if (homeView.getCurrentPage () instanceof RegisterWithWEEEMainPage)
              {
                homeView.setToopDataBean (bean);
                final RegisterWithWEEEMainPage page = (RegisterWithWEEEMainPage) homeView.getCurrentPage ();
                page.addMainCompanyForm ();
              }
            }
            else
              if (threadUINavigator.getCurrentView () instanceof RequestToSlovakiaOne)
              {
                final RequestToSlovakiaOne homeView = (RequestToSlovakiaOne) threadUINavigator.getCurrentView ();
                if (homeView.getCurrentPage () instanceof RegisterWithWEEEMainPage)
                {
                  homeView.setToopDataBean (bean);
                  final RegisterWithWEEEMainPage page = (RegisterWithWEEEMainPage) homeView.getCurrentPage ();
                  page.addMainCompanyForm ();
                }
              }
              else
                if (threadUINavigator.getCurrentView () instanceof RequestToSlovakiaTwo)
                {
                  final RequestToSlovakiaTwo homeView = (RequestToSlovakiaTwo) threadUINavigator.getCurrentView ();
                  if (homeView.getCurrentPage () instanceof RegisterWithWEEEMainPage)
                  {
                    homeView.setToopDataBean (bean);
                    final RegisterWithWEEEMainPage page = (RegisterWithWEEEMainPage) homeView.getCurrentPage ();
                    page.addMainCompanyForm ();
                  }
                }
                else
                  if (threadUINavigator.getCurrentView () instanceof RequestToItalyOne)
                  {
                    final RequestToItalyOne homeView = (RequestToItalyOne) threadUINavigator.getCurrentView ();
                    if (homeView.getCurrentPage () instanceof RegisterWithWEEEMainPage)
                    {
                      homeView.setToopDataBean (bean);
                      final RegisterWithWEEEMainPage page = (RegisterWithWEEEMainPage) homeView.getCurrentPage ();
                      page.addMainCompanyForm ();
                    }
                  }
                  else
                    if (threadUINavigator.getCurrentView () instanceof DynamicRequest)
                    {
                      final DynamicRequest homeView = (DynamicRequest) threadUINavigator.getCurrentView ();
                      if (homeView.getCurrentPage () instanceof DynamicRequestPage)
                      {
                        homeView.setToopDataBean (bean);
                        final DynamicRequestPage page = (DynamicRequestPage) homeView.getCurrentPage ();

                        final String expectedUuid = page.getRequestId ();

                        if (aResponse.getDataRequestIdentifier () != null &&
                            expectedUuid != null &&
                            expectedUuid.equals (aResponse.getDataRequestIdentifier ().getValue ()))
                        {
                          if (!aResponse.hasErrorEntries ())
                          {

                            final IdentifierType documentTypeIdentifier = aResponse.getRoutingInformation ()
                                                                                   .getDocumentTypeIdentifier ();

                            if (documentTypeIdentifier.getValue ().contains ("registeredorganization"))
                            {
                              page.addMainCompanyForm ();
                            }
                            else
                            {
                              page.addKeyValueForm ();
                            }
                          }
                          else
                          {
                            page.setError (aResponse.getError ());
                          }

                          final String conceptErrors = getConceptErrors (aResponse);
                          if (!conceptErrors.isEmpty ())
                          {
                            page.setConceptErrors (conceptErrors);
                          }
                        }
                      }
                    }
                    else
                      if (threadUINavigator.getCurrentView () instanceof Maritime)
                      {
                        final Maritime homeView = (Maritime) threadUINavigator.getCurrentView ();
                        if (homeView.getCurrentPage () instanceof MaritimePage)
                        {
                          homeView.setToopDataBean (bean);
                          // homeView.setDocumentDataBean(documentBean);
                          final MaritimePage page = (MaritimePage) homeView.getCurrentPage ();

                          final String expectedUuid = page.getRequestId ();

                          if (aResponse.getDataRequestIdentifier () != null &&
                              expectedUuid != null &&
                              expectedUuid.equals (aResponse.getDataRequestIdentifier ().getValue ()))
                          {
                            if (!aResponse.hasErrorEntries ())
                            {

                              final IdentifierType documentTypeIdentifier = aResponse.getRoutingInformation ()
                                                                                     .getDocumentTypeIdentifier ();

                              if (documentTypeIdentifier.getValue ().contains ("list"))
                              {
                                // add grid layout
                                final List <DocumentDataBean> docResponseList = DemoUIToopInterfaceHelper.getDocumentResponseDataBeanList (aResponse);
                                page.addDocumentCertificateList (docResponseList);
                                // page.addComponent(page.addDocumentCertificateList(docResponseList),
                                // "documentCertificateList");
                              }
                              else
                              {
                                if (attachments != null && attachments.size () > 0)
                                {
                                  for (final AsicReadEntry attachment : attachments)
                                  {
                                    final StreamResource myResource = new StreamResource ((StreamResource.StreamSource) () -> new ByteArrayInputStream (attachment.payload ()),
                                                                                          attachment.getEntryName ());
                                    myResource.getStream ()
                                              .setParameter ("Content-Disposition",
                                                             "attachment;filename=\"" +
                                                                                    attachment.getEntryName () +
                                                                                    "\"");
                                    myResource.setCacheTime (0);
                                    myResource.setMIMEType ("application/octet-stream");
                                    aUI.getPage ().open (myResource, "_top", false);
                                  }
                                }
                              }
                            }
                            else
                            {
                              page.setError (aResponse.getError ());
                            }

                            final String conceptErrors = getConceptErrors (aResponse);
                            if (!conceptErrors.isEmpty ())
                            {
                              page.setConceptErrors (conceptErrors);
                            }
                          }
                        }
                      }

      ToopKafkaClient.send (EErrorLevel.INFO, () -> sLogPrefix + "Pushed new bean data to the Demo UI: " + bean);
    }
    catch (final Exception e)
    {
      ToopKafkaClient.send (EErrorLevel.ERROR, () -> sLogPrefix + "Failed to push new bean data to the Demo UI", e);
    }
  }

  private static String getConceptErrors (@Nonnull final TDETOOPResponseType aResponse)
  {

    final StringBuilder sb = new StringBuilder ();

    // Inspect all mapped values
    for (final TDEDataElementRequestType aDER : aResponse.getDataElementRequest ())
    {

      final TDEConceptRequestType aFirstLevelConcept = aDER.getConceptRequest ();

      final String sourceConceptName = aFirstLevelConcept.getConceptName ().getValue ();

      for (final TDEConceptRequestType aSecondLevelConcept : aFirstLevelConcept.getConceptRequest ())
      {

        final String toopConceptName = aSecondLevelConcept.getConceptName ().getValue ();

        for (final TDEConceptRequestType aThirdLevelConcept : aSecondLevelConcept.getConceptRequest ())
        {

          final String destinationConceptName = aThirdLevelConcept.getConceptName ().getValue ();

          final String mappedConcept = sourceConceptName + " - " + toopConceptName + " - " + destinationConceptName;

          for (final TDEDataElementResponseValueType aThirdLevelConceptDERValue : aThirdLevelConcept.getDataElementResponseValue ())
          {

            if (aThirdLevelConceptDERValue.getErrorIndicator () != null &&
                aThirdLevelConceptDERValue.getErrorIndicator ().isValue ())
            {
              sb.append (" - Concept Error (").append (mappedConcept).append ("):\n");
              if (aThirdLevelConceptDERValue.getErrorCode () != null)
              {
                sb.append ("     ").append (aThirdLevelConceptDERValue.getErrorCode ().getValue ()).append ("\n");
              }
              else
                if (aThirdLevelConceptDERValue.getResponseDescription () != null)
                {
                  sb.append ("     ")
                    .append (aThirdLevelConceptDERValue.getResponseDescription ().getValue ())
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
