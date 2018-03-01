package eu.toop.demoui.pages;

import com.vaadin.ui.Button;
import com.vaadin.ui.CustomLayout;
import eu.toop.demoui.view.HomeView;

public class TradeLicensesAndRegistrationsPage extends CustomLayout {

  public TradeLicensesAndRegistrationsPage (HomeView view) {
    super ("TradeLicensesAndRegistrationsPage");

    setHeight ("100%");

    Button nextButton = new Button ("Next");
    addComponent (nextButton, "nextButton");
    nextButton.addClickListener(new Button.ClickListener() {
      public void buttonClick(Button.ClickEvent event) {
        nextButton.setCaption ("clicked");
      }
    });
  }
}
