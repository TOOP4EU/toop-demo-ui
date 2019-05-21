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

import static org.junit.Assert.assertEquals;

import eu.toop.demoui.bean.ToopDataBean;
import org.junit.Test;

public class ToopDataBeanTest {
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

    final ToopDataBean toopDataBean = new ToopDataBean();
    toopDataBean.setActivityDeclaration (activityDeclaration);
    toopDataBean.setAddress (address);
    toopDataBean.setBusinessCode (businessCode);
    toopDataBean.setCompanyNaceCode (companyNaceCode);
    toopDataBean.setCompanyName (companyName);
    toopDataBean.setCompanyType (companyType);
    toopDataBean.setLegalStatus (legalStatus);
    toopDataBean.setLegalStatusEffectiveDate (legalStatusEffectiveDate);
    toopDataBean.setRegistrationAuthority (registrationAuthority);
    toopDataBean.setRegistrationDate (registrationDate);
    toopDataBean.setRegistrationNumber (registrationNumber);
    toopDataBean.setSSNumber (ssNumber);
    toopDataBean.setVATNumber (vatNumber);

    assertEquals (activityDeclaration, toopDataBean.getActivityDeclaration ());
    assertEquals (address, toopDataBean.getAddress ());
    assertEquals (businessCode, toopDataBean.getBusinessCode ());
    assertEquals (companyNaceCode, toopDataBean.getCompanyNaceCode ());
    assertEquals (companyName, toopDataBean.getCompanyName ());
    assertEquals (companyType, toopDataBean.getCompanyType ());
    assertEquals (legalStatus, toopDataBean.getLegalStatus ());
    assertEquals (legalStatusEffectiveDate, toopDataBean.getLegalStatusEffectiveDate ());
    assertEquals (registrationAuthority, toopDataBean.getRegistrationAuthority ());
    assertEquals (registrationDate, toopDataBean.getRegistrationDate ());
    assertEquals (registrationNumber, toopDataBean.getRegistrationNumber ());
    assertEquals (ssNumber, toopDataBean.getSSNumber ());
    assertEquals (vatNumber, toopDataBean.getVATNumber ());
  }
}
