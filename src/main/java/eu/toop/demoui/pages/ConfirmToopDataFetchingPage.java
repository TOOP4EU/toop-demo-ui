package eu.toop.demoui.pages;

import com.vaadin.server.Extension;
import com.vaadin.ui.Button;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import eu.toop.commons.concept.ConceptValue;
import eu.toop.demoui.bean.MainCompany;
import eu.toop.demoui.components.ConfirmToopDataFetchingTable;
import eu.toop.demoui.form.MainCompanyForm;
import eu.toop.demoui.view.HomeView;
import eu.toop.iface.ToopInterfaceManager;
import eu.toop.kafkaclient.ToopKafkaClient;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Arrays;

public class ConfirmToopDataFetchingPage extends Window {
  public ConfirmToopDataFetchingPage (HomeView view) {

    Window subWindow = new Window ("Sub-window");
    VerticalLayout subContent = new VerticalLayout();
    subWindow.setContent (subContent);

    subWindow.setWidth ("800px");

    subWindow.setModal (true);
    subWindow.setCaption (null);
    subWindow.setResizable (false);
    subWindow.setClosable (false);

    // Put some components in it
    ConfirmToopDataFetchingTable confirmToopDataFetchingTable = new ConfirmToopDataFetchingTable ();
    subContent.addComponent (confirmToopDataFetchingTable);

    Button cancelButton = new Button ("I don't agree", new Button.ClickListener() {
      public void buttonClick(Button.ClickEvent event) {
        subWindow.close ();
      }
    });

    Button proceedButton = new Button ("I AGREE", new Button.ClickListener() {
      public void buttonClick(Button.ClickEvent event) {
        onConsent ();
        subWindow.close ();

        // Notify the Package-Tracker that we are sending a TOOP Message!
        ToopKafkaClient.setEnabled (true);
        ToopKafkaClient.send("", "'dc.freedonia.toop' -> 'mp.freedonia.toop'");
        ToopKafkaClient.close ();

        // Send the request to the Message-Processor
        try {
          final String NS = "http://toop.eu/organization";
          ToopInterfaceManager.requestConcepts (Arrays.asList (new ConceptValue (NS, "companyName"),
            new ConceptValue (NS, "companyType")));
        } catch (final IOException ex) {
          // Convert from checked to unchecked
          throw new UncheckedIOException (ex);
        }
      }
    });

    subContent.addComponent (cancelButton);
    subContent.addComponent (proceedButton);

    // Center it in the browser window
    subWindow.center();

    // Open it in the UI
    view.getUI ().addWindow (subWindow);
  }

  protected void onConsent () {

  }
}
