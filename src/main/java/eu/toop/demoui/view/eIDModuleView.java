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

import java.util.ArrayList;
import java.util.List;

public class eIDModuleView extends VerticalLayout implements View {

  public eIDModuleView() {
    // List of planets
    List<String> choices = new ArrayList<>();
    choices.add("Elonia");

    ComboBox<String> countryComboBox = new ComboBox<>("Select eID Coutnry");
    countryComboBox.setItems(choices);
    addComponent(countryComboBox);

    // Use the name property for item captions
    countryComboBox.setItemCaptionGenerator(String::toString);

    Button nextButton = new Button("Go to IdentityProviderView", clickEvent -> {
      UI.getCurrent().getNavigator().navigateTo(IdentityProviderView.class.getName());
    });
    addComponent(nextButton);
  }
}
