package eu.toop.demoui.pages;

import com.vaadin.ui.Button;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.themes.ValoTheme;
import eu.toop.demoui.view.HomeView;

public class ChooseAuthenticationMethodPage extends CustomLayout {

  public ChooseAuthenticationMethodPage (HomeView view) {
    super ("ChooseAuthenticationMethodPage");

    setHeight ("100%");

    Button freedoniaCredentialsButton = new Button ("Freedonia credentials");
    freedoniaCredentialsButton.addStyleName (ValoTheme.BUTTON_BORDERLESS);
    freedoniaCredentialsButton.addStyleName (" freedonia-auth-method-button");
    addComponent (freedoniaCredentialsButton, "freedoniaCredentialsButton");

    Button nextButton = new Button ("European eID");
    nextButton.addStyleName (ValoTheme.BUTTON_BORDERLESS);
    nextButton.addStyleName (" freedonia-auth-method-button");
    addComponent (nextButton, "nextButton");
    nextButton.addClickListener(new Button.ClickListener() {
      public void buttonClick(Button.ClickEvent event) {
        nextButton.setCaption ("clicked");
        view.setCurrentPage (new ChooseCountryPage (view));
      }
    });
  }
}