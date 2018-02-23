/**
 * Copyright (C) 2018 toop.eu
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
