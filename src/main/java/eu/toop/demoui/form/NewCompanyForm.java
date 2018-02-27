package eu.toop.demoui.form;

import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;
import eu.toop.demoui.bean.NewCompany;

public class NewCompanyForm extends FormLayout {

  private final Binder<NewCompany> binder = new Binder<> ();
  NewCompany newCompany;

  public NewCompanyForm (NewCompany newCompany, boolean readOnly) {

    TextField companyTradeName = new TextField("Company trade name");
    TextField undertakingIdentification = new TextField("Undertaking identification");
    TextField companyType = new TextField("Company type");
    TextField legalForm = new TextField("Legal form");

    binder.bind (companyTradeName, NewCompany::getCompanyTradeName, NewCompany::setCompanyTradeName);
    binder.bind (undertakingIdentification, NewCompany::getUndertakingIdentification, NewCompany::setUndertakingIdentification);
    binder.bind (companyType, NewCompany::getCompanyType, NewCompany::setCompanyType);
    binder.bind (legalForm, NewCompany::getLegalForm, NewCompany::setLegalForm);

    companyTradeName.setReadOnly (readOnly);
    undertakingIdentification.setReadOnly (readOnly);
    companyType.setReadOnly (readOnly);
    legalForm.setReadOnly (readOnly);

    addComponent (companyTradeName);
    addComponent (undertakingIdentification);
    addComponent (companyType);
    addComponent (legalForm);

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
