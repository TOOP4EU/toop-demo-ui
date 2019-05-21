/**
 * Copyright (C) 2018-2019 toop.eu
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
package eu.toop.demoui.layouts;

import com.vaadin.ui.Button;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.themes.ValoTheme;

import eu.toop.demoui.view.BaseView;

public class ReviewPage extends CustomLayout {
  public ReviewPage (final BaseView view) {

    super ("ReviewPage");

    setHeight ("100%");

    final IdentityForm identityForm = new IdentityForm (view.getIdentity (), true);
    addComponent (identityForm, "identityForm");

    final NewCompanyForm newCompanyForm = new NewCompanyForm (view.getNewCompany (), true);
    addComponent (newCompanyForm, "newCompanyForm");

    final MainCompanyForm mainCompanyForm = new MainCompanyForm (view.getToopDataBean (), true);
    addComponent (mainCompanyForm, "mainCompanyForm");

    final Button nextButton = new Button ("Proceed");
    nextButton.addStyleName (ValoTheme.BUTTON_BORDERLESS);
    nextButton.addStyleName (" freedonia");
    addComponent (nextButton, "nextButton");
    nextButton.addClickListener (event -> view.setCurrentPage (new SuccessPage (view)));
  }
}