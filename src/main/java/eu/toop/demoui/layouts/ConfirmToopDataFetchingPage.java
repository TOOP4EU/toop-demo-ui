package eu.toop.demoui.layouts;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;

import com.helger.commons.error.level.EErrorLevel;
import com.helger.commons.string.StringHelper;
import com.vaadin.ui.Button;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import eu.toop.commons.codelist.EPredefinedDocumentTypeIdentifier;
import eu.toop.commons.codelist.EPredefinedProcessIdentifier;
import eu.toop.commons.concept.ConceptValue;
import eu.toop.commons.jaxb.ToopXSDHelper;
import eu.toop.demoui.view.BaseView;
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

    final Button cancelButton = new Button ("I don't agree", (event) -> subWindow.close ());

    final Button proceedButton = new Button ("I AGREE", (event) -> {
      onConsent ();
      subWindow.close ();

      // Send the request to the Message-Processor
      try {
        final List<ConceptValue> conceptList = new ArrayList<> ();

        conceptList.add (new ConceptValue ("http://example.register.fre/freedonia-business-register",
                                           "FreedoniaAddress"));
        conceptList.add (new ConceptValue ("http://example.register.fre/freedonia-business-register",
                                           "FreedoniaSSNumber"));
        conceptList.add (new ConceptValue ("http://example.register.fre/freedonia-business-register",
                                           "FreedoniaBusinessCode"));
        conceptList.add (new ConceptValue ("http://example.register.fre/freedonia-business-register",
                                           "FreedoniaVATNumber"));
        conceptList.add (new ConceptValue ("http://example.register.fre/freedonia-business-register",
                                           "FreedoniaCompanyType"));
        conceptList.add (new ConceptValue ("http://example.register.fre/freedonia-business-register",
                                           "FreedoniaRegistrationDate"));
        conceptList.add (new ConceptValue ("http://example.register.fre/freedonia-business-register",
                                           "FreedoniaRegistrationNumber"));
        conceptList.add (new ConceptValue ("http://example.register.fre/freedonia-business-register",
                                           "FreedoniaCompanyName"));
        conceptList.add (new ConceptValue ("http://example.register.fre/freedonia-business-register",
                                           "FreedoniaCompanyNaceCode"));
        conceptList.add (new ConceptValue ("http://example.register.fre/freedonia-business-register",
                                           "FreedoniaActivityDeclaration"));
        conceptList.add (new ConceptValue ("http://example.register.fre/freedonia-business-register",
                                           "FreedoniaRegistrationAuthority"));

        // Notify the logger and Package-Tracker that we are sending a TOOP Message!
        ToopKafkaClient.send (EErrorLevel.INFO,
                              () -> "[DC] Requesting concepts: "
                                    + StringHelper.getImplodedMapped (", ", conceptList,
                                                                      x -> x.getNamespace () + "#" + x.getValue ()));

        ToopInterfaceClient.createRequestAndSendToToopConnector (ToopXSDHelper.createIdentifier ("iso6523-actorid-upis",
                                                                                                 "9999:freedonia"),
                                                                 "SV",
                                                                 EPredefinedDocumentTypeIdentifier.REQUEST_REGISTEREDORGANIZATION,
                                                                 EPredefinedProcessIdentifier.DATAREQUESTRESPONSE,
                                                                 conceptList);
      } catch (final IOException ex) {
        // Convert from checked to unchecked
        throw new UncheckedIOException (ex);
      }
    });

    subContent.addComponent (cancelButton);
    subContent.addComponent (proceedButton);

    // Center it in the browser window
    subWindow.center ();

    // Open it in the UI
    view.getUI ().addWindow (subWindow);
  }

  protected void onConsent () {

  }
}
