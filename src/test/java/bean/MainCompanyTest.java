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
package bean;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import eu.toop.demoui.bean.MainCompany;

public class MainCompanyTest {
  @Test
  public void testBasic () {

    final String activityDeclaration = "Manufacture of other electrical equipment";
    final String address = "Gamlavegen 234, 321 44, Velma, Elonia";
    final String businessCode = "JF 234556-6213";
    final String companyNaceCode = "C27.9";
    final String companyName = "Zizi mat";
    final String companyType = "Limited";
    final String legalStatus = "Active";
    final String legalStatusEffectiveDate = "2012-01-12";
    final String registrationAuthority = "Elonia Tax Agency";
    final String registrationDate = "2012-01-12";
    final String registrationNumber = "009987 665543";
    final String ssNumber = "PKL 0987 6548";
    final String vatNumber = "09897656";

    final MainCompany mainCompany = new MainCompany ();
    mainCompany.setActivityDeclaration (activityDeclaration);
    mainCompany.setAddress (address);
    mainCompany.setBusinessCode (businessCode);
    mainCompany.setCompanyNaceCode (companyNaceCode);
    mainCompany.setCompanyName (companyName);
    mainCompany.setCompanyType (companyType);
    mainCompany.setLegalStatus (legalStatus);
    mainCompany.setLegalStatusEffectiveDate (legalStatusEffectiveDate);
    mainCompany.setRegistrationAuthority (registrationAuthority);
    mainCompany.setRegistrationDate (registrationDate);
    mainCompany.setRegistrationNumber (registrationNumber);
    mainCompany.setSSNumber (ssNumber);
    mainCompany.setVATNumber (vatNumber);

    assertEquals (activityDeclaration, mainCompany.getActivityDeclaration ());
    assertEquals (address, mainCompany.getAddress ());
    assertEquals (businessCode, mainCompany.getBusinessCode ());
    assertEquals (companyNaceCode, mainCompany.getCompanyNaceCode ());
    assertEquals (companyName, mainCompany.getCompanyName ());
    assertEquals (companyType, mainCompany.getCompanyType ());
    assertEquals (legalStatus, mainCompany.getLegalStatus ());
    assertEquals (legalStatusEffectiveDate, mainCompany.getLegalStatusEffectiveDate ());
    assertEquals (registrationAuthority, mainCompany.getRegistrationAuthority ());
    assertEquals (registrationDate, mainCompany.getRegistrationDate ());
    assertEquals (registrationNumber, mainCompany.getRegistrationNumber ());
    assertEquals (ssNumber, mainCompany.getSSNumber ());
    assertEquals (vatNumber, mainCompany.getVATNumber ());
  }
}
