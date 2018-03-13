package eu.toop.demoui.pages;

import com.vaadin.ui.Button;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.themes.ValoTheme;
import eu.toop.demoui.view.HomeView;

public class BusinessServicesPage extends CustomLayout {

  public BusinessServicesPage (HomeView view) {
    super ("BusinessServicesPage");

    setHeight ("100%");

    Button nextButton = new Button ("Trade licenses and registrations");
    nextButton.addStyleName (ValoTheme.BUTTON_LINK);
    nextButton.setSizeUndefined ();
    addComponent (nextButton, "nextButton");
    nextButton.addClickListener(new Button.ClickListener() {
      public void buttonClick(Button.ClickEvent event) {
        nextButton.setCaption ("clicked");
        view.setCurrentPage (new TradeLicensesAndRegistrationsPage (view));

      }
    });
  }
}