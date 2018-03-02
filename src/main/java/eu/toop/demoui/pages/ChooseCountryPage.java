package eu.toop.demoui.pages;

import com.vaadin.ui.Button;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.themes.ValoTheme;
import eu.toop.demoui.view.HomeView;

public class ChooseCountryPage extends CustomLayout {
  public ChooseCountryPage (HomeView view) {
    super ("ChooseCountryPage");

    setHeight ("100%");

    Button nextButton = new Button ("Next");
    nextButton.addStyleName (ValoTheme.BUTTON_BORDERLESS);
    nextButton.addStyleName (" freedonia");
    addComponent (nextButton, "nextButton");
    nextButton.addClickListener(new Button.ClickListener() {
      public void buttonClick(Button.ClickEvent event) {
        view.setCurrentPage (new IdentityProviderPage (view));
      }
    });
  }
}