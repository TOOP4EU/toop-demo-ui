package eu.toop.demoui.builder.model;

import eu.toop.commons.codelist.EPredefinedDocumentTypeIdentifier;

public class Request {
    private String
            countryCode,
            naturalPersonIdentifier = "",
            naturalPersonFirstName = "",
            naturalPersonFamilyName = "",
            id = "",
            legalName = "",
            documentIdentifier = "";
    private EPredefinedDocumentTypeIdentifier eDocumentTypeID;

    public Request(String countryCode, EPredefinedDocumentTypeIdentifier documentTypeID) {
        this.countryCode = countryCode;
        eDocumentTypeID = documentTypeID;
    }

    public EPredefinedDocumentTypeIdentifier geteDocumentTypeID() {
        return eDocumentTypeID;
    }

    public void seteDocumentTypeID(EPredefinedDocumentTypeIdentifier eDocumentTypeID) {
        this.eDocumentTypeID = eDocumentTypeID;
    }

    public String getDocumentIdentifier() {
        return documentIdentifier;
    }

    public void setDocumentIdentifier(String documentIdentifier) {
        this.documentIdentifier = documentIdentifier;
    }


    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getNaturalPersonIdentifier() {
        return naturalPersonIdentifier;
    }

    public void setNaturalPersonIdentifier(String naturalPersonIdentifier) {
        this.naturalPersonIdentifier = naturalPersonIdentifier;
    }

    public String getNaturalPersonFirstName() {
        return naturalPersonFirstName;
    }

    public void setNaturalPersonFirstName(String naturalPersonFirstName) {
        this.naturalPersonFirstName = naturalPersonFirstName;
    }

    public String getNaturalPersonFamilyName() {
        return naturalPersonFamilyName;
    }

    public void setNaturalPersonFamilyName(String naturalPersonFamilyName) {
        this.naturalPersonFamilyName = naturalPersonFamilyName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLegalName() {
        return legalName;
    }

    public void setLegalName(String legalName) {
        this.legalName = legalName;
    }

}
