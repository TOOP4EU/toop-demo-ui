/**
 * Copyright (C) 2018-2019 toop.eu
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
