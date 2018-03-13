package eu.toop.demoui.pages;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Arrays;

import com.helger.commons.error.level.EErrorLevel;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import eu.toop.commons.concept.ConceptValue;
import eu.toop.commons.doctype.EToopDocumentType;
import eu.toop.commons.doctype.EToopProcess;
import eu.toop.demoui.components.ConfirmToopDataFetchingTable;
import eu.toop.demoui.view.HomeView;
import eu.toop.iface.ToopInterfaceManager;
import eu.toop.kafkaclient.ToopKafkaClient;

public class ConfirmToopDataFetchingPage extends Window {
  public ConfirmToopDataFetchingPage (final HomeView view) {

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

      { // Adding mockup data to the main company form for a live demo purpose
        view.getMainCompany ().setCompanyName ("Zizi mat");
        view.getMainCompany ().setCompanyCode ("JF 234556-6213");
        view.getMainCompany ().setCompanyType ("Limited");
        view.getMainCompany ().setLegalStatus ("Active");
        view.getMainCompany ().setRegistrationNumber ("009987 665543");
        view.getMainCompanyForm ().setOrganizationBean (view.getMainCompany ());
        view.getMainCompanyForm ().save ();
      }

      // Notify the Package-Tracker that we are sending a TOOP Message!
      ToopKafkaClient.send (EErrorLevel.INFO, "'dc.freedonia.toop' -> 'tc.freedonia.toop'");

      // Send the request to the Message-Processor
      try {
        ToopInterfaceManager.requestConcepts ("iso6523-actorid-upis::9999:freedonia", "SV",
                                              EToopDocumentType.DOCTYPE_REGISTERED_ORGANIZATION_REQUEST,
                                              EToopProcess.PROCESS_REQUEST_RESPONSE,
                                              Arrays.asList (new ConceptValue ("http://example.register.fre/freedonia-business-register",
                                                                               "companyCode")));
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
