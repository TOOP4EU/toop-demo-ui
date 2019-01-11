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
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.themes.ValoTheme;

import eu.toop.demoui.view.BaseView;

public class RegisterWithWEEEMainPage extends CustomLayout {

  private final BaseView view;
  private final ProgressBar spinner = new ProgressBar ();

  private static final String TOOP_BUTTON_STYLE = ValoTheme.BUTTON_BORDERLESS + " freedonia";

  public RegisterWithWEEEMainPage (final BaseView view) {

    super ("RegisterWithWEEEMainPage");
    this.view = view;

    setHeight ("100%");

    final IdentityForm identityForm = new IdentityForm (view.getIdentity (), true);
    addComponent (identityForm, "identityForm");

    final Button toopButton = new Button ("Get company info");
    toopButton.addStyleName (TOOP_BUTTON_STYLE);
    addComponent (toopButton, "toopButton");

    spinner.setStyleName ("spinner");
    spinner.setIndeterminate (true);
    spinner.setVisible (false);
    spinner.setCaption ("Please wait while your request for data is processed...");
    addComponent (spinner, "spinner");

    toopButton.addClickListener (event ->
      new ConfirmToopDataFetchingPage (view) {
        @Override
        public void onConsent () {
          // Show a loading icon while toop data is being retrieved.
          spinner.setVisible (true);
          toopButton.setEnabled (false);
        }

        @Override
        public void onSelfProvide () {
          toopButton.setEnabled (false);
          RegisterWithWEEEMainPage.this.view.setCurrentPage (new ManualDataEntry (RegisterWithWEEEMainPage.this.view));
        }
      }
    );
  }

  public void addMainCompanyForm () {

    spinner.setVisible (false);

    final MainCompanyForm mainCompanyForm = new MainCompanyForm (view.getMainCompany (), false);

    final BaseForm baseForm = new BaseForm (mainCompanyForm, "Preview of company details");
    addComponent (baseForm, "mainCompanyForm");
    view.setMainCompanyForm (mainCompanyForm);

    final Button nextButton = new Button ("I have previewed this information and want to proceed");
    nextButton.addStyleName (TOOP_BUTTON_STYLE);
    addComponent (nextButton, "nextButton");
    nextButton.addClickListener (event -> {

      mainCompanyForm.save ();
      //view.setCurrentPage (new RegisterWithWEEENewDetailsPage (view));
      view.setCurrentPage (new SuccessPage (view));
    });

    final Button gotoManualDataEntryButton = new Button("I do not wish this information to be used");
    gotoManualDataEntryButton.addStyleName (TOOP_BUTTON_STYLE);
    gotoManualDataEntryButton.addStyleName (" gotoManualDataEntryButton");
    addComponent (gotoManualDataEntryButton, "gotoManualDataEntryButton");
    gotoManualDataEntryButton.addClickListener (event -> view.setCurrentPage (new ManualDataEntry (view)));
  }

  @Override
  public boolean equals(Object obj) {
    return super.equals (obj);
  }

  @Override
  public int hashCode() {
    return super.hashCode ();
  }
}