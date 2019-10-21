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
package eu.toop.demoui.endpoints;

import java.io.IOException;

import javax.annotation.Nonnull;

import com.helger.commons.collection.impl.CommonsArrayList;
import com.helger.commons.collection.impl.ICommonsList;
import com.helger.commons.error.level.EErrorLevel;

import eu.toop.commons.codelist.EPredefinedDocumentTypeIdentifier;
import eu.toop.commons.dataexchange.v140.TDEAddressType;
import eu.toop.commons.dataexchange.v140.TDEDataProviderType;
import eu.toop.commons.dataexchange.v140.TDEErrorType;
import eu.toop.commons.dataexchange.v140.TDETOOPRequestType;
import eu.toop.commons.dataexchange.v140.TDETOOPResponseType;
import eu.toop.commons.error.ToopErrorException;
import eu.toop.commons.exchange.AsicReadEntry;
import eu.toop.commons.exchange.AsicWriteEntry;
import eu.toop.commons.exchange.ToopMessageBuilder140;
import eu.toop.commons.exchange.ToopRequestWithAttachments140;
import eu.toop.commons.exchange.ToopResponseWithAttachments140;
import eu.toop.commons.jaxb.ToopXSDHelper140;
import eu.toop.commons.usecase.ReverseDocumentTypeMapping;
import eu.toop.demoui.DPUIConfig;
import eu.toop.demoui.certificate.*;
import eu.toop.iface.IToopInterfaceDP;
import eu.toop.iface.ToopInterfaceClient;
import eu.toop.iface.ToopInterfaceConfig;
import eu.toop.kafkaclient.ToopKafkaClient;
import oasis.names.specification.ubl.schema.xsd.unqualifieddatatypes_21.IdentifierType;

public final class DemoUIToopInterfaceDP implements IToopInterfaceDP {
    @Nonnull
    private static TDETOOPResponseType _createResponseFromRequest(@Nonnull final TDETOOPRequestType aRequest,
                                                                  @Nonnull final String sLogPrefix) {
        // build response
        final TDETOOPResponseType aResponse = ToopMessageBuilder140.createResponse(aRequest);
        {
            // Required for response
            aResponse.getRoutingInformation()
                    .setDataProviderElectronicAddressIdentifier(ToopXSDHelper140.createIdentifier("elonia@register.example.org"));

            final TDEDataProviderType p = new TDEDataProviderType();
            p.setDPIdentifier(ToopXSDHelper140.createIdentifier("demo-agency",
                    DPUIConfig.getResponderIdentifierScheme(),
                    DPUIConfig.getResponderIdentifierValue()));
            p.setDPName(ToopXSDHelper140.createText("EloniaDP"));
            final TDEAddressType pa = new TDEAddressType();
            pa.setCountryCode(ToopXSDHelper140.createCodeWithLOA(DPUIConfig.getProviderCountryCode()));
            p.setDPLegalAddress(pa);
            aResponse.addDataProvider(p);
        }

        // Document type must be switch from request to response
        final IdentifierType aDocTypeID = aRequest.getRoutingInformation().getDocumentTypeIdentifier();
        final EPredefinedDocumentTypeIdentifier eRequestDocType = EPredefinedDocumentTypeIdentifier.getFromDocumentTypeIdentifierOrNull(aDocTypeID.getSchemeID(),
                aDocTypeID.getValue());
        if (eRequestDocType != null) {
            final EPredefinedDocumentTypeIdentifier eResponseDocType = ReverseDocumentTypeMapping.getReverseDocumentTypeOrNull(eRequestDocType);
            if (eResponseDocType == null) {
                // Found no reverse document type
                ToopKafkaClient.send(EErrorLevel.ERROR,
                        () -> sLogPrefix +
                                "Found no response document type for '" +
                                aDocTypeID.getSchemeID() +
                                "::" +
                                aDocTypeID.getValue() +
                                "'");
            } else {
                // Set new doc type in response
                ToopKafkaClient.send(EErrorLevel.INFO,
                        () -> sLogPrefix +
                                "Switching document type '" +
                                eRequestDocType.getURIEncoded() +
                                "' to '" +
                                eResponseDocType.getURIEncoded() +
                                "'");
                aResponse.getRoutingInformation()
                        .setDocumentTypeIdentifier(ToopXSDHelper140.createIdentifier(eResponseDocType.getScheme(),
                                eResponseDocType.getID()));
            }
        }
        return aResponse;
    }

