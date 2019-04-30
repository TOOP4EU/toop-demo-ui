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

import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Nonnull;

import com.helger.commons.collection.impl.ICommonsList;
import com.helger.commons.error.level.EErrorLevel;
import com.helger.commons.mime.MimeTypeParser;

import eu.toop.commons.codelist.EPredefinedDocumentTypeIdentifier;
import eu.toop.commons.dataexchange.v140.TDEAddressType;
import eu.toop.commons.dataexchange.v140.TDEConceptRequestType;
import eu.toop.commons.dataexchange.v140.TDEDataElementRequestType;
import eu.toop.commons.dataexchange.v140.TDEDataElementResponseValueType;
import eu.toop.commons.dataexchange.v140.TDEDataProviderType;
import eu.toop.commons.dataexchange.v140.TDEDataRequestSubjectType;
import eu.toop.commons.dataexchange.v140.TDEDocumentRequestType;
import eu.toop.commons.dataexchange.v140.TDEDocumentResponseType;
import eu.toop.commons.dataexchange.v140.TDEDocumentType;
import eu.toop.commons.dataexchange.v140.TDEErrorType;
import eu.toop.commons.dataexchange.v140.TDEIssuerType;
import eu.toop.commons.dataexchange.v140.TDETOOPRequestType;
import eu.toop.commons.dataexchange.v140.TDETOOPResponseType;
import eu.toop.commons.error.ToopErrorException;
import eu.toop.commons.exchange.AsicReadEntry;
import eu.toop.commons.exchange.AsicWriteEntry;
import eu.toop.commons.exchange.ToopMessageBuilder140;
import eu.toop.commons.exchange.ToopRequestWithAttachments140;
import eu.toop.commons.exchange.ToopResponseWithAttachments140;
import eu.toop.commons.jaxb.ToopWriter;
import eu.toop.commons.jaxb.ToopXSDHelper140;
import eu.toop.commons.usecase.ReverseDocumentTypeMapping;
import eu.toop.demoui.DCUIConfig;
import eu.toop.iface.IToopInterfaceDP;
import eu.toop.iface.ToopInterfaceClient;
import eu.toop.iface.ToopInterfaceConfig;
import eu.toop.kafkaclient.ToopKafkaClient;
import oasis.names.specification.ubl.schema.xsd.unqualifieddatatypes_21.IdentifierType;
import oasis.names.specification.ubl.schema.xsd.unqualifieddatatypes_21.TextType;

public class DemoUIToopInterfaceDP implements IToopInterfaceDP {

  private static boolean _canUseConcept (@Nonnull final TDEConceptRequestType aConcept) {
    // This class can only deliver to "DP" concept types without child entries
    return aConcept.hasNoConceptRequestEntries () && "DP".equals (aConcept.getConceptTypeCode ().getValue ());
  }

  private static void _applyStaticDataset (final TDEDataRequestSubjectType ds,
      @Nonnull final TDEConceptRequestType aConcept, @Nonnull final String sLogPrefix,
      final DCUIConfig.Dataset dataset) {

    final TextType conceptName = aConcept.getConceptName ();
    final TDEDataElementResponseValueType aValue = new TDEDataElementResponseValueType ();
    aConcept.getDataElementResponseValue ().add (aValue);

    if (conceptName == null) {
      aValue.setErrorIndicator (ToopXSDHelper140.createIndicator (true));
      aValue.setErrorCode (ToopXSDHelper140.createCode ("MockError from DemoDP: Concept name missing"));
      ToopKafkaClient.send (EErrorLevel.ERROR, () -> sLogPrefix + "Concept name missing: " + aConcept);
      return;
    }

    aValue.setAlternativeResponseIndicator (ToopXSDHelper140.createIndicator (false));
    aValue.setErrorIndicator (ToopXSDHelper140.createIndicator (false));

    if (dataset != null) {
      final String conceptValue = dataset.getConceptValue (conceptName.getValue ());

      if (conceptValue == null) {
        aValue.setErrorIndicator (ToopXSDHelper140.createIndicator (true));
        aValue.setErrorCode (ToopXSDHelper140
            .createCode ("MockError from DemoDP: Concept [" + conceptName.getValue () + "] is missing in dataset"));
        ToopKafkaClient.send (EErrorLevel.ERROR, () -> sLogPrefix + "Failed to populate concept: Concept ["
            + conceptName.getValue () + "] is missing in dataset");
        return;
      }

      aValue.setResponseDescription (ToopXSDHelper140.createText (conceptValue));

      ToopKafkaClient.send (EErrorLevel.INFO,
          () -> sLogPrefix + "Populated concept [" + conceptName.getValue () + "]: [" + conceptValue + "]");
    } else {

      aValue.setErrorIndicator (ToopXSDHelper140.createIndicator (true));
      aValue.setErrorCode (ToopXSDHelper140.createCode ("MockError from DemoDP: No dataset found"));
    }
  }

