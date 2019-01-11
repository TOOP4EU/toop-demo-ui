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

public class NewCompany implements Serializable {
  private String wasteDisposalProcess;
  private String hazardousMaterials;
  private String exemptions;
  private String producerComplianceScheme;

  public String getWasteDisposalProcess () {

    return wasteDisposalProcess;
  }

  public void setWasteDisposalProcess (String wasteDisposalProcess) {

    this.wasteDisposalProcess = wasteDisposalProcess;
  }

  public String getHazardousMaterials () {

    return hazardousMaterials;
  }

  public void setHazardousMaterials (String hazardousMaterials) {

    this.hazardousMaterials = hazardousMaterials;
  }

  public String getExemptions () {

    return exemptions;
  }

  public void setExemptions (String exemptions) {

    this.exemptions = exemptions;
  }

  public String getProducerComplianceScheme () {

    return producerComplianceScheme;
  }

  public void setProducerComplianceScheme (String producerComplianceScheme) {

    this.producerComplianceScheme = producerComplianceScheme;
  }
}
