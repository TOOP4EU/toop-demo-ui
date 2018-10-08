package layouts;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.vaadin.ui.Component;
import com.vaadin.ui.TextField;

import eu.toop.demoui.bean.MainCompany;
import eu.toop.demoui.layouts.MainCompanyForm;

public class MainCompanyFormTest {
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

    final String newActivityDeclaration = "New Manufacture of other electrical equipment";
    final String newAddress = "New Gamlavegen 234, 321 44, Velma, Elonia";
    final String newBusinessCode = "New JF 234556-6213";
    final String newCompanyNaceCode = "New C27.9";
    final String newCompanyName = "New Zizi mat";
    final String newCompanyType = "New Limited";
    final String newLegalStatus = "New Active";
    final String newLegalStatusEffectiveDate = "New 2012-01-12";
    final String newRegistrationAuthority = "New Elonia Tax Agency";
    final String newRegistrationDate = "New 2012-01-12";
    final String newRegistrationNumber = "New 009987 665543";
    final String newSsNumber = "New PKL 0987 6548";
    final String newVatNumber = "New 09897656";

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

    final MainCompanyForm mainCompanyForm = new MainCompanyForm (mainCompany, false);

    for (final Component comp : mainCompanyForm) {
      if (comp instanceof TextField) {
        final TextField textField = (TextField)comp;
        switch (textField.getCaption ()) {
          case "Address":
            textField.setValue (newAddress);
            break;
          case "SS number":
            textField.setValue (newSsNumber);
            break;
          case "Company code":
            textField.setValue (newBusinessCode);
            break;
          case "VAT number":
            textField.setValue (newVatNumber);
            break;
          case "Company type":
            textField.setValue (newCompanyType);
            break;
          case "Legal status":
            textField.setValue (newLegalStatus);
            break;
          case "Legal status effective date":
            textField.setValue (newLegalStatusEffectiveDate);
            break;
          case "Registration date":
            textField.setValue (newRegistrationDate);
            break;
          case "Registration number":
            textField.setValue (newRegistrationNumber);
            break;
          case "Company name":
            textField.setValue (newCompanyName);
            break;
          case "Company nace code":
            textField.setValue (newCompanyNaceCode);
            break;
          case "Activity declaration":
            textField.setValue (newActivityDeclaration);
            break;
          case "Registration authority":
            textField.setValue (newRegistrationAuthority);
            break;
        }
      }
    }
    mainCompanyForm.save();

    assertEquals (newActivityDeclaration, mainCompany.getActivityDeclaration ());
    assertEquals (newAddress, mainCompany.getAddress ());
    assertEquals (newBusinessCode, mainCompany.getBusinessCode ());
    assertEquals (newCompanyNaceCode, mainCompany.getCompanyNaceCode ());
    assertEquals (newCompanyName, mainCompany.getCompanyName ());
    assertEquals (newCompanyType, mainCompany.getCompanyType ());
    assertEquals (newLegalStatus, mainCompany.getLegalStatus ());
    assertEquals (newLegalStatusEffectiveDate, mainCompany.getLegalStatusEffectiveDate ());
    assertEquals (newRegistrationAuthority, mainCompany.getRegistrationAuthority ());
    assertEquals (newRegistrationDate, mainCompany.getRegistrationDate ());
    assertEquals (newRegistrationNumber, mainCompany.getRegistrationNumber ());
    assertEquals (newSsNumber, mainCompany.getSSNumber ());
    assertEquals (newVatNumber, mainCompany.getVATNumber ());
  }
}
