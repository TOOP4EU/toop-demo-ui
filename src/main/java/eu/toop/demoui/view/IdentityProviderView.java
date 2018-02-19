package eu.toop.demoui.view;

import com.vaadin.navigator.View;
import com.vaadin.ui.*;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class IdentityProviderView extends VerticalLayout implements View {
  public IdentityProviderView() {
    addComponent(new Label("Login"));

    TextField usernameField = new TextField();
    TextField passwordField = new TextField();
    CheckBoxGroup<String> checkboxGroup = new CheckBoxGroup<>();

    usernameField.setPlaceholder("Username");
    passwordField.setPlaceholder("Password");

    usernameField.setCaption("Username:");
    passwordField.setCaption("Password:");

    checkboxGroup.setItems("IP address for subject confirmation data");

    addComponent(usernameField);
    addComponent(passwordField);
    addComponent(checkboxGroup);

    Button nextButton = new Button("Use my identity", clickEvent -> {
      UI.getCurrent().getNavigator().navigateTo(IdentityProviderConfirmView.class.getName());
    });
    addComponent(nextButton);
  }
}
