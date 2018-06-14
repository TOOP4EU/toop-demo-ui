package eu.toop.demoui.layouts;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;

import com.helger.datetime.util.PDTXMLConverter;
import com.vaadin.ui.themes.ValoTheme;
import eu.toop.commons.codelist.EPredefinedDocumentTypeIdentifier;
import eu.toop.commons.codelist.EPredefinedProcessIdentifier;
import eu.toop.commons.dataexchange.TDEAddressType;
import eu.toop.commons.dataexchange.TDEDataRequestSubjectType;
import eu.toop.commons.dataexchange.TDENaturalPersonType;
import eu.toop.demoui.view.BaseView;

import com.helger.commons.error.level.EErrorLevel;
import com.helger.commons.string.StringHelper;
import com.vaadin.ui.Button;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import eu.toop.commons.concept.ConceptValue;
import eu.toop.commons.jaxb.ToopXSDHelper;
import eu.toop.iface.ToopInterfaceClient;
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

        // Notify the logger and Package-Tracker that we are sending a TOOP Message!
        ToopKafkaClient.send (EErrorLevel.INFO,
            () -> "[DC] Requesting concepts: "
                + StringHelper.getImplodedMapped (", ", conceptList,
                x -> x.getNamespace () + "#" + x.getValue ()));

        final TDEDataRequestSubjectType aDS = new TDEDataRequestSubjectType ();
        aDS.setDataRequestSubjectTypeCode (ToopXSDHelper.createCode ("12345"));
        {
          final TDENaturalPersonType aNP = new TDENaturalPersonType ();
          aNP.setPersonIdentifier (ToopXSDHelper.createIdentifier (view.getIdentity ().getIdentifier ()));
          aNP.setFamilyName (ToopXSDHelper.createText (view.getIdentity ().getFamilyName ()));
          aNP.setFirstName (ToopXSDHelper.createText (view.getIdentity ().getFirstName ()));
          aNP.setBirthDate (PDTXMLConverter.getXMLCalendarDateNow ());
          final TDEAddressType aAddress = new TDEAddressType ();
          // Destination country to use
          aAddress.setCountryCode (ToopXSDHelper.createCode ("SV"));
          aNP.setNaturalPersonLegalAddress (aAddress);
          aDS.setNaturalPerson (aNP);
        }

        ToopInterfaceClient.createRequestAndSendToToopConnector (aDS,
            ToopXSDHelper.createIdentifier ("iso6523-actorid-upis",
            "9999:freedonia"),
            "SV",
            EPredefinedDocumentTypeIdentifier.REQUEST_REGISTEREDORGANIZATION,
            EPredefinedProcessIdentifier.DATAREQUESTRESPONSE, conceptList);
      } catch (final IOException ex) {
        // Convert from checked to unchecked
        throw new UncheckedIOException (ex);
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
    // The user may override this method to execute their own code when the user click on the 'consent'-button.
  }

  protected void onSelfProvide () {
    // The user may override this method to execute their own code when the user click on the 'self-provide'-button.
  }
}
