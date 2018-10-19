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

import eu.toop.demoui.bean.MainCompany;

public class MainCompanyForm extends FormLayout {

  private static final Logger logger = LoggerFactory.getLogger (MainCompanyForm.class);

  private final Binder<MainCompany> binder = new Binder<> ();
  private MainCompany mainCompany;

  public MainCompanyForm (final MainCompany mainCompany, final boolean readOnly) {

    final TextField addressField = new TextField ("Address");
    final TextField ssNumberField = new TextField ("SS number");
    final TextField businessCodeField = new TextField ("Company code");
    final TextField vatNumberField = new TextField ("VAT number");
    final TextField companyTypeField = new TextField ("Company type");
    final TextField legalStatusField = new TextField ("Legal status");
    final TextField legalStatusEffectiveDateField = new TextField ("Legal status effective date");
    final TextField registrationDateField = new TextField ("Registration date");
    final TextField registrationNumberField = new TextField ("Registration number");
    final TextField companyNameField = new TextField ("Company name");
    final TextField companyNaceCodeField = new TextField ("Company nace code");
    final TextField activityDeclarationField = new TextField ("Activity declaration");
    final TextField registrationAuthorityField = new TextField ("Registration authority");

    binder.bind (addressField, MainCompany::getAddress, MainCompany::setAddress);
    binder.bind (ssNumberField, MainCompany::getSSNumber, MainCompany::setSSNumber);
    binder.bind (businessCodeField, MainCompany::getBusinessCode, MainCompany::setBusinessCode);
    binder.bind (vatNumberField, MainCompany::getVATNumber, MainCompany::setVATNumber);
    binder.bind (companyTypeField, MainCompany::getCompanyType, MainCompany::setCompanyType);
    binder.bind (legalStatusField, MainCompany::getLegalStatus, MainCompany::setLegalStatus);
    binder.bind (legalStatusEffectiveDateField, MainCompany::getLegalStatusEffectiveDate, MainCompany::setLegalStatusEffectiveDate);
    binder.bind (registrationDateField, MainCompany::getRegistrationDate, MainCompany::setRegistrationDate);
    binder.bind (registrationNumberField, MainCompany::getRegistrationNumber, MainCompany::setRegistrationNumber);
    binder.bind (companyNameField, MainCompany::getCompanyName, MainCompany::setCompanyName);
    binder.bind (companyNaceCodeField, MainCompany::getCompanyNaceCode, MainCompany::setCompanyNaceCode);
    binder.bind (activityDeclarationField, MainCompany::getActivityDeclaration, MainCompany::setActivityDeclaration);
    binder.bind (registrationAuthorityField, MainCompany::getRegistrationAuthority, MainCompany::setRegistrationAuthority);

    addressField.setReadOnly (readOnly);
    ssNumberField.setReadOnly (readOnly);
    businessCodeField.setReadOnly (readOnly);
    vatNumberField.setReadOnly (readOnly);
    companyTypeField.setReadOnly (readOnly);
    legalStatusField.setReadOnly (readOnly);
    legalStatusEffectiveDateField.setReadOnly (readOnly);
    registrationDateField.setReadOnly (readOnly);
    registrationNumberField.setReadOnly (readOnly);
    companyNameField.setReadOnly (readOnly);
    companyNaceCodeField.setReadOnly (readOnly);
    activityDeclarationField.setReadOnly (readOnly);
    registrationAuthorityField.setReadOnly (readOnly);

    addComponent (addressField);
    addComponent (ssNumberField);
    addComponent (businessCodeField);
    addComponent (vatNumberField);
    addComponent (companyTypeField);
    addComponent (legalStatusField);
    addComponent (legalStatusEffectiveDateField);
    addComponent (registrationDateField);
    addComponent (registrationNumberField);
    addComponent (companyNameField);
    addComponent (companyNaceCodeField);
    addComponent (activityDeclarationField);
    addComponent (registrationAuthorityField);

    setOrganizationBean (mainCompany);
  }

  public void setOrganizationBean (final MainCompany mainCompany) {

    this.mainCompany = mainCompany;
    binder.readBean (this.mainCompany);
  }

  public void save () {

    try {
      binder.writeBean (mainCompany);
    } catch (final ValidationException e) {
      logger.error ("Failed to write to 'mainCompany' bean");
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