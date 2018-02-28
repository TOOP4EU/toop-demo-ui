package eu.toop.demoui.view;

import com.vaadin.navigator.View;
import com.vaadin.ui.*;
import eu.toop.demoui.pages.HomePage;

public class HomeView extends CustomLayout implements View {

  public HomeView () {
    super("HomeView");
    setHeight ("100%");

    addComponent (new HomePage (this), "page");
  }
}
