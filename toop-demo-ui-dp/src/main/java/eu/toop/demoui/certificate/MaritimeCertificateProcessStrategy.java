package eu.toop.demoui.certificate;

import com.helger.commons.collection.impl.CommonsArrayList;
import com.helger.commons.error.level.EErrorLevel;
import com.helger.commons.mime.CMimeType;
import eu.toop.commons.dataexchange.v140.*;
import eu.toop.commons.jaxb.ToopXSDHelper140;
import eu.toop.kafkaclient.ToopKafkaClient;

import javax.annotation.Nonnull;
import java.util.UUID;

public class MaritimeCertificateProcessStrategy implements CertificateProcessStrategy {

    private final String sLogPrefix;
    private final TDETOOPResponseType aResponse;

    public MaritimeCertificateProcessStrategy(@Nonnull final String sLogPrefix,
                                              @Nonnull final TDETOOPResponseType aResponse) {

        this.sLogPrefix = sLogPrefix;
        this.aResponse = aResponse;
    }

    @Override
    public void processCertificate() {
        ToopKafkaClient.send(EErrorLevel.INFO, () -> sLogPrefix + "Handling a document request/List");

        for (final TDEDocumentRequestType documentRequestType : aResponse.getDocumentRequest()) {

            final TDEDocumentResponseType documentResponseType = new TDEDocumentResponseType();
            documentResponseType.setDocumentName(ToopXSDHelper140.createText("ISMCompliance"));
            documentResponseType.setDocumentDescription(ToopXSDHelper140.createText("Document of Compliance (DOC)"));
            documentResponseType.setDocumentIdentifier(ToopXSDHelper140.createIdentifier("077SM/16"));
            documentResponseType.setDocumentIssueDate(ToopXSDHelper140.createDateWithLOANow());
            documentResponseType.setDocumentIssuePlace(ToopXSDHelper140.createText("Pallen, Elonia"));
            {
                final TDEIssuerType issuerType = new TDEIssuerType();
                issuerType.setDocumentIssuerIdentifier(ToopXSDHelper140.createIdentifier("elonia", "9916", "EE12345678"));
                issuerType.setDocumentIssuerName(ToopXSDHelper140.createText("EE-EMA"));
                documentResponseType.setDocumentIssuer(issuerType);
            }
            documentResponseType.setLegalReference(ToopXSDHelper140.createText("SOLAS 1974"));
            documentResponseType.setDocumentRemarks(new CommonsArrayList<>());
            documentResponseType.setErrorIndicator(ToopXSDHelper140.createIndicator(false));
            {
                final TDEDocumentType tdeDocument = new TDEDocumentType();
                tdeDocument.setDocumentURI(ToopXSDHelper140.createIdentifier(UUID.randomUUID().toString()));
                tdeDocument.setDocumentMimeTypeCode(ToopXSDHelper140.createCode(CMimeType.APPLICATION_PDF.getAsString()));
                documentResponseType.addDocument(tdeDocument);
            }
            // FIXME Setters values should probably change
            {
                final TDEDocumentType tdeDocument = new TDEDocumentType();
                tdeDocument.setDocumentURI(ToopXSDHelper140.createIdentifier(UUID.randomUUID().toString()));
                tdeDocument.setDocumentMimeTypeCode(ToopXSDHelper140.createCode(CMimeType.APPLICATION_PDF.getAsString()));
                documentResponseType.addDocument(tdeDocument);
            }
            documentRequestType.setDocumentResponse(new CommonsArrayList<>(documentResponseType));
        }
    }
}
