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
package eu.toop.demoui.layouts;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;

import eu.toop.demoui.bean.NewCompany;

public class NewCompanyForm extends FormLayout {

  private static final Logger logger = LoggerFactory.getLogger (NewCompanyForm.class);

  private final Binder<NewCompany> binder = new Binder<> ();
  private NewCompany newCompany;

  public NewCompanyForm (final NewCompany newCompany, boolean readOnly) {

    final TextField wasteDisposalProcess = new TextField ("Waste disposal process");
    final TextField hazardousMaterials = new TextField ("Hazardous materials");
    final TextField exemptions = new TextField ("Exemptions");
    final TextField producerComplianceScheme = new TextField ("Producer compliance scheme");

    binder.bind (wasteDisposalProcess, NewCompany::getWasteDisposalProcess, NewCompany::setWasteDisposalProcess);
    binder.bind (hazardousMaterials, NewCompany::getHazardousMaterials, NewCompany::setHazardousMaterials);
    binder.bind (exemptions, NewCompany::getExemptions, NewCompany::setExemptions);
    binder.bind (producerComplianceScheme, NewCompany::getProducerComplianceScheme, NewCompany::setProducerComplianceScheme);

    wasteDisposalProcess.setReadOnly (readOnly);
    hazardousMaterials.setReadOnly (readOnly);
    exemptions.setReadOnly (readOnly);
    producerComplianceScheme.setReadOnly (readOnly);

    addComponent (wasteDisposalProcess);
    addComponent (hazardousMaterials);
    addComponent (exemptions);
    addComponent (producerComplianceScheme);

    setNewCompanyBean (newCompany);
  }

  public void setNewCompanyBean (final NewCompany newCompany) {

    this.newCompany = newCompany;
    binder.readBean (this.newCompany);
  }

  public void save () {

    try {
      binder.writeBean (newCompany);
    } catch (ValidationException e) {
      logger.error ("Failed to write to 'newCompany' bean");
    }
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