  @Nonnull
  private static TDETOOPResponseType _createResponseFromRequest (@Nonnull final TDETOOPRequestType aRequest,
      @Nonnull final String sLogPrefix) {
    // build response
    final TDETOOPResponseType aResponse = ToopMessageBuilder140.createResponse (aRequest);
    {
      // Required for response
      aResponse.getRoutingInformation ().setDataProviderElectronicAddressIdentifier (
          ToopXSDHelper140.createIdentifier ("elonia@register.example.org"));

      final TDEDataProviderType p = new TDEDataProviderType ();
      p.setDPIdentifier (ToopXSDHelper140.createIdentifier ("demo-agency",
              DCUIConfig.getResponderIdentifierScheme (),
              DCUIConfig.getResponderIdentifierValue ()));
      p.setDPName (ToopXSDHelper140.createText ("EloniaDP"));
      final TDEAddressType pa = new TDEAddressType ();
      pa.setCountryCode (ToopXSDHelper140.createCodeWithLOA (DCUIConfig.getProviderCountryCode ()));
      p.setDPLegalAddress (pa);
      aResponse.addDataProvider (p);
    }

    // Document type must be switch from request to response
    final EPredefinedDocumentTypeIdentifier eRequestDocType = EPredefinedDocumentTypeIdentifier
        .getFromDocumentTypeIdentifierOrNull (
            aRequest.getRoutingInformation ().getDocumentTypeIdentifier ().getSchemeID (),
            aRequest.getRoutingInformation ().getDocumentTypeIdentifier ().getValue ());
    if (eRequestDocType != null) {
      try {
        final EPredefinedDocumentTypeIdentifier eResponseDocType = ReverseDocumentTypeMapping
            .getReverseDocumentType (eRequestDocType);

        // Set new doc type in response
        ToopKafkaClient.send (EErrorLevel.INFO, () -> sLogPrefix + "Switching document type '"
            + eRequestDocType.getURIEncoded () + "' to '" + eResponseDocType.getURIEncoded () + "'");
        aResponse.getRoutingInformation ().setDocumentTypeIdentifier (
            ToopXSDHelper140.createIdentifier (eResponseDocType.getScheme (), eResponseDocType.getID ()));
      } catch (final IllegalArgumentException ex) {
        // Found no reverse document type
        ToopKafkaClient.send (EErrorLevel.INFO,
            () -> sLogPrefix + "Found no response document type for '"
                + aRequest.getRoutingInformation ().getDocumentTypeIdentifier ().getSchemeID () + "::"
                + aRequest.getRoutingInformation ().getDocumentTypeIdentifier ().getValue () + "'");
      }
    }
    return aResponse;
  }

  private static void applyConceptValues (final TDEDataRequestSubjectType ds, final TDEDataElementRequestType aDER,
      final String sLogPrefix, final DCUIConfig.Dataset dataset) {

    final TDEConceptRequestType aFirstLevelConcept = aDER.getConceptRequest ();
    if (aFirstLevelConcept != null) {
      for (final TDEConceptRequestType aSecondLevelConcept : aFirstLevelConcept.getConceptRequest ()) {
        for (final TDEConceptRequestType aThirdLevelConcept : aSecondLevelConcept.getConceptRequest ()) {
          if (_canUseConcept (aThirdLevelConcept)) {
            _applyStaticDataset (ds, aThirdLevelConcept, sLogPrefix, dataset);
          } else {
            // 3 level nesting is maximum
            ToopKafkaClient.send (EErrorLevel.ERROR,
                () -> sLogPrefix + "A third level concept that is unusable - weird: " + aThirdLevelConcept);
          }
        }
      }
    }
  }

