package eu.toop.demoui.form;

import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;

import eu.toop.demoui.bean.NewCompany;

public class NewCompanyForm extends FormLayout {

  private final Binder<NewCompany> binder = new Binder<> ();
  NewCompany newCompany;

  public NewCompanyForm (NewCompany newCompany, boolean readOnly) {

    TextField wasteDisposalProcess = new TextField("Waste disposal process");
    TextField hazardousMaterials = new TextField("Hazardous materials");
    TextField exemptions = new TextField("Exemptions");
    TextField producerComplianceScheme = new TextField("Producer compliance scheme");

    binder.bind (wasteDisposalProcess, NewCompany::getWasteDisposalProcess, NewCompany::setWasteDisposalProcess);
    binder.bind (hazardousMaterials, NewCompany::getHazardousMaterials, NewCompany::setHazardousMaterials);
    binder.bind (exemptions, NewCompany::getExemptions, NewCompany::setExemptions);
    binder.bind (producerComplianceScheme, NewCompany::getProducerComplianceScheme, NewCompany::setProducerComplianceScheme);

    wasteDisposalProcess.setReadOnly (readOnly);
    hazardousMaterials.setReadOnly (readOnly);
    exemptions.setReadOnly (readOnly);
    producerComplianceScheme.setReadOnly (readOnly);

    addComponent (wasteDisposalProcess);
    addComponent (hazardousMaterials);
    addComponent (exemptions);
    addComponent (producerComplianceScheme);

    setNewCompanyBean (newCompany);
  }

  public void setNewCompanyBean(NewCompany _newCompany) {
    newCompany = _newCompany;
    binder.readBean(newCompany);
  }

  public void save() {
    try {
      binder.writeBean (newCompany);
    } catch (ValidationException e) {
      e.printStackTrace ();
    }
  }
}
