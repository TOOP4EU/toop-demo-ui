package eu.toop.demoui.view;

import com.vaadin.navigator.View;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import eu.toop.iface.mockup.MSDataRequest;
import eu.toop.iface.mockup.TOOPDataRequest;
import eu.toop.iface.mockup.TOOPMessageBundle;
import eu.toop.iface.mockup.TOOPMessageBundleBuilder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class StartView extends VerticalLayout implements View {

    public StartView() {

        addComponent(new Label("TOOP demo user interface"));

        Button eIDModuleButton = new Button("Go to the eID-Module!");
        eIDModuleButton.addClickListener(e -> {
            getUI().getNavigator().navigateTo("eIDModuleView");
        });
        addComponents(eIDModuleButton);

        Button toopButton = new Button("Pre-fill my form using TOOP!");
        toopButton.addClickListener(e -> {
            ByteArrayOutputStream archiveOutput = new ByteArrayOutputStream();
            final File keystoreFile = new File("src/main/resources/demo-keystore.jks");
            final String keystorePassword = "password";
            final String keyPassword = "password";

            try {
                TOOPMessageBundle bundle = new TOOPMessageBundleBuilder()
                        .setMSDataRequest(new MSDataRequest("ABC123"))
                        .sign(archiveOutput, keystoreFile, keystorePassword, keyPassword);
                eu.toop.iface.mockup.client.SendToMPClient.httpClientCall(archiveOutput.toByteArray());
                archiveOutput.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
        addComponents(toopButton);
    }
}
