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
package eu.toop.demoui.bean;

import java.io.Serializable;

public class MainCompany implements Serializable {

  private String address;
  private String SSNumber;
  private String businessCode;
  private String VATNumber;
  private String companyType;
  private String legalStatus;
  private String legalStatusEffectiveDate;
  private String registrationDate;
  private String registrationNumber;
  private String companyName;
  private String companyNaceCode;
  private String activityDeclaration;
  private String registrationAuthority;

  public String getAddress () {

    return address;
  }

  public void setAddress (String address) {

    this.address = address;
  }

  public String getSSNumber () {

    return SSNumber;
  }

  public void setSSNumber (String SSNumber) {

    this.SSNumber = SSNumber;
  }

  public String getBusinessCode () {

    return businessCode;
  }

  public void setBusinessCode (String businessCode) {

    this.businessCode = businessCode;
  }

  public String getVATNumber () {

    return VATNumber;
  }

  public void setVATNumber (String VATNumber) {

    this.VATNumber = VATNumber;
  }

  public String getCompanyType () {

    return companyType;
  }

  public void setCompanyType (String companyType) {

    this.companyType = companyType;
  }

  public String getLegalStatus () {

    return legalStatus;
  }

  public void setLegalStatus (String legalStatus) {

    this.legalStatus = legalStatus;
  }

  public String getLegalStatusEffectiveDate () {

    return legalStatusEffectiveDate;
  }

  public void setLegalStatusEffectiveDate (String legalStatusEffectiveDate) {

    this.legalStatusEffectiveDate = legalStatusEffectiveDate;
  }

  public String getRegistrationDate () {

    return registrationDate;
  }

  public void setRegistrationDate (String registrationDate) {

    this.registrationDate = registrationDate;
  }

  public String getRegistrationNumber () {

    return registrationNumber;
  }

  public void setRegistrationNumber (String registrationNumber) {

    this.registrationNumber = registrationNumber;
  }

  public String getCompanyName () {

    return companyName;
  }

  public void setCompanyName (String companyName) {

    this.companyName = companyName;
  }

  public String getCompanyNaceCode () {

    return companyNaceCode;
  }

  public void setCompanyNaceCode (String companyNaceCode) {

    this.companyNaceCode = companyNaceCode;
  }

  public String getActivityDeclaration () {

    return activityDeclaration;
  }

  public void setActivityDeclaration (String activityDeclaration) {

    this.activityDeclaration = activityDeclaration;
  }

  public String getRegistrationAuthority () {

    return registrationAuthority;
  }

  public void setRegistrationAuthority (String registrationAuthority) {

    this.registrationAuthority = registrationAuthority;
  }
}
