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

      // Notify the Package-Tracker that we are sending a TOOP Message!
      ToopKafkaClient.send (EErrorLevel.INFO, "'dc.freedonia.toop' -> 'mp.freedonia.toop'");

      // Send the request to the Message-Processor
      try {
        final String NS = "http://toop.eu/organization";
        ToopInterfaceManager.requestConcepts (Arrays.asList (new ConceptValue (NS, "companyName"),
                                                             new ConceptValue (NS, "companyType")));
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
