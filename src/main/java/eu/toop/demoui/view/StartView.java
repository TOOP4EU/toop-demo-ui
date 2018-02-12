package eu.toop.demoui.view;

import com.vaadin.navigator.View;
import com.vaadin.ui.Button;
import com.vaadin.ui.VerticalLayout;

public class StartView extends VerticalLayout implements View {

    final String EIDMODULE_URL = "http://138.68.103.237:8090/login";

    public StartView() {
        Button toopButton = new Button("Go TOOP!");
        toopButton.addClickListener(e -> {
            System.out.println("Go TOOP!");
        });

        addComponents(toopButton);
    }
}
