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
package bean;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import eu.toop.demoui.bean.NewCompany;

public final class NewCompanyTest {
  @Test
  public void testBasic () {

    final String exemptions = "Test exemptions";
    final String hazardousMaterials = "Test hazardous materials";
    final String producerComplianceScheme = "Test producer compliance scheme";
    final String wasteDisposalProcess = "Test waste disposal process";

    final NewCompany newCompany = new NewCompany ();
    newCompany.setExemptions (exemptions);
    newCompany.setHazardousMaterials (hazardousMaterials);
    newCompany.setProducerComplianceScheme (producerComplianceScheme);
    newCompany.setWasteDisposalProcess (wasteDisposalProcess);

    assertEquals (exemptions, newCompany.getExemptions ());
    assertEquals (hazardousMaterials, newCompany.getHazardousMaterials ());
    assertEquals (producerComplianceScheme, newCompany.getProducerComplianceScheme ());
    assertEquals (wasteDisposalProcess, newCompany.getWasteDisposalProcess ());
  }
}
