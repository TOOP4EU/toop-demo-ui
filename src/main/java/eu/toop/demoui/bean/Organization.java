package eu.toop.demoui.bean;

import java.io.Serializable;

public class Organization implements Serializable {
  private String companyName;
  private String companyType;

  public String getCompanyName() {
    return companyName;
  }

  public void setCompanyName(String companyName) {
    this.companyName = companyName;
  }

  public String getCompanyType() {
    return companyType;
  }

  public void setCompanyType(String companyType) {
    this.companyType = companyType;
  }
}
