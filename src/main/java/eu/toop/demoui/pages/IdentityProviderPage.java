package eu.toop.demoui.pages;

import com.vaadin.ui.Button;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;
import eu.toop.demoui.view.HomeView;

public class IdentityProviderPage extends CustomLayout {
  public IdentityProviderPage (HomeView view) {
    super ("IdentityProviderPage");

    setHeight ("100%");

    TextField usernameField = new TextField ();
    usernameField.setPlaceholder ("Username");
    usernameField.setStyleName ("usernameField");
    addComponent (usernameField, "usernameField");

    PasswordField passwordField = new PasswordField ();
    passwordField.setPlaceholder ("Password");
    passwordField.setStyleName ("passwordField");
    addComponent (passwordField, "passwordField");

    Button nextButton = new Button ("Use my identity");
    nextButton.addStyleName (ValoTheme.BUTTON_BORDERLESS);
    nextButton.addStyleName (" elonia");
    nextButton.setWidth ("200px");
    addComponent (nextButton, "nextButton");
    nextButton.addClickListener(new Button.ClickListener() {
      public void buttonClick(Button.ClickEvent event) {
        nextButton.setCaption ("clicked");
        view.setCurrentPage (new ConfirmPersonalDetailsPage (view));
      }
    });
  }
}