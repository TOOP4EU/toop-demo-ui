package eu.toop.demoui.components;

import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class Footer extends VerticalLayout {
  public Footer () {
    setMargin (false);
    setStyleName ("footer");
    setWidth ("100%");

    addComponent (new Label (" "));
  }
}
