/**
 * Copyright (C) 2018 toop.eu
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.toop.demoui.layouts;

import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;

import eu.toop.demoui.bean.MainCompany;

public class MainCompanyForm extends FormLayout {

  private final Binder<MainCompany> binder = new Binder<> ();
  private MainCompany mainCompany;

  public MainCompanyForm (final MainCompany mainCompany, final boolean readOnly, final Button.ClickListener onSubmit) {

    final TextField addressField = new TextField ("Address:");
    final TextField SSNumberField = new TextField ("SS Number:");
    final TextField businessCodeField = new TextField ("Company Code:");
    final TextField VATNumberField = new TextField ("VAT Number:");
    final TextField companyTypeField = new TextField ("Company Type:");
    final TextField legalStatusField = new TextField ("Legal Status:");
    final TextField legalStatusEffectiveDateField = new TextField ("Legal Status Effective Date:");
    final TextField registrationDateField = new TextField ("Registration Date:");
    final TextField registrationNumberField = new TextField ("Registration Number:");
    final TextField companyNameField = new TextField ("Company Name:");
    final TextField companyNaceCodeField = new TextField ("Company Name Code:");
    final TextField activityDeclarationField = new TextField ("Activity Declaration:");
    final TextField registrationAuthorityField = new TextField ("Registration Authority:");

    binder.bind (addressField, MainCompany::getAddress, MainCompany::setAddress);
    binder.bind (SSNumberField, MainCompany::getSSNumber, MainCompany::setSSNumber);
    binder.bind (businessCodeField, MainCompany::getBusinessCode, MainCompany::setBusinessCode);
    binder.bind (VATNumberField, MainCompany::getVATNumber, MainCompany::setVATNumber);
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
    SSNumberField.setReadOnly (readOnly);
    businessCodeField.setReadOnly (readOnly);
    VATNumberField.setReadOnly (readOnly);
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
    addComponent (SSNumberField);
    addComponent (businessCodeField);
    addComponent (VATNumberField);
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

  public void setOrganizationBean (final MainCompany _mainCompany) {

    mainCompany = _mainCompany;
    binder.readBean (mainCompany);
  }

  public void save () {

    try {
      binder.writeBean (mainCompany);
    } catch (final ValidationException e) {
      e.printStackTrace ();
    }
  }
}