package eu.toop.demoui.view;

import com.vaadin.navigator.View;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class TempView extends VerticalLayout implements View {
  public TempView() {
    addComponent (new Label ("Temp view!"));
  }
}
