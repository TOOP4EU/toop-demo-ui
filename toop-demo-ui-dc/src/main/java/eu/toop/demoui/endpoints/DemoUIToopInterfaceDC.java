/**
 * Copyright (C) 2018-2019 toop.eu
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.toop.demoui.endpoints;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import javax.annotation.Nonnull;

import com.helger.commons.collection.impl.ICommonsList;
import com.helger.commons.error.level.EErrorLevel;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.UI;

import eu.toop.commons.dataexchange.v140.TDEConceptRequestType;
import eu.toop.commons.dataexchange.v140.TDEDataElementRequestType;
import eu.toop.commons.dataexchange.v140.TDEDataElementResponseValueType;
import eu.toop.commons.dataexchange.v140.TDEDataProviderType;
import eu.toop.commons.dataexchange.v140.TDETOOPResponseType;
import eu.toop.commons.exchange.AsicReadEntry;
import eu.toop.commons.exchange.ToopResponseWithAttachments140;
import eu.toop.demoui.DCToToopInterfaceMapper;
import eu.toop.demoui.bean.DocumentDataBean;
import eu.toop.demoui.layouts.MaritimePage;
import eu.toop.demoui.view.Maritime;
import eu.toop.iface.IToopInterfaceDC;
import eu.toop.kafkaclient.ToopKafkaClient;
import oasis.names.specification.ubl.schema.xsd.unqualifieddatatypes_21.IdentifierType;

public class DemoUIToopInterfaceDC implements IToopInterfaceDC {
    public DemoUIToopInterfaceDC() {
    }

    public void onToopResponse(@Nonnull final ToopResponseWithAttachments140 aResponseWA) throws IOException {
        final TDETOOPResponseType aResponse = aResponseWA.getResponse();
        final ICommonsList<AsicReadEntry> attachments = aResponseWA.attachments();

        DemoUIToopInterfaceHelper.dumpResponse(aResponse);

        final String sRequestID = aResponse.getDataRequestIdentifier().getValue();
        final String sLogPrefix = "[" + sRequestID + "] [DC] ";

        final TDEDataProviderType aDP = aResponse.hasNoDataProviderEntries() ? null : aResponse.getDataProviderAtIndex(0);

        ToopKafkaClient.send(EErrorLevel.INFO,
                () -> sLogPrefix +
                        "Received data from Data Provider: " +
                        (aDP == null ? "null"
                                : " DPIdentifier: " +
                                aDP.getDPIdentifier().getValue() +
                                ", " +
                                " DPName: " +
                                aDP.getDPName().getValue() +
                                ", " +
                                " DPElectronicAddressIdentifier: " +
                                aResponse.getRoutingInformation()
                                        .getDataProviderElectronicAddressIdentifier()
                                        .getValue()));

        ToopKafkaClient.send(EErrorLevel.INFO, () -> sLogPrefix + "Number of attachments: " + attachments.size());
        for (final AsicReadEntry attachment : attachments) {
            ToopKafkaClient.send(EErrorLevel.INFO,
                    () -> sLogPrefix +
                            "Received document: " +
                            attachment.getEntryName() +
                            ", size: " +
                            attachment.payload().length);
            // attachment.payload(); <-- this is the byte[]
        }

        // Push a new organization bean to the UI
        try {
            // Find the correct UI
            final UI aUI = DCToToopInterfaceMapper.getAndRemoveUI(sRequestID);
            if (aUI == null)
                throw new IllegalStateException("Having no Vaadin UI for request ID '" + sRequestID + "'");

            ToopKafkaClient.send(EErrorLevel.INFO, () -> sLogPrefix + "Current UI: " + aUI);
            final Navigator threadUINavigator = aUI.getNavigator();
            ToopKafkaClient.send(EErrorLevel.INFO, () -> sLogPrefix + "Current Navigator: " + threadUINavigator);

            final DocumentDataBean bean = new DocumentDataBean(attachments);
            // DocumentDataBean documentBean = new DocumentDataBean(attachments);
            if (threadUINavigator.getCurrentView() instanceof Maritime) {
                final Maritime homeView = (Maritime) threadUINavigator.getCurrentView();
                if (homeView.getCurrentPage() instanceof MaritimePage) {
//                    homeView.setToopDataBean(bean);\
                    homeView.setDocumentDataBean(bean);
                    // homeView.setDocumentDataBean(documentBean);
                    final MaritimePage page = (MaritimePage) homeView.getCurrentPage();

                    final String expectedUuid = page.getRequestId();

                    if (aResponse.getDataRequestIdentifier() != null &&
                            expectedUuid != null &&
                            expectedUuid.equals(aResponse.getDataRequestIdentifier().getValue())) {
                        if (!aResponse.hasErrorEntries()) {

                            final IdentifierType documentTypeIdentifier = aResponse.getRoutingInformation()
                                    .getDocumentTypeIdentifier();

                            if (documentTypeIdentifier.getValue().contains("list")) {
                                // add grid layout
                                final List<DocumentDataBean> docResponseList = DemoUIToopInterfaceHelper.getDocumentResponseDataBeanList(aResponse);
                                page.addDocumentCertificateList(docResponseList);
                                // page.addComponent(page.addDocumentCertificateList(docResponseList),
                                // "documentCertificateList");
                            } else {
                                if (attachments != null && attachments.size() > 0) {
                                    for (final AsicReadEntry attachment : attachments) {
                                        final StreamResource myResource = new StreamResource((StreamResource.StreamSource) () -> new ByteArrayInputStream(attachment.payload()),
                                                attachment.getEntryName());
                                        myResource.getStream()
                                                .setParameter("Content-Disposition",
                                                        "attachment;filename=\"" +
                                                                attachment.getEntryName() +
                                                                "\"");
                                        myResource.setCacheTime(0);
                                        myResource.setMIMEType("application/octet-stream");
                                        aUI.getPage().open(myResource, "_top", false);
                                    }
                                }
                            }
                        } else {
                            page.setError(aResponse.getError());
                        }

                        final String conceptErrors = getConceptErrors(aResponse);
                        if (!conceptErrors.isEmpty()) {
                            page.setConceptErrors(conceptErrors);
                        }
                    }
                }
            }

            ToopKafkaClient.send(EErrorLevel.INFO, () -> sLogPrefix + "Pushed new bean data to the Demo UI: " + bean);
        } catch (final Exception e) {
            ToopKafkaClient.send(EErrorLevel.ERROR, () -> sLogPrefix + "Failed to push new bean data to the Demo UI", e);
        }
    }

    private static String getConceptErrors(@Nonnull final TDETOOPResponseType aResponse) {

        final StringBuilder sb = new StringBuilder();

        // Inspect all mapped values
        for (final TDEDataElementRequestType aDER : aResponse.getDataElementRequest()) {

            final TDEConceptRequestType aFirstLevelConcept = aDER.getConceptRequest();

            final String sourceConceptName = aFirstLevelConcept.getConceptName().getValue();

            for (final TDEConceptRequestType aSecondLevelConcept : aFirstLevelConcept.getConceptRequest()) {

                final String toopConceptName = aSecondLevelConcept.getConceptName().getValue();

                for (final TDEConceptRequestType aThirdLevelConcept : aSecondLevelConcept.getConceptRequest()) {

                    final String destinationConceptName = aThirdLevelConcept.getConceptName().getValue();

                    final String mappedConcept = sourceConceptName + " - " + toopConceptName + " - " + destinationConceptName;

                    for (final TDEDataElementResponseValueType aThirdLevelConceptDERValue : aThirdLevelConcept.getDataElementResponseValue()) {

                        if (aThirdLevelConceptDERValue.getErrorIndicator() != null &&
                                aThirdLevelConceptDERValue.getErrorIndicator().isValue()) {
                            sb.append(" - Concept Error (").append(mappedConcept).append("):\n");
                            if (aThirdLevelConceptDERValue.getErrorCode() != null) {
                                sb.append("     ").append(aThirdLevelConceptDERValue.getErrorCode().getValue()).append("\n");
                            } else if (aThirdLevelConceptDERValue.getResponseDescription() != null) {
                                sb.append("     ")
                                        .append(aThirdLevelConceptDERValue.getResponseDescription().getValue())
                                        .append("\n");
                            }
                        }
                    }
                }
            }
        }
        return sb.toString();
    }
}