  public void onToopRequest (@Nonnull final ToopRequestWithAttachments140 aRequestWA) throws IOException {
    final TDETOOPRequestType aRequest = aRequestWA.getRequest();
    final ICommonsList<AsicReadEntry> attachments = aRequestWA.attachments ();

    final String sRequestID = aRequest.getDocumentUniversalUniqueIdentifier ().getValue ();
    final String sLogPrefix = "[" + sRequestID + "] ";
    ToopKafkaClient.send (EErrorLevel.INFO, () -> sLogPrefix + "Received DP Backend Request");

    dumpRequest (aRequest);

    // Record matching to dataset

    // Try to find dataset for natural person
    final TDEDataRequestSubjectType ds = aRequest.getDataRequestSubject ();
    String naturalPersonIdentifier = null;
    String legalEntityIdentifier = null;

    if (ds.getNaturalPerson () != null) {
      if (ds.getNaturalPerson ().getPersonIdentifier () != null) {
        if (!ds.getNaturalPerson ().getPersonIdentifier ().getValue ().isEmpty ()) {
          ToopKafkaClient.send (EErrorLevel.INFO, () -> sLogPrefix + "Record matching NaturalPerson: "
              + ds.getNaturalPerson ().getPersonIdentifier ().getValue ());
          naturalPersonIdentifier = ds.getNaturalPerson ().getPersonIdentifier ().getValue ();
        }
      }
    }

    if (ds.getLegalPerson () != null) {
      if (ds.getLegalPerson ().getLegalPersonUniqueIdentifier () != null) {
        if (!ds.getLegalPerson ().getLegalPersonUniqueIdentifier ().getValue ().isEmpty ()) {
          ToopKafkaClient.send (EErrorLevel.INFO, () -> sLogPrefix + "Record matching LegalPerson: "
              + ds.getLegalPerson ().getLegalPersonUniqueIdentifier ().getValue ());
          legalEntityIdentifier = ds.getLegalPerson ().getLegalPersonUniqueIdentifier ().getValue ();
        }
      }
    }

    // Get datasets from config
    final DCUIConfig dcuiConfig = new DCUIConfig ();

    final List<DCUIConfig.Dataset> datasets = dcuiConfig.getDatasetsByIdentifier (naturalPersonIdentifier,
        legalEntityIdentifier);

    DCUIConfig.Dataset dataset = null;
    if (datasets.size () > 0) {
      dataset = datasets.get (0);
      ToopKafkaClient.send (EErrorLevel.ERROR, () -> sLogPrefix + "Dataset found");
    } else {
      ToopKafkaClient.send (EErrorLevel.ERROR, () -> sLogPrefix + "No dataset found");
    }

    // build response
    final TDETOOPResponseType aResponse = _createResponseFromRequest (aRequest, sLogPrefix);
    aResponse.setSpecificationIdentifier (ToopXSDHelper140.createSpecificationIdentifierResponse ());


    // handle document request
    final List<AsicWriteEntry> documentEntries = new ArrayList<>();
    if (aResponse.getDocumentRequest().size() > 0) {
      final TDEDocumentRequestType documentRequestType = aResponse.getDocumentRequestAtIndex(0);

      if (documentRequestType != null) {

        ToopKafkaClient.send (EErrorLevel.INFO, () -> sLogPrefix + "Handling a document request");
        final TDEDocumentType tdeDocument = new TDEDocumentType();
        tdeDocument.setDocumentURI(ToopXSDHelper140.createIdentifier("file:/attachments/SeaWindDOC.pdf"));
        tdeDocument.setDocumentMimeTypeCode(ToopXSDHelper140.createCode("application/pdf"));
        tdeDocument.setDocumentTypeCode(documentRequestType.getDocumentRequestTypeCode());

        final TDEIssuerType issuerType = new TDEIssuerType();
        issuerType.setDocumentIssuerIdentifier(ToopXSDHelper140.createIdentifier("elonia", "toop-doctypeid-qns", "EE12345678"));
        issuerType.setDocumentIssuerName(ToopXSDHelper140.createText("EE-EMA"));

        final TDEDocumentResponseType documentResponseType = new TDEDocumentResponseType();
        documentResponseType.addDocument(tdeDocument);
        documentResponseType.setDocumentName(ToopXSDHelper140.createText("ISMCompliance"));
        documentResponseType.setDocumentDescription(ToopXSDHelper140.createText("Document of Compliance (DOC)"));
        documentResponseType.setDocumentIdentifier(ToopXSDHelper140.createIdentifier("077SM/16"));
        documentResponseType.setDocumentIssueDate(ToopXSDHelper140.createDateWithLOANow());
        documentResponseType.setDocumentIssuePlace(ToopXSDHelper140.createText("Pallen, Elonia"));
        documentResponseType.setDocumentIssuer(issuerType);
        documentResponseType.setLegalReference(ToopXSDHelper140.createText("SOLAS 1974"));
        documentResponseType.setDocumentRemarks(new ArrayList<>());
        documentResponseType.setErrorIndicator(ToopXSDHelper140.createIndicator(false));

        final List<TDEDocumentResponseType> documentResponses = new ArrayList<>();
        documentResponses.add(documentResponseType);

        documentRequestType.setDocumentResponse(documentResponses);

        final byte[] fakeDocument = "A document file...".getBytes();
        final AsicWriteEntry entry = new AsicWriteEntry("SeaWindDOC.pdf", fakeDocument, MimeTypeParser.parseMimeType("application/pdf"));
        documentEntries.add(entry);
      }
    }

    // add all the mapped values in the response
    for (final TDEDataElementRequestType aDER : aResponse.getDataElementRequest ()) {
      applyConceptValues (aRequest.getDataRequestSubject (), aDER, sLogPrefix, dataset);
    }

    // send back to toop-connector at /from-dp
    // The URL must be configured in toop-interface.properties file
    try {
      dumpResponse (aResponse);
      ToopInterfaceClient.sendResponseToToopConnector(aResponse,
              ToopInterfaceConfig.getToopConnectorDPUrl (),
              documentEntries);
    } catch (final ToopErrorException ex) {
      throw new RuntimeException (ex);
    }
  }

