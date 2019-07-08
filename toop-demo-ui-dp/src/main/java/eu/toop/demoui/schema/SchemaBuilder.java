package eu.toop.demoui.schema;

import com.helger.commons.collection.impl.CommonsArrayList;
import com.helger.commons.mime.CMimeType;
import eu.toop.commons.dataexchange.v140.TDEDocumentResponseType;
import eu.toop.commons.dataexchange.v140.TDEDocumentType;
import eu.toop.commons.dataexchange.v140.TDEIssuerType;
import eu.toop.commons.jaxb.ToopXSDHelper140;

import java.util.UUID;

public interface SchemaBuilder {

    default TDEDocumentResponseType createDefaultTDEDocumentResponseType() {

        final TDEDocumentResponseType documentResponseType = new TDEDocumentResponseType();

        documentResponseType.setDocumentName(ToopXSDHelper140.createText("ISMCompliance"));
        documentResponseType.setDocumentDescription(ToopXSDHelper140.createText("Document of Compliance (DOC)"));
        documentResponseType.setDocumentIdentifier(ToopXSDHelper140.createIdentifier("077SM/16"));
        documentResponseType.setDocumentIssueDate(ToopXSDHelper140.createDateWithLOANow());
        documentResponseType.setDocumentIssuePlace(ToopXSDHelper140.createText("Pallen, Elonia"));
        documentResponseType.setDocumentIssuer(createDefaultTDEIssuerType());
        documentResponseType.setLegalReference(ToopXSDHelper140.createText("SOLAS 1974"));
        documentResponseType.setDocumentRemarks(new CommonsArrayList<>());
        documentResponseType.setErrorIndicator(ToopXSDHelper140.createIndicator(false));
        documentResponseType.addDocument(createDefaultTDEDocumentType());
        documentResponseType.addDocument(createDefaultTDEDocumentType());

        return documentResponseType;
    }

    default TDEIssuerType createDefaultTDEIssuerType() {

        final TDEIssuerType issuerType = new TDEIssuerType();

        issuerType.setDocumentIssuerIdentifier(ToopXSDHelper140.createIdentifier("elonia", "9916", "EE12345678"));
        issuerType.setDocumentIssuerName(ToopXSDHelper140.createText("EE-EMA"));

        return issuerType;
    }

    default TDEDocumentType createDefaultTDEDocumentType() {

        final TDEDocumentType tdeDocument = new TDEDocumentType();

        tdeDocument.setDocumentURI(ToopXSDHelper140.createIdentifier(UUID.randomUUID().toString()));
        tdeDocument.setDocumentMimeTypeCode(ToopXSDHelper140.createCode(CMimeType.APPLICATION_PDF.getAsString()));

        return tdeDocument;
    }

}
