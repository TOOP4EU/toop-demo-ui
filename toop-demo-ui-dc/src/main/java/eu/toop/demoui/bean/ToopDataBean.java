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
package eu.toop.demoui.bean;

import java.io.Serializable;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

import com.helger.commons.collection.impl.ICommonsList;

import eu.toop.commons.exchange.AsicReadEntry;

public class ToopDataBean implements Serializable {

  private String address;
  private String ssNumber;
  private String businessCode;
  private String vatNumber;
  private String companyType;
  private String legalStatus;
  private String legalStatusEffectiveDate;
  private String registrationDate;
  private String registrationNumber;
  private String companyName;
  private String companyNaceCode;
  private String activityDeclaration;
  private String registrationAuthority;
  private ICommonsList<AsicReadEntry> attachments = null;
  private List<AbstractMap.SimpleEntry<String, String>> keyValList = new ArrayList<> ();

  public ToopDataBean () {

  }

  public ToopDataBean (final ICommonsList<AsicReadEntry> attachments) {
    this.attachments = attachments;
  }

  public String getAddress () {

    return address;
  }

  public void setAddress (String address) {

    this.address = address;
  }

  public String getSSNumber () {

    return ssNumber;
  }

  public void setSSNumber (String ssNumber) {

    this.ssNumber = ssNumber;
  }

  public String getBusinessCode () {

    return businessCode;
  }

  public void setBusinessCode (String businessCode) {

    this.businessCode = businessCode;
  }

  public String getVATNumber () {

    return vatNumber;
  }

  public void setVATNumber (String vatNumber) {

    this.vatNumber = vatNumber;
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

  public ICommonsList<AsicReadEntry> getAttachments () {
    return attachments;
  }

  public List<AbstractMap.SimpleEntry<String, String>> getKeyValList () {
    return keyValList;
  }

  public void setKeyValList (List<AbstractMap.SimpleEntry<String, String>> keyValList) {
    this.keyValList = keyValList;
  }
}
