package eu.toop.demoui.schema;

import com.helger.commons.mime.CMimeType;
import eu.toop.commons.dataexchange.v140.TDEDocumentType;
import eu.toop.commons.dataexchange.v140.TDEIssuerType;
import eu.toop.commons.jaxb.ToopXSDHelper140;

import java.util.UUID;

public class SchemaFactory {

    public static TDEIssuerType createDefaultTDEIssuerType() {

        final TDEIssuerType issuerType = new TDEIssuerType();

        issuerType.setDocumentIssuerIdentifier(ToopXSDHelper140.createIdentifier("elonia", "9916", "EE12345678"));
        issuerType.setDocumentIssuerName(ToopXSDHelper140.createText("EE-EMA"));

        return issuerType;
    }

    public static TDEIssuerType createDefaultTDEIssuerType(String issuerName) {

        final TDEIssuerType issuerType = new TDEIssuerType();

        issuerType.setDocumentIssuerIdentifier(ToopXSDHelper140.createIdentifier("elonia", "9916", "EE12345678"));
        issuerType.setDocumentIssuerName(ToopXSDHelper140.createText(issuerName));

        return issuerType;
    }

    public static TDEDocumentType createDefaultTDEDocumentType() {

        final TDEDocumentType tdeDocument = new TDEDocumentType();

        tdeDocument.setDocumentURI(ToopXSDHelper140.createIdentifier(UUID.randomUUID().toString()));
        tdeDocument.setDocumentMimeTypeCode(ToopXSDHelper140.createCode(CMimeType.APPLICATION_PDF.getAsString()));

        return tdeDocument;
    }

}
