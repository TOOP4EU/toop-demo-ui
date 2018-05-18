package eu.toop.demoui.layouts;

import com.vaadin.ui.Button;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.themes.ValoTheme;

import eu.toop.demoui.view.BaseView;

public class TradeLicensesAndRegistrationsPage extends CustomLayout {

  public TradeLicensesAndRegistrationsPage (final BaseView view) {

    super ("TradeLicensesAndRegistrationsPage");

    setHeight ("100%");

    final Button nextButton = new Button ("Registration of Business activity");
    nextButton.addStyleName (ValoTheme.BUTTON_LINK);
    nextButton.setSizeUndefined ();
    addComponent (nextButton, "nextButton");
    nextButton.addClickListener ((event) -> {

      nextButton.setCaption ("clicked");
      view.setCurrentPage (new ChooseAuthenticationMethodPage (view));
    });
  }
}
