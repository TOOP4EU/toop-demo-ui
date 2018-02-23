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
