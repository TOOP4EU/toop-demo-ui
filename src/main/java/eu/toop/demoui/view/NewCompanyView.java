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

public class NewCompanyView extends VerticalLayout implements View {
  public NewCompanyView() {
    addComponent(new Label("Register a new branch in Freedonia"));
    addComponent(new Label("Add the new branch details to register with Freedonia"));

    TextField companyTradeNameField = new TextField();
    TextField undertakingIdentification = new TextField();
    TextField companyType = new TextField();
    TextField legalForm = new TextField();

    companyTradeNameField.setCaption("Company trade field name:");
    undertakingIdentification.setCaption("Undertaking identification:");
    companyType.setCaption("Company type:");
    legalForm.setCaption("Legal form:");

    addComponent(companyTradeNameField);
    addComponent(undertakingIdentification);
    addComponent(companyType);
    addComponent(legalForm);

    Button nextButton2 = new Button("Register your new branch in Freedonia!", clickEvent -> {
      UI.getCurrent().getNavigator().navigateTo(FinalReviewView.class.getName());
    });
    addComponent(nextButton2);
  }
}
