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

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getBirthPlace() {
        return birthPlace;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }
}
