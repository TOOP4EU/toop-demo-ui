package eu.toop.demoui.view;

import com.vaadin.navigator.View;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class SuccessView extends VerticalLayout implements View {
  public SuccessView() {
    addComponent(new Label("Success!!"));
    addComponent(new Label("You have successfully registered the company Zizi mat in Freedonias public services system."));
    addComponent(new Label("Confirmation email will probably be sent right away but there will be communication in the next coming days from OPSP once registration is completed or if additional information is needed."));
    addComponent(new Label("If you have any enqueries or need any further information you can contact us in this email address: registration@freedonia-osps.free"));
  }
}
