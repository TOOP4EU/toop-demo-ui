package eu.toop.demoui.layouts;

import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.Label;

public class ServiceChoicePanel extends CustomLayout {
  public ServiceChoicePanel (final String title) {

    super ("ServiceChoicePanel");

    addComponent (new Label (title), "title");
  }
}
