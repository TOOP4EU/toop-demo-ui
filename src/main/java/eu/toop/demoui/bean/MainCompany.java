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
package eu.toop.demoui.bean;

import java.io.Serializable;

public class MainCompany implements Serializable {
  private String company;
  private String undertakingIdentification;
  private String registrationNumber;
  private String companyCode;
  private String companyDesignation;
  private String companyName;
  private String legalForm;
  private String companyType;
  private String companyStatus;
  private String legalStatus;
  private String addressData;
  private String telephoneNumber;
  private String emailAddress;

  public String getCompany () {
    return company;
  }

  public void setCompany (String company) {
    this.company = company;
  }

  public String getUndertakingIdentification () {
    return undertakingIdentification;
  }

  public void setUndertakingIdentification (String undertakingIdentification) {
    this.undertakingIdentification = undertakingIdentification;
  }

  public String getRegistrationNumber () {
    return registrationNumber;
  }

  public void setRegistrationNumber (String registrationNumber) {
    this.registrationNumber = registrationNumber;
  }

  public String getCompanyCode () {
    return companyCode;
  }

  public void setCompanyCode (String companyCode) {
    this.companyCode = companyCode;
  }

  public String getCompanyDesignation () {
    return companyDesignation;
  }

  public void setCompanyDesignation (String companyDesignation) {
    this.companyDesignation = companyDesignation;
  }

  public String getCompanyName () {
    return companyName;
  }

  public void setCompanyName (String companyName) {
    this.companyName = companyName;
  }

  public String getLegalForm () {
    return legalForm;
  }

  public void setLegalForm (String legalForm) {
    this.legalForm = legalForm;
  }

  public String getCompanyType () {
    return companyType;
  }

  public void setCompanyType (String companyType) {
    this.companyType = companyType;
  }

  public String getCompanyStatus () {
    return companyStatus;
  }

  public void setCompanyStatus (String companyStatus) {
    this.companyStatus = companyStatus;
  }

  public String getLegalStatus () {
    return legalStatus;
  }

  public void setLegalStatus (String legalStatus) {
    this.legalStatus = legalStatus;
  }

  public String getAddressData () {
    return addressData;
  }

  public void setAddressData (String addressData) {
    this.addressData = addressData;
  }

  public String getTelephoneNumber () {
    return telephoneNumber;
  }

  public void setTelephoneNumber (String telephoneNumber) {
    this.telephoneNumber = telephoneNumber;
  }

  public String getEmailAddress () {
    return emailAddress;
  }

  public void setEmailAddress (String emailAddress) {
    this.emailAddress = emailAddress;
  }
}
