package eu.toop.demoui.bean;

import java.io.Serializable;

public class NewCompany implements Serializable {
  private String companyTradeName;
  private String undertakingIdentification;
  private String companyType;
  private String legalForm;

  public String getCompanyTradeName () {
    return companyTradeName;
  }

  public void setCompanyTradeName (String companyTradeName) {
    this.companyTradeName = companyTradeName;
  }

  public String getUndertakingIdentification () {
    return undertakingIdentification;
  }

  public void setUndertakingIdentification (String undertakingIdentification) {
    this.undertakingIdentification = undertakingIdentification;
  }

  public String getCompanyType () {
    return companyType;
  }

  public void setCompanyType (String companyType) {
    this.companyType = companyType;
  }

  public String getLegalForm () {
    return legalForm;
  }

  public void setLegalForm (String legalForm) {
    this.legalForm = legalForm;
  }
}
