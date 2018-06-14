package bean;

import eu.toop.demoui.bean.NewCompany;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NewCompanyTest {
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