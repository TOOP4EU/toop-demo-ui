package eu.toop.demoui.pages;

import com.vaadin.ui.Button;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import eu.toop.demoui.bean.Identity;
import eu.toop.demoui.form.IdentityForm;
import eu.toop.demoui.view.HomeView;

import java.time.LocalDate;

public class ConfirmPersonalDetailsPage extends CustomLayout {
  public ConfirmPersonalDetailsPage (HomeView view) {
    super ("ConfirmPersonalDetailsPage");

    setHeight ("100%");

    Identity identity = new Identity ();
    identity.setFirstName ("Jan");
    identity.setFamilyName ("Doe");
    identity.setBirthPlace("Fridili");
    identity.setBirthDate (LocalDate.parse("1986-02-01"));
    identity.setIdentifier ("EL/EL/12345");
    identity.setNationality ("EL");
    view.setIdentity (identity);

    IdentityForm identityForm = new IdentityForm (view.getIdentity (), true, clickEvent -> {});
    addComponent (identityForm, "identityForm");

    Button nextButton = new Button ("Next");
    addComponent (nextButton, "nextButton");
    nextButton.addClickListener(new Button.ClickListener() {
      public void buttonClick(Button.ClickEvent event) {
        nextButton.setCaption ("clicked");
        //view.setCurrentPage (new BusinessServicesPage (view));
      }
    });
  }
}