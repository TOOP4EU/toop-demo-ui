package eu.toop.demoui.pages;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;

import eu.toop.demoui.view.BaseView;
import eu.toop.demoui.view.PhaseOne;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.commons.error.level.EErrorLevel;
import com.helger.commons.string.StringHelper;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import eu.toop.commons.concept.ConceptValue;
import eu.toop.commons.doctype.EToopDocumentType;
import eu.toop.commons.doctype.EToopProcess;
import eu.toop.commons.jaxb.ToopXSDHelper;
import eu.toop.demoui.components.ConfirmToopDataFetchingTable;
import eu.toop.iface.ToopInterfaceClient;
import eu.toop.kafkaclient.ToopKafkaClient;

public class ConfirmToopDataFetchingPage extends Window {

  private static final Logger s_aLogger = LoggerFactory.getLogger (ConfirmToopDataFetchingPage.class);

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

    final Button cancelButton = new Button ("I don't agree", (ClickListener) event -> subWindow.close ());

    final Button proceedButton = new Button ("I AGREE", (ClickListener) event -> {
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
            EToopDocumentType.DOCTYPE_REGISTERED_ORGANIZATION_REQUEST,
            EToopProcess.PROCESS_REQUEST_RESPONSE, conceptList);
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
