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

import java.io.ByteArrayInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;

import eu.toop.commons.exchange.AsicReadEntry;
import eu.toop.demoui.bean.ToopDataBean;

public class MainCompanyForm extends FormLayout {

  private static final Logger logger = LoggerFactory.getLogger (MainCompanyForm.class);

  private final Binder<ToopDataBean> binder = new Binder<> ();
  private ToopDataBean toopDataBean;

  public MainCompanyForm (final ToopDataBean toopDataBean, final boolean readOnly) {

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

    binder.bind (addressField, ToopDataBean::getAddress, ToopDataBean::setAddress);
    binder.bind (ssNumberField, ToopDataBean::getSSNumber, ToopDataBean::setSSNumber);
    binder.bind (businessCodeField, ToopDataBean::getBusinessCode, ToopDataBean::setBusinessCode);
    binder.bind (vatNumberField, ToopDataBean::getVATNumber, ToopDataBean::setVATNumber);
    binder.bind (companyTypeField, ToopDataBean::getCompanyType, ToopDataBean::setCompanyType);
    binder.bind (legalStatusField, ToopDataBean::getLegalStatus, ToopDataBean::setLegalStatus);
    binder.bind (legalStatusEffectiveDateField, ToopDataBean::getLegalStatusEffectiveDate, ToopDataBean::setLegalStatusEffectiveDate);
    binder.bind (registrationDateField, ToopDataBean::getRegistrationDate, ToopDataBean::setRegistrationDate);
    binder.bind (registrationNumberField, ToopDataBean::getRegistrationNumber, ToopDataBean::setRegistrationNumber);
    binder.bind (companyNameField, ToopDataBean::getCompanyName, ToopDataBean::setCompanyName);
    binder.bind (companyNaceCodeField, ToopDataBean::getCompanyNaceCode, ToopDataBean::setCompanyNaceCode);
    binder.bind (activityDeclarationField, ToopDataBean::getActivityDeclaration, ToopDataBean::setActivityDeclaration);
    binder.bind (registrationAuthorityField, ToopDataBean::getRegistrationAuthority, ToopDataBean::setRegistrationAuthority);

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

    setToopDataBean(toopDataBean);

    if (toopDataBean.getAttachments() != null && toopDataBean.getAttachments().size() > 0) {
      for (AsicReadEntry attachment : toopDataBean.getAttachments()) {

        Button downloadButton = new Button("Download " + attachment.getEntryName());

        StreamResource myResource = new StreamResource((StreamResource.StreamSource) () ->
                new ByteArrayInputStream(attachment.payload()), attachment.getEntryName());
        FileDownloader fileDownloader = new FileDownloader(myResource);
        fileDownloader.extend(downloadButton);

        addComponent(downloadButton);
      }
    }
  }

  public void setToopDataBean(final ToopDataBean toopDataBean) {

    this.toopDataBean = toopDataBean;
    binder.readBean (this.toopDataBean);
  }

  public void save () {

    try {
      binder.writeBean (toopDataBean);
    } catch (final ValidationException e) {
      logger.error ("Failed to write to 'toopDataBean' bean");
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