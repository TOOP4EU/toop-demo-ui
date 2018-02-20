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

public class StartView extends VerticalLayout implements View {
    public StartView() {
        VerticalLayout headerLayout = new VerticalLayout();
        VerticalLayout mainLayout = new VerticalLayout();
        VerticalLayout footerLayout = new VerticalLayout();

        addComponent(headerLayout);
        addComponent(mainLayout);
        addComponent(footerLayout);

        headerLayout.addComponent(new Label("About | Contact | Help"));

        mainLayout.addComponent(new Label("Welcome to Freedonia's"));
        mainLayout.addComponent(new Label("Online public services portal"));

        HorizontalLayout processChoiceLayout = new HorizontalLayout();
        mainLayout.addComponent(processChoiceLayout);

        Button licenseAndPermissionsButton = new Button("License and permissions");
        Button companyDataMandatesButton = new Button("Company data mandates");
        Button registerABranchButton = new Button("Register a branch", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                UI.getCurrent().getNavigator().navigateTo(eIDModuleView.class.getName());
            }
        });

        licenseAndPermissionsButton.setEnabled(false);
        companyDataMandatesButton.setEnabled(false);

        processChoiceLayout.addComponent(licenseAndPermissionsButton);
        processChoiceLayout.addComponent(companyDataMandatesButton);
        processChoiceLayout.addComponent(registerABranchButton);
    }
}
