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
package eu.toop.demoui.form;

import java.io.File;
import java.io.IOException;

import com.helger.asic.SignatureHelper;
import com.helger.commons.collection.impl.CommonsArrayList;
import com.helger.commons.io.file.FileHelper;
import com.helger.commons.io.stream.NonBlockingByteArrayOutputStream;
import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;

import eu.toop.commons.doctype.EToopDocumentType;
import eu.toop.commons.doctype.EToopProcess;
import eu.toop.commons.exchange.RequestValue;
import eu.toop.commons.exchange.message.ToopMessageBuilder;
import eu.toop.commons.exchange.mock.MSDataRequest;
import eu.toop.demoui.bean.MainCompany;
import eu.toop.iface.mockup.client.HttpClientInvoker;
//import eu.toop.iface.mockup.client.HttpClientInvoker;

public class MainCompanyForm extends FormLayout {
  private final Binder<MainCompany> binder = new Binder<> ();

  private MainCompany mainCompany;

  public MainCompanyForm (final MainCompany mainCompany, boolean readOnly, final Button.ClickListener onSubmit) {

    final TextField companyField = new TextField ("Company");
    final TextField undertakingIdentificationField = new TextField ("Undertaking identification");
    final TextField registrationNumberField = new TextField ("Registration number");
    final TextField companyCodeField = new TextField ("Company code");
    final TextField companyDesignationField = new TextField ("Company designation");
    final TextField companyNameField = new TextField ("Company name");
    final TextField legalFormField = new TextField ("Legal form");
    final TextField companyTypeField = new TextField ("Company type");
    final TextField companyStatusField = new TextField ("Company status");
    final TextField legalStatusField = new TextField ("Legal status");
    final TextField addressDataField = new TextField ("Address data");
    final TextField telephoneNumberField = new TextField ("Telephone number");
    final TextField emailAddressField = new TextField ("Email address");

    binder.bind (companyField, MainCompany::getCompany, MainCompany::setCompany);
    binder.bind (undertakingIdentificationField, MainCompany::getUndertakingIdentification, MainCompany::setUndertakingIdentification);
    binder.bind (registrationNumberField, MainCompany::getRegistrationNumber, MainCompany::setRegistrationNumber);
    binder.bind (companyCodeField, MainCompany::getCompanyCode, MainCompany::setCompanyCode);
    binder.bind (companyDesignationField, MainCompany::getCompanyDesignation, MainCompany::setCompanyDesignation);
    binder.bind (companyNameField, MainCompany::getCompanyName, MainCompany::setCompanyName);
    binder.bind (legalFormField, MainCompany::getLegalForm, MainCompany::setLegalForm);
    binder.bind (companyTypeField, MainCompany::getCompanyType, MainCompany::setCompanyType);
    binder.bind (companyStatusField, MainCompany::getCompanyStatus, MainCompany::setCompanyStatus);
    binder.bind (legalStatusField, MainCompany::getLegalStatus, MainCompany::setLegalStatus);
    binder.bind (addressDataField, MainCompany::getAddressData, MainCompany::setAddressData);
    binder.bind (telephoneNumberField, MainCompany::getTelephoneNumber, MainCompany::setTelephoneNumber);
    binder.bind (emailAddressField, MainCompany::getEmailAddress, MainCompany::setEmailAddress);

    companyField.setReadOnly (readOnly);
    undertakingIdentificationField.setReadOnly (readOnly);
    registrationNumberField.setReadOnly (readOnly);
    companyCodeField.setReadOnly (readOnly);
    companyDesignationField.setReadOnly (readOnly);
    companyNameField.setReadOnly (readOnly);
    legalFormField.setReadOnly (readOnly);
    companyTypeField.setReadOnly (readOnly);
    companyStatusField.setReadOnly (readOnly);
    legalStatusField.setReadOnly (readOnly);
    addressDataField.setReadOnly (readOnly);
    telephoneNumberField.setReadOnly (readOnly);
    emailAddressField.setReadOnly (readOnly);

    addComponent (companyField);
    addComponent (undertakingIdentificationField);
    addComponent (registrationNumberField);
    addComponent (companyCodeField);
    addComponent (companyDesignationField);
    addComponent (companyNameField);
    addComponent (legalFormField);
    addComponent (companyTypeField);
    addComponent (companyStatusField);
    addComponent (legalStatusField);
    addComponent (addressDataField);
    addComponent (telephoneNumberField);
    addComponent (emailAddressField);

    setOrganizationBean (mainCompany);
  }

  public void setOrganizationBean (final MainCompany _mainCompany) {
    mainCompany = _mainCompany;
    binder.readBean (mainCompany);
  }

  public void save() {
    try {
      binder.writeBean (mainCompany);
    } catch (ValidationException e) {
      e.printStackTrace ();
    }
  }
}