package eu.toop.demoui.certificate;

import com.helger.commons.collection.impl.CommonsArrayList;
import com.helger.commons.error.level.EErrorLevel;
import com.helger.commons.mime.CMimeType;
import eu.toop.commons.dataexchange.v140.*;
import eu.toop.commons.jaxb.ToopXSDHelper140;
import eu.toop.demoui.schema.SchemaBuilder;
import eu.toop.kafkaclient.ToopKafkaClient;

import javax.annotation.Nonnull;
import java.util.UUID;

public class MaritimeCertificateProcessStrategy implements CertificateProcessStrategy, SchemaBuilder {

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
            documentRequestType.setDocumentResponse(new CommonsArrayList<>(createDefaultTDEDocumentResponseType()));
            documentRequestType.getDocumentResponse().add(createDefaultTDEDocumentResponseType());
        }
    }
}
