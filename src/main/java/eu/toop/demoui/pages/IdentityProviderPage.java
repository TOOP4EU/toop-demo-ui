package eu.toop.demoui.pages;

import com.vaadin.ui.Button;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import eu.toop.demoui.bean.Identity;
import eu.toop.demoui.components.Body;
import eu.toop.demoui.components.EloniaHeader;
import eu.toop.demoui.components.InfoIdP;
import eu.toop.demoui.view.StartView;

import java.time.LocalDate;

public class IdentityProviderPage extends BasePage {

  public IdentityProviderPage (StartView view) {
    super (view);

    setStyleName ("eIDModule");

    VerticalLayout main = new VerticalLayout ();
    setHeader (new EloniaHeader ());
    setBody (new Body (new InfoIdP (), main));

    main.setSizeUndefined ();

    TextField usernameField = new TextField ("Login");
    usernameField.setPlaceholder ("Username");
    main.addComponent (usernameField);

    TextField passwordField = new TextField ();
    passwordField.setPlaceholder ("Password");
    main.addComponent (passwordField);

    Button nextButton = new Button ("Use my identity");
    main.addComponent (nextButton);

    nextButton.addClickListener(new Button.ClickListener() {
      public void buttonClick(Button.ClickEvent event) {
        // Preset mockup data
        Identity identity = new Identity ();
        identity.setFirstName ("Jan");
        identity.setFamilyName ("Doe");
        identity.setBirthPlace("Fridili");
        identity.setBirthDate (LocalDate.parse("1986-02-01"));
        identity.setIdentifier ("EL/EL/12345");
        identity.setNationality ("EL");
        view.setIdentity (identity);

        view.setCurrentPage (new ConfirmDetailsPage (getView ()));
      }
    });
  }
}
