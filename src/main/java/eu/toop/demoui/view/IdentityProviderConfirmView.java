package eu.toop.demoui.view;

import com.vaadin.navigator.View;
import com.vaadin.ui.*;
import eu.toop.demoui.bean.Identity;
import eu.toop.demoui.form.IdentityForm;

import java.time.LocalDate;

public class IdentityProviderConfirmView extends VerticalLayout implements View {
  public IdentityProviderConfirmView() {
    addComponent(new Label("Confirm your personal details"));
    addComponent(new Label("Your personal details"));

    Identity identity = new Identity();
    identity.setIdentifier("EL/EL/12345");
    identity.setFamilyName("Stern");
    identity.setFirstName("Maximillian");
    identity.setBirthDate(LocalDate.parse("1976-10-25"));
    IdentityForm identityForm = new IdentityForm(identity, null);

    addComponent(identityForm);

    Button nextButton = new Button("Go to MainCompanyView", clickEvent -> {
      UI.getCurrent().getNavigator().navigateTo(MainCompanyView.class.getName());
    });
    addComponent(nextButton);
  }
}