    public void onToopRequest(@Nonnull final ToopRequestWithAttachments140 aRequestWA) throws IOException {
        final TDETOOPRequestType aRequest = aRequestWA.getRequest();
        final ICommonsList<AsicReadEntry> attachments = aRequestWA.attachments();

        final String sRequestID = aRequest.getDocumentUniversalUniqueIdentifier().getValue();
        final String sLogPrefix = "[" + sRequestID + "] ";
        ToopKafkaClient.send(EErrorLevel.INFO,
                () -> sLogPrefix +
                        "Received DP Backend Request" +
                        (attachments.isEmpty() ? "" : " with " + attachments.size() + " attachment(s)"));

        final IdentifierType aDocType = aRequest.getRoutingInformation().getDocumentTypeIdentifier();
        final EPredefinedDocumentTypeIdentifier eDocType = aDocType == null ? null
                : EPredefinedDocumentTypeIdentifier.getFromDocumentTypeIdentifierOrNull(aDocType.getSchemeID(),
                aDocType.getValue());

        DemoUIToopInterfaceHelper.dumpRequest(aRequest);

        // build response
        final TDETOOPResponseType aResponse = _createResponseFromRequest(aRequest, sLogPrefix);
        aResponse.setSpecificationIdentifier(ToopXSDHelper140.createSpecificationIdentifierResponse());

        final ICommonsList<AsicWriteEntry> documentEntries = new CommonsArrayList<>();

        // Record matching to dataset
        if (aRequest.hasDataElementRequestEntries())
            HandlerDataRequest.handle(sLogPrefix, aRequest, aResponse);

        // handle document request
        if (aResponse.hasDocumentRequestEntries()) {

            final CertificateProcessContext processContext;
            final CertificateProcessStrategy processStrategy;

            if (eDocType == EPredefinedDocumentTypeIdentifier.REQUEST_CREWCERTIFICATE_LIST ||
                    eDocType == EPredefinedDocumentTypeIdentifier.REQUEST_SHIPCERTIFICATE_LIST) {

                processStrategy = new MaritimeCertificateProcessStrategy(sLogPrefix, aResponse);
                processContext = new CertificateProcessContext(processStrategy);
            } else {

                processStrategy = new NonMaritimeCertificateProcessStrategy(sLogPrefix, aRequest, aResponse, documentEntries);
                processContext = new CertificateProcessContext(processStrategy);
            }

            processContext.executeProcessStrategy();
        }

        // send back to toop-connector at /from-dp
        // The URL must be configured in toop-interface.properties file
        try {
            DemoUIToopInterfaceHelper.dumpResponse(aResponse);

            // Signing happens internally
            ToopInterfaceClient.sendResponseToToopConnector(aResponse,
                    ToopInterfaceConfig.getToopConnectorDPUrl(),
                    documentEntries);
        } catch (final ToopErrorException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void onToopErrorResponse(@Nonnull final ToopResponseWithAttachments140 aResponseWA) throws IOException {
        final TDETOOPResponseType aResponse = aResponseWA.getResponse();
        final ICommonsList<AsicReadEntry> attachments = aResponseWA.attachments();

        DemoUIToopInterfaceHelper.dumpResponse(aResponse);

        final IdentifierType docUuid = aResponse.getDocumentUniversalUniqueIdentifier();
        final String sRequestID = (docUuid != null ? docUuid.getValue() : "");
        final String sLogPrefix = "[" + sRequestID + "] ";

        final StringBuilder sb = new StringBuilder();
        sb.append(sLogPrefix);
        sb.append("Received TOOP Error Response from TC.");
        sb.append(" Contains ").append(attachments.size()).append(" documents(s)\n");

        if (aResponse.hasErrorEntries()) {
            sb.append(" Contains ").append(aResponse.getErrorCount()).append(" error(s)\n");
            for (final TDEErrorType error : aResponse.getError())
                sb.append(error).append("\n");
            ToopKafkaClient.send(EErrorLevel.ERROR, sb::toString);
        } else {
            // Warnings only
            ToopKafkaClient.send(EErrorLevel.WARN, sb::toString);
        }
    }
}
