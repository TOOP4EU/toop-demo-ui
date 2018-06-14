package bean;

import eu.toop.demoui.bean.MainCompany;
import eu.toop.demoui.layouts.IdentityForm;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
