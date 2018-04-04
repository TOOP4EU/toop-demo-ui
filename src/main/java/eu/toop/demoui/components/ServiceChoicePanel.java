package eu.toop.demoui.components;

import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.Label;

public class ServiceChoicePanel extends CustomLayout {
  public ServiceChoicePanel (String title, String image) {

    super ("ServiceChoicePanel");

    addComponent (new Label (title), "title");
  }
}
