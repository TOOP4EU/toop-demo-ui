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
