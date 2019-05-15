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
package eu.toop.demoui.layouts;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.helger.commons.error.level.EErrorLevel;
import com.helger.commons.string.StringHelper;
import com.vaadin.ui.Button;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

import eu.toop.commons.codelist.EPredefinedDocumentTypeIdentifier;
import eu.toop.commons.codelist.EPredefinedProcessIdentifier;
import eu.toop.commons.concept.ConceptValue;
import eu.toop.commons.dataexchange.v140.TDEAddressWithLOAType;
import eu.toop.commons.dataexchange.v140.TDEDataRequestSubjectType;
import eu.toop.commons.dataexchange.v140.TDELegalPersonType;
import eu.toop.commons.dataexchange.v140.TDENaturalPersonType;
import eu.toop.commons.dataexchange.v140.TDETOOPRequestType;
import eu.toop.commons.error.ToopErrorException;
import eu.toop.commons.exchange.ToopMessageBuilder140;
import eu.toop.commons.jaxb.ToopXSDHelper140;
import eu.toop.demoui.DCUIConfig;
import eu.toop.demoui.endpoints.DemoUIToopInterfaceHelper;
import eu.toop.demoui.view.BaseView;
import eu.toop.iface.ToopInterfaceClient;
import eu.toop.iface.ToopInterfaceConfig;
import eu.toop.kafkaclient.ToopKafkaClient;

public class ConfirmToopDataFetchingPage extends Window {
  public ConfirmToopDataFetchingPage (final BaseView view) {

    final Window subWindow = new Window ("Sub-window");
    final VerticalLayout subContent = new VerticalLayout ();
    subWindow.setContent (subContent);

    subWindow.setWidth ("800px");

    subWindow.setModal (true);
    subWindow.setCaption (null);
    subWindow.setResizable (false);
    subWindow.setClosable (false);

    // Put some components in it
    final ConfirmToopDataFetchingTable confirmToopDataFetchingTable = new ConfirmToopDataFetchingTable ();
    subContent.addComponent (confirmToopDataFetchingTable);

    final Button proceedButton = new Button ("Please request this information through TOOP", event -> {
      onConsent ();
      subWindow.close ();

      // SendRequest the request to the Message-Processor
      try {

        String destinationCountryCode = view.getIdentity ().getNationality ();
        if (destinationCountryCode == null) {
          destinationCountryCode = DCUIConfig.getDestinationCountryCode ();
        }

        final List<ConceptValue> conceptList = new ArrayList<> ();

        conceptList.add (new ConceptValue (DCUIConfig.getConceptNamespace (), "FreedoniaStreetAddress"));
        conceptList.add (new ConceptValue (DCUIConfig.getConceptNamespace (), "FreedoniaSSNumber"));
        conceptList.add (new ConceptValue (DCUIConfig.getConceptNamespace (), "FreedoniaCompanyCode"));
        conceptList.add (new ConceptValue (DCUIConfig.getConceptNamespace (), "FreedoniaVATNumber"));
        conceptList.add (new ConceptValue (DCUIConfig.getConceptNamespace (), "FreedoniaCompanyType"));
        conceptList.add (new ConceptValue (DCUIConfig.getConceptNamespace (), "FreedoniaRegistrationDate"));
        conceptList.add (new ConceptValue (DCUIConfig.getConceptNamespace (), "FreedoniaRegistrationNumber"));
        conceptList.add (new ConceptValue (DCUIConfig.getConceptNamespace (), "FreedoniaCompanyName"));
        conceptList.add (new ConceptValue (DCUIConfig.getConceptNamespace (), "FreedoniaNaceCode"));
        conceptList.add (new ConceptValue (DCUIConfig.getConceptNamespace (), "FreedoniaActivityDescription"));
        conceptList.add (new ConceptValue (DCUIConfig.getConceptNamespace (), "FreedoniaRegistrationAuthority"));
        conceptList.add (new ConceptValue (DCUIConfig.getConceptNamespace (), "FreedoniaLegalStatus"));
        conceptList.add (new ConceptValue (DCUIConfig.getConceptNamespace (), "FreedoniaLegalStatusEffectiveDate"));

        // Notify the logger and Package-Tracker that we are sending a TOOP Message!
        ToopKafkaClient.send (EErrorLevel.INFO, () -> "[DC] Requesting concepts: "
            + StringHelper.getImplodedMapped (", ", conceptList, x -> x.getNamespace () + "#" + x.getValue ()));

        final String identifierPrefix = DCUIConfig.getSenderCountryCode() + "/" + destinationCountryCode + "/";

        final TDEDataRequestSubjectType aDS = new TDEDataRequestSubjectType ();
        {
          aDS.setDataRequestSubjectTypeCode (ToopXSDHelper140.createCode ("NP"));
          final TDENaturalPersonType aNP = new TDENaturalPersonType ();
          aNP.setPersonIdentifier (ToopXSDHelper140.createIdentifierWithLOA (identifierPrefix + view.getIdentity ().getIdentifier ()));
          aNP.setFamilyName (ToopXSDHelper140.createTextWithLOA (view.getIdentity ().getFamilyName ()));
          aNP.setFirstName (ToopXSDHelper140.createTextWithLOA (view.getIdentity ().getFirstName ()));
          aNP.setBirthDate (ToopXSDHelper140.createDateWithLOANow ());
          final TDEAddressWithLOAType aAddress = new TDEAddressWithLOAType ();
          // Destination country to use
          aAddress.setCountryCode (ToopXSDHelper140.createCodeWithLOA (destinationCountryCode));
          aNP.setNaturalPersonLegalAddress (aAddress);
          aDS.setNaturalPerson (aNP);
        }

        if (view.getIdentity ().getLegalPersonIdentifier () != null
            && !view.getIdentity ().getLegalPersonIdentifier ().isEmpty ()) {
          aDS.setDataRequestSubjectTypeCode (ToopXSDHelper140.createCode ("LE"));
          final TDELegalPersonType aLE = new TDELegalPersonType ();
          aLE.setLegalPersonUniqueIdentifier (
              ToopXSDHelper140.createIdentifierWithLOA (identifierPrefix + view.getIdentity ().getLegalPersonIdentifier ()));
          aLE.setLegalName (ToopXSDHelper140.createTextWithLOA (view.getIdentity ().getLegalPersonName ()));
          final TDEAddressWithLOAType aAddress = new TDEAddressWithLOAType ();
          // Destination country to use
          aAddress
              .setCountryCode (ToopXSDHelper140.createCodeWithLOA (view.getIdentity ().getLegalPersonNationality ()));
          aLE.setLegalPersonLegalAddress (aAddress);
          aDS.setLegalPerson (aLE);
        }

        ToopKafkaClient.send (EErrorLevel.INFO,
            () -> "[DC] Sending request to TC: " + ToopInterfaceConfig.getToopConnectorDCUrl ());

        final TDETOOPRequestType aRequest = ToopMessageBuilder140.createMockRequest (aDS, DCUIConfig.getSenderCountryCode(),
            destinationCountryCode,
            ToopXSDHelper140.createIdentifier (DCUIConfig.getSenderIdentifierScheme (),
                DCUIConfig.getSenderIdentifierValue ()),
                EPredefinedDocumentTypeIdentifier.URN_EU_TOOP_NS_DATAEXCHANGE_1P40_REQUEST_URN_EU_TOOP_REQUEST_REGISTEREDORGANIZATION_1_40,
                EPredefinedProcessIdentifier.DATAREQUESTRESPONSE,
                conceptList);

        aRequest.setDocumentUniversalUniqueIdentifier (ToopXSDHelper140.createIdentifierUUID ());
        aRequest.setSpecificationIdentifier (ToopXSDHelper140.createIdentifier(EPredefinedDocumentTypeIdentifier.DOC_TYPE_SCHEME, "urn:eu:toop:ns:dataexchange-1p40::Request"));

        DemoUIToopInterfaceHelper.dumpRequest (aRequest);

        ToopInterfaceClient.sendRequestToToopConnector (aRequest);
      } catch (final IOException | ToopErrorException ex) {
        // Convert from checked to unchecked
        throw new RuntimeException (ex);
      }
    });
    proceedButton.addStyleName (ValoTheme.BUTTON_BORDERLESS);
    proceedButton.addStyleName ("ConsentAgreeButton");

    final Button cancelButton = new Button ("Thanks, I will provide this information myself", event -> {
      onSelfProvide ();
      subWindow.close ();
    });
    cancelButton.addStyleName (ValoTheme.BUTTON_BORDERLESS);
    cancelButton.addStyleName ("ConsentCancelButton");

    subContent.addComponent (proceedButton);
    subContent.addComponent (cancelButton);

    // Center it in the browser window
    subWindow.center ();

    // Open it in the UI
    view.getUI ().addWindow (subWindow);
  }

  protected void onConsent () {
    // The user may override this method to execute their own code when the user
    // click on the 'consent'-button.
  }

  protected void onSelfProvide () {
    // The user may override this method to execute their own code when the user
    // click on the 'self-provide'-button.
  }
}