  public void onToopErrorResponse (@Nonnull final ToopResponseWithAttachments140 aResponseWA) throws IOException {
    final TDETOOPResponseType aResponse = aResponseWA.getResponse ();
    final ICommonsList<AsicReadEntry> attachments = aResponseWA.attachments ();

    final IdentifierType docUuid = aResponse.getDocumentUniversalUniqueIdentifier();
    final String sRequestID = (docUuid != null ? docUuid.getValue() : "");
    final String sLogPrefix = "[" + sRequestID + "] ";

    final StringBuilder sb = new StringBuilder();
    sb.append(sLogPrefix);
    sb.append("Received TOOP Error Response from TC.");

    sb.append(String.format(" Contains %d documents(s)\n", attachments.size()));

    if (aResponse.hasErrorEntries()) {
      sb.append(String.format(" Contains %d error(s)\n", aResponse.getErrorCount()));

      for (final TDEErrorType error : aResponse.getError()) {
        sb.append(error).append("\n");
      }
      ToopKafkaClient.send (EErrorLevel.ERROR, sb::toString);
    } else {
      ToopKafkaClient.send (EErrorLevel.WARN, sb::toString);
    }
  }

  private void dumpRequest (@Nonnull final TDETOOPRequestType aRequest) {
    try {

      final DateFormat dateFormat = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss.SSS");
      final String filePath = String.format ("%s/request-dump-%s.log", DCUIConfig.getDumpResponseDirectory (),
          dateFormat.format (new Date ()));

      final String requestXml = ToopWriter.request140 ().getAsString (aRequest);
      if (requestXml != null) {
        try (final FileWriter fw = new FileWriter (filePath)) {
          fw.write (requestXml);
        }
      }
    } catch (final IOException e) {
      e.printStackTrace ();
    }
  }

  private void dumpResponse (@Nonnull final TDETOOPResponseType aResponse) {

    try {

      final DateFormat dateFormat = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss.SSS");
      final String filePath = String.format ("%s/response-dump-%s.log", DCUIConfig.getDumpResponseDirectory (),
          dateFormat.format (new Date ()));

      final String responseXml = ToopWriter.response140 ().getAsString (aResponse);
      if (responseXml != null) {
        try (final FileWriter fw = new FileWriter (filePath)) {
          fw.write (responseXml);
        }
      }
    } catch (final IOException e) {
      e.printStackTrace ();
    }
  }
}
