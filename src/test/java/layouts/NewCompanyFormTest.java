package layouts;

import com.vaadin.ui.Component;
import com.vaadin.ui.TextField;
import eu.toop.demoui.bean.NewCompany;
import eu.toop.demoui.layouts.NewCompanyForm;
import org.junit.jupiter.api.Test;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NewCompanyFormTest {
  @Test
  public void testBasic () {

    final String exemptions = "Test exemptions";
    final String hazardousMaterials = "Test hazardous materials";
    final String producerComplianceScheme = "Test producer compliance scheme";
    final String wasteDisposalProcess = "Test waste disposal process";

    final String newExemptions = "New Test exemptions";
    final String newHazardousMaterials = "New Test hazardous materials";
    final String newProducerComplianceScheme = "New Test producer compliance scheme";
    final String newWasteDisposalProcess = "New Test waste disposal process";

    final NewCompany newCompany = new NewCompany ();
    newCompany.setExemptions (exemptions);
    newCompany.setHazardousMaterials (hazardousMaterials);
    newCompany.setProducerComplianceScheme (producerComplianceScheme);
    newCompany.setWasteDisposalProcess (wasteDisposalProcess);

    final NewCompanyForm newCompanyForm = new NewCompanyForm (newCompany, false);

    for (Iterator<Component> iterator = newCompanyForm.iterator(); iterator.hasNext();) {
      Component comp = (Component) iterator.next();

      if (comp instanceof TextField) {
        TextField textField = (TextField)comp;
        switch (textField.getCaption ()) {
          case "Exemptions":
            textField.setValue (newExemptions);
            break;
          case "Hazardous materials":
            textField.setValue (newHazardousMaterials);
            break;
          case "Producer compliance scheme":
            textField.setValue (newProducerComplianceScheme);
            break;
          case "Waste disposal process":
            textField.setValue (newWasteDisposalProcess);
            break;
        }
      }
    }
    newCompanyForm.save();

    assertEquals (newExemptions, newCompany.getExemptions ());
    assertEquals (newHazardousMaterials, newCompany.getHazardousMaterials ());
    assertEquals (newProducerComplianceScheme, newCompany.getProducerComplianceScheme ());
    assertEquals (newWasteDisposalProcess, newCompany.getWasteDisposalProcess ());
  }
}
