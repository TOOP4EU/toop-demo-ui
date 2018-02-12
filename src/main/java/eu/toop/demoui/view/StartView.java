package eu.toop.demoui.view;

import com.vaadin.navigator.View;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class StartView extends VerticalLayout implements View {

    public StartView() {

        addComponent(new Label("TOOP demo user interface"));

        Button eIDModuleButton = new Button("Go to the eID-Module!");
        eIDModuleButton.addClickListener(e -> {
            getUI().getNavigator().navigateTo("eIDModuleView");
        });
        addComponents(eIDModuleButton);

        // TODO: Preview Identity the claim data retrieved from eID-Module

        Button toopButton = new Button("Pre-fill my form using TOOP!");
        toopButton.addClickListener(e -> {
            // TODO: Start construction of ASIC-container.
            // TODO: Serialize MSDataRequest container into binary and store in Asic-container.
            // TODO: Use toop-interface to send the Asic-container to the Message-Processor!
            System.out.println("TODO: Use the toop-interface to send to Message-Processor!");
        });
        addComponents(toopButton);
    }
}
