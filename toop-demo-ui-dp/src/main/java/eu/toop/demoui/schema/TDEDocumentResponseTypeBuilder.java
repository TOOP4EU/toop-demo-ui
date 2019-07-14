package eu.toop.demoui.schema;

import com.helger.commons.collection.impl.CommonsArrayList;
import eu.toop.commons.dataexchange.v140.TDEDateWithLOAType;
import eu.toop.commons.dataexchange.v140.TDEDocumentResponseType;
import eu.toop.commons.dataexchange.v140.TDEDocumentType;
import eu.toop.commons.dataexchange.v140.TDEIssuerType;
import eu.toop.commons.jaxb.ToopXSDHelper140;
import oasis.names.specification.ubl.schema.xsd.unqualifieddatatypes_21.TextType;

import java.util.ArrayList;
import java.util.List;

public class TDEDocumentResponseTypeBuilder {

    private String name;
    private String description;
    private String identifier;
    private TDEDateWithLOAType issueDate;
    private String issuePlace;
    private TDEIssuerType issuer;
    private String legalReference;
    private List<TextType> documentRemarks;
    private boolean errorIndicator;
    private List<TDEDocumentType> documentTypeList;

    public TDEDocumentResponseTypeBuilder() {
        this.name = "MinimumSafeManning";
        this.description = "Minimum Safe Manning Certiﬁcate";
        this.identifier = "090SM/17";
        this.issueDate = ToopXSDHelper140.createDateWithLOANow();
        this.issuePlace = "Pallen, Elonia";
        this.issuer = SchemaFactory.createDefaultTDEIssuerType();
        this.legalReference = "SOLAS 1978";
        this.documentRemarks = new CommonsArrayList<>();
        this.errorIndicator = false;
    }

    public TDEDocumentResponseTypeBuilder setDocumentName(String name) {
        this.name = name;
        return TDEDocumentResponseTypeBuilder.this;
    }

    public TDEDocumentResponseTypeBuilder setDocumentDescription(String description) {
        this.description = description;
        return TDEDocumentResponseTypeBuilder.this;
    }

    public TDEDocumentResponseTypeBuilder setDocumentIdentifier(String identifier) {
        this.identifier = identifier;
        return TDEDocumentResponseTypeBuilder.this;
    }

//    public TDEDocumentResponseTypeBuilder setDocumentIssueDate(TDEDateWithLOAType issueDate) {
//        this.issueDate = issueDate;
//        return TDEDocumentResponseTypeBuilder.this;
//    }

    public TDEDocumentResponseTypeBuilder setDocumentIssuePlace(String issuePlace) {
        this.issuePlace = issuePlace;
        return TDEDocumentResponseTypeBuilder.this;
    }

    public TDEDocumentResponseTypeBuilder setDocumentIssuer(TDEIssuerType issuer) {
        this.issuer = issuer;
        return TDEDocumentResponseTypeBuilder.this;
    }

    public TDEDocumentResponseTypeBuilder setLegalReference(String legalReference) {
        this.legalReference = legalReference;
        return TDEDocumentResponseTypeBuilder.this;
    }

//    public TDEDocumentResponseTypeBuilder setDocumentRemarks(@Nonnull List<TextType> documentRemarks) {
//        this.documentRemarks = documentRemarks;
//        return TDEDocumentResponseTypeBuilder.this;
//    }

//    public TDEDocumentResponseTypeBuilder setErrorIndicator(boolean errorIndicator) {
//        this.errorIndicator = errorIndicator;
//        return TDEDocumentResponseTypeBuilder.this;
//    }

    public TDEDocumentResponseTypeBuilder addDocument(TDEDocumentType documentType) {
        if (documentTypeList == null) {
            documentTypeList = new ArrayList<>();
        }
        documentTypeList.add(documentType);
        return TDEDocumentResponseTypeBuilder.this;
    }

    public TDEDocumentResponseType build() {
        final TDEDocumentResponseType documentResponseType = new TDEDocumentResponseType();

        documentResponseType.setDocumentName(ToopXSDHelper140.createText(name));
        documentResponseType.setDocumentDescription(ToopXSDHelper140.createText(description));
        documentResponseType.setDocumentIdentifier(ToopXSDHelper140.createIdentifier(identifier));
        documentResponseType.setDocumentIssueDate(issueDate);
        documentResponseType.setDocumentIssuePlace(ToopXSDHelper140.createText(issuePlace));
        documentResponseType.setDocumentIssuer(issuer);
        documentResponseType.setLegalReference(ToopXSDHelper140.createText(legalReference));
        documentResponseType.setDocumentRemarks(documentRemarks);
        documentResponseType.setErrorIndicator(ToopXSDHelper140.createIndicator(errorIndicator));
        documentTypeList.forEach(documentResponseType::addDocument);

        return documentResponseType;
    }

}
