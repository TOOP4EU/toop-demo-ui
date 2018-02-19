package eu.toop.demoui.bean;

import java.time.LocalDate;

public class Identity implements java.io.Serializable {
  private String firstName;
  private String familyName;
  private String birthPlace;
  private LocalDate birthDate;
  private String identifier;
  private String nationality;

  public Identity() {
    // Preset mockup data
    firstName = "John";
    familyName = "Doe";
    birthPlace = "Fridili";
    birthDate = LocalDate.parse("1986-02-01");
    identifier = "EL/EL/12345";
    nationality = "EL";
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(final String firstName) {
    this.firstName = firstName;
  }

  public String getFamilyName() {
    return familyName;
  }

  public void setFamilyName(final String familyName) {
    this.familyName = familyName;
  }

  public String getBirthPlace() {
    return birthPlace;
  }

  public void setBirthPlace(final String birthPlace) {
    this.birthPlace = birthPlace;
  }

  public LocalDate getBirthDate() {
    return birthDate;
  }

  public void setBirthDate(final LocalDate birthDate) {
    this.birthDate = birthDate;
  }

  public String getIdentifier() {
    return identifier;
  }

  public void setIdentifier(final String identifier) {
    this.identifier = identifier;
  }

  public String getNationality() {
    return nationality;
  }

  public void setNationality(final String nationality) {
    this.nationality = nationality;
  }
}
