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
package eu.toop.demoui.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.helger.commons.collection.impl.ICommonsList;

import eu.toop.commons.exchange.AsicReadEntry;

public class ToopDataBean implements Serializable
{
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
  private ICommonsList <AsicReadEntry> attachments = null;
  private List <Map.Entry <String, String>> keyValList = new ArrayList <> ();

  public ToopDataBean ()
  {}

  public ToopDataBean (final ICommonsList <AsicReadEntry> attachments)
  {
    this.attachments = attachments;
  }

  public String getAddress ()
  {
    return address;
  }

  public void setAddress (final String address)
  {
    this.address = address;
  }

  public String getSSNumber ()
  {
    return ssNumber;
  }

  public void setSSNumber (final String ssNumber)
  {
    this.ssNumber = ssNumber;
  }

  public String getBusinessCode ()
  {
    return businessCode;
  }

  public void setBusinessCode (final String businessCode)
  {
    this.businessCode = businessCode;
  }

  public String getVATNumber ()
  {
    return vatNumber;
  }

  public void setVATNumber (final String vatNumber)
  {
    this.vatNumber = vatNumber;
  }

  public String getCompanyType ()
  {
    return companyType;
  }

  public void setCompanyType (final String companyType)
  {
    this.companyType = companyType;
  }

  public String getLegalStatus ()
  {
    return legalStatus;
  }

  public void setLegalStatus (final String legalStatus)
  {
    this.legalStatus = legalStatus;
  }

  public String getLegalStatusEffectiveDate ()
  {
    return legalStatusEffectiveDate;
  }

  public void setLegalStatusEffectiveDate (final String legalStatusEffectiveDate)
  {
    this.legalStatusEffectiveDate = legalStatusEffectiveDate;
  }

  public String getRegistrationDate ()
  {
    return registrationDate;
  }

  public void setRegistrationDate (final String registrationDate)
  {
    this.registrationDate = registrationDate;
  }

  public String getRegistrationNumber ()
  {
    return registrationNumber;
  }

  public void setRegistrationNumber (final String registrationNumber)
  {
    this.registrationNumber = registrationNumber;
  }

  public String getCompanyName ()
  {
    return companyName;
  }

  public void setCompanyName (final String companyName)
  {
    this.companyName = companyName;
  }

  public String getCompanyNaceCode ()
  {
    return companyNaceCode;
  }

  public void setCompanyNaceCode (final String companyNaceCode)
  {
    this.companyNaceCode = companyNaceCode;
  }

  public String getActivityDeclaration ()
  {
    return activityDeclaration;
  }

  public void setActivityDeclaration (final String activityDeclaration)
  {
    this.activityDeclaration = activityDeclaration;
  }

  public String getRegistrationAuthority ()
  {
    return registrationAuthority;
  }

  public void setRegistrationAuthority (final String registrationAuthority)
  {
    this.registrationAuthority = registrationAuthority;
  }

  public ICommonsList <AsicReadEntry> getAttachments ()
  {
    return attachments;
  }

  public List <Map.Entry <String, String>> getKeyValList ()
  {
    return keyValList;
  }

  public void setKeyValList (final List <Map.Entry <String, String>> keyValList)
  {
    this.keyValList = keyValList;
  }
}
