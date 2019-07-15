package eu.toop.demoui.certificate;

import com.helger.commons.collection.impl.CommonsArrayList;
import com.helger.commons.error.level.EErrorLevel;
import eu.toop.commons.dataexchange.v140.*;
import eu.toop.demoui.schema.SchemaFactory;
import eu.toop.demoui.schema.TDEDocumentResponseTypeBuilder;
import eu.toop.kafkaclient.ToopKafkaClient;

import javax.annotation.Nonnull;

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

            documentRequestType.setDocumentResponse(new CommonsArrayList<>());
            addDummyDocuments(documentRequestType);
        }
    }

    private void addDummyDocuments(TDEDocumentRequestType documentRequestType) {

        documentRequestType.getDocumentResponse().add(new TDEDocumentResponseTypeBuilder()
                .setDocumentName("ISMCompliance")
                .setDocumentDescription("Document of Compliance (DOC)")
                .setDocumentIdentifier("077SM/16")
                .setDocumentIssuePlace("Pallen, Elonia")
                .setLegalReference("SOLAS 1974")
                .addDocument(SchemaFactory.createDefaultTDEDocumentType())
                .addDocument(SchemaFactory.createDefaultTDEDocumentType())
                .build());

        documentRequestType.getDocumentResponse().add(new TDEDocumentResponseTypeBuilder()
                .setDocumentIssuer(SchemaFactory.createDefaultTDEIssuerType("__ISSUER"))
                .addDocument(SchemaFactory.createDefaultTDEDocumentType())
                .addDocument(SchemaFactory.createDefaultTDEDocumentType())
                .build());

        documentRequestType.getDocumentResponse().add(new TDEDocumentResponseTypeBuilder()
                .setDocumentName("ContinuousSynopsis")
                .setDocumentDescription("Continuous Synopsis Record (CSR)")
                .setDocumentIdentifier("088SM/19")
                .setDocumentIssuePlace("Pallen, Elonia")
                .setLegalReference("SOLAS 2000")
                .setDocumentIssuer(SchemaFactory.createDefaultTDEIssuerType("__ISSUER_2"))
                .addDocument(SchemaFactory.createDefaultTDEDocumentType())
                .addDocument(SchemaFactory.createDefaultTDEDocumentType())
                .build());

        documentRequestType.getDocumentResponse().add(new TDEDocumentResponseTypeBuilder()
                .setDocumentName("SafetyManagement")
                .setDocumentDescription("Safety Management Certiﬁcate (SMC)")
                .setDocumentIdentifier("999SM/19")
                .setDocumentIssuePlace("Pallen, Elonia")
                .setLegalReference("SOLAS 1980")
                .setDocumentIssuer(SchemaFactory.createDefaultTDEIssuerType("__ISSUER_3"))
                .addDocument(SchemaFactory.createDefaultTDEDocumentType())
                .addDocument(SchemaFactory.createDefaultTDEDocumentType())
                .build());
    }

}
