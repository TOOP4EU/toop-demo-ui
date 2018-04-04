package eu.toop.demoui.pages;

import com.vaadin.ui.Button;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.themes.ValoTheme;

import eu.toop.demoui.view.BaseView;
import eu.toop.demoui.view.PhaseOne;

public class TradeLicensesAndRegistrationsPage extends CustomLayout {

  public TradeLicensesAndRegistrationsPage (BaseView view) {

    super ("TradeLicensesAndRegistrationsPage");

    setHeight ("100%");

    Button nextButton = new Button ("Waste Electrical and Electronic Equipment - WEEE Register");
    nextButton.addStyleName (ValoTheme.BUTTON_LINK);
    nextButton.setSizeUndefined ();
    addComponent (nextButton, "nextButton");
    nextButton.addClickListener (new Button.ClickListener () {
      public void buttonClick (Button.ClickEvent event) {

        nextButton.setCaption ("clicked");
        view.setCurrentPage (new ChooseAuthenticationMethodPage (view));

      }
    });
  }
}
