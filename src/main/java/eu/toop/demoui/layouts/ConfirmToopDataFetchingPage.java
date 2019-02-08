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

import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Nonnull;

import com.helger.commons.error.level.EErrorLevel;
import com.helger.commons.string.StringHelper;
import com.vaadin.ui.Button;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

import eu.toop.commons.codelist.EPredefinedDocumentTypeIdentifier;
import eu.toop.commons.codelist.EPredefinedProcessIdentifier;
import eu.toop.commons.concept.ConceptValue;
import eu.toop.commons.dataexchange.v140.TDEAddressType;
import eu.toop.commons.dataexchange.v140.TDEDataRequestSubjectType;
import eu.toop.commons.dataexchange.v140.TDELegalPersonType;
import eu.toop.commons.dataexchange.v140.TDENaturalPersonType;
import eu.toop.commons.dataexchange.v140.TDETOOPRequestType;
import eu.toop.commons.error.ToopErrorException;
import eu.toop.commons.exchange.ToopMessageBuilder;
import eu.toop.commons.jaxb.ToopWriter;
import eu.toop.commons.jaxb.ToopXSDHelper;
import eu.toop.demoui.DCUIConfig;
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

      // Send the request to the Message-Processor
      try {

        String destinationCountryCode = view.getIdentity ().getNationality ();
        if (destinationCountryCode == null) {
          destinationCountryCode = DCUIConfig.getDestinationCountryCode ();
        }

        final String conceptNamespace = "http://example.register.fre/freedonia-business-register";

        final List<ConceptValue> conceptList = new ArrayList<> ();

        conceptList.add (new ConceptValue (conceptNamespace, "FreedoniaAddress"));
        conceptList.add (new ConceptValue (conceptNamespace, "FreedoniaSSNumber"));
        conceptList.add (new ConceptValue (conceptNamespace, "FreedoniaBusinessCode"));
        conceptList.add (new ConceptValue (conceptNamespace, "FreedoniaVATNumber"));
        conceptList.add (new ConceptValue (conceptNamespace, "FreedoniaCompanyType"));
        conceptList.add (new ConceptValue (conceptNamespace, "FreedoniaRegistrationDate"));
        conceptList.add (new ConceptValue (conceptNamespace, "FreedoniaRegistrationNumber"));
        conceptList.add (new ConceptValue (conceptNamespace, "FreedoniaCompanyName"));
        conceptList.add (new ConceptValue (conceptNamespace, "FreedoniaCompanyNaceCode"));
        conceptList.add (new ConceptValue (conceptNamespace, "FreedoniaActivityDeclaration"));
        conceptList.add (new ConceptValue (conceptNamespace, "FreedoniaRegistrationAuthority"));
        conceptList.add (new ConceptValue (conceptNamespace, "FreedoniaLegalStatus"));
        conceptList.add (new ConceptValue (conceptNamespace, "FreedoniaLegalStatusEffectiveDate"));

        // Notify the logger and Package-Tracker that we are sending a TOOP Message!
        ToopKafkaClient.send (EErrorLevel.INFO, () -> "[DC] Requesting concepts: "
            + StringHelper.getImplodedMapped (", ", conceptList, x -> x.getNamespace () + "#" + x.getValue ()));

        final TDEDataRequestSubjectType aDS = new TDEDataRequestSubjectType ();
        aDS.setDataRequestSubjectTypeCode (ToopXSDHelper.createCode ("12345"));
        {
          final TDENaturalPersonType aNP = new TDENaturalPersonType ();
          aNP.setPersonIdentifier (ToopXSDHelper.createIdentifierWithLOA (view.getIdentity ().getIdentifier ()));
          aNP.setFamilyName (ToopXSDHelper.createTextWithLOA (view.getIdentity ().getFamilyName ()));
          aNP.setFirstName (ToopXSDHelper.createTextWithLOA (view.getIdentity ().getFirstName ()));
          aNP.setBirthDate (ToopXSDHelper.createDateWithLOANow ());
          final TDEAddressType aAddress = new TDEAddressType ();
          // Destination country to use
          aAddress.setCountryCode (ToopXSDHelper.createCodeWithLOA (destinationCountryCode));
          aNP.setNaturalPersonLegalAddress (aAddress);
          aDS.setNaturalPerson (aNP);
        }

        if (view.getIdentity ().getLegalPersonIdentifier () != null
            && !view.getIdentity ().getLegalPersonIdentifier ().isEmpty ()) {
          final TDELegalPersonType aLE = new TDELegalPersonType ();
          aLE.setLegalPersonUniqueIdentifier (
              ToopXSDHelper.createIdentifierWithLOA (view.getIdentity ().getLegalPersonIdentifier ()));
          aLE.setLegalEntityIdentifier (
              ToopXSDHelper.createIdentifierWithLOA (view.getIdentity ().getLegalPersonIdentifier ()));
          aLE.setLegalName (ToopXSDHelper.createTextWithLOA (view.getIdentity ().getLegalPersonName ()));
          final TDEAddressType aAddress = new TDEAddressType ();
          // Destination country to use
          aAddress.setCountryCode (ToopXSDHelper.createCodeWithLOA (view.getIdentity ().getLegalPersonNationality ()));
          aLE.setLegalPersonLegalAddress (aAddress);
          aDS.setLegalPerson (aLE);
        }

        ToopKafkaClient.send (EErrorLevel.INFO,
            () -> "[DC] Sending request to TC: " + ToopInterfaceConfig.getToopConnectorDCUrl ());

        final TDETOOPRequestType aRequest = ToopMessageBuilder.createMockRequest (aDS,
            ToopXSDHelper.createIdentifier (DCUIConfig.getSenderIdentifierScheme (),
                DCUIConfig.getSenderIdentifierValue ()),
            EPredefinedDocumentTypeIdentifier.REQUEST_REGISTEREDORGANIZATION,
            EPredefinedProcessIdentifier.DATAREQUESTRESPONSE, conceptList);

        dumpRequest (aRequest);

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

  private void dumpRequest (@Nonnull final TDETOOPRequestType aRequest) {

    FileWriter fw = null;
    try {

      final DateFormat dateFormat = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss.SSS");
      final String filePath = String.format ("%s/request-dump-%s.log", DCUIConfig.getDumpResponseDirectory (),
          dateFormat.format (new Date ()));

      final String requestXml = ToopWriter.request ().getAsString (aRequest);
      fw = new FileWriter (filePath);
      if (requestXml != null) {
        fw.write (requestXml);
      }
    } catch (final IOException e) {
      e.printStackTrace ();
    } finally {
      if (fw != null) {
        try {
          fw.close ();
        } catch (final IOException e) {
          e.printStackTrace ();
        }
      }
    }
  }
}
