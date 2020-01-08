/**
 * Copyright (C) 2018-2020 toop.eu
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

import eu.toop.demoui.bean.ToopDataBean;
import eu.toop.demoui.view.BaseView;

public class ManualDataEntry extends CustomLayout {

  private final BaseView view;

  public ManualDataEntry (final BaseView view) {

    super ("ManualDataEntry");
    this.view = view;

    setHeight ("100%");

    final IdentityForm identityForm = new IdentityForm (view.getIdentity (), true);
    addComponent (identityForm, "identityForm");

    final Button manualDataEntryButton = new Button ("Enter company info");
    manualDataEntryButton.addStyleName (ValoTheme.BUTTON_BORDERLESS);
    manualDataEntryButton.addStyleName (" freedonia");
    addComponent (manualDataEntryButton, "manualDataEntryButton");

    manualDataEntryButton.addClickListener (event -> {
      view.setToopDataBean (new ToopDataBean ());
      manualDataEntryButton.setEnabled (false);
      addMainCompanyForm ();
    });
  }

  public void addMainCompanyForm () {

    final MainCompanyForm mainCompanyForm = new MainCompanyForm (view.getToopDataBean (), false);

    final BaseForm baseForm = new BaseForm (mainCompanyForm, "Enter company details");
    addComponent (baseForm, "mainCompanyForm");
    view.setMainCompanyForm (mainCompanyForm);

    final Button nextButton = new Button ("I confirm this information is correct");
    nextButton.addStyleName (ValoTheme.BUTTON_BORDERLESS);
    nextButton.addStyleName (" freedonia");
    addComponent (nextButton, "nextButton");
    nextButton.addClickListener (event -> {
      mainCompanyForm.save ();
      //view.setCurrentPage (new RegisterWithWEEENewDetailsPage (view));
      view.setCurrentPage (new SuccessPage (view));
    });
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
