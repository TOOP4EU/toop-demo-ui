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
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import eu.toop.demoui.bean.Organization;
import eu.toop.demoui.form.OrganizationForm;

public class MainCompanyView extends VerticalLayout implements View {

  private Organization organization;
  private OrganizationForm organizationForm;

  public MainCompanyView() {
    organization = new Organization();
    organizationForm = new OrganizationForm(organization, event -> {});
    addComponent(organizationForm);

    Button nextButton2 = new Button("Go to NewCompanyView", clickEvent -> {
      UI.getCurrent().getNavigator().navigateTo(NewCompanyView.class.getName());
    });
    addComponent(nextButton2);
  }

  public Organization getOrganization() {
    return organization;
  }

  public OrganizationForm getOrganizationForm() {
    return organizationForm;
  }
}
