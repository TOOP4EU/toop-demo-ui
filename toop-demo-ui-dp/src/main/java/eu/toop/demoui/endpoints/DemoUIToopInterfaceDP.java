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
import java.util.ArrayList;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.pdfbox.pdmodel.common.PDRectangle;

import com.helger.commons.annotation.Nonempty;
import com.helger.commons.collection.impl.CommonsArrayList;
import com.helger.commons.collection.impl.ICommonsList;
import com.helger.commons.datetime.PDTFactory;
import com.helger.commons.error.level.EErrorLevel;
import com.helger.commons.io.stream.NonBlockingByteArrayOutputStream;
import com.helger.commons.mime.CMimeType;
import com.helger.commons.string.StringHelper;
import com.helger.pdflayout4.PDFCreationException;
import com.helger.pdflayout4.PageLayoutPDF;
import com.helger.pdflayout4.base.EPLPlaceholder;
import com.helger.pdflayout4.base.PLPageSet;
import com.helger.pdflayout4.element.text.PLText;
import com.helger.pdflayout4.spec.EHorzAlignment;
import com.helger.pdflayout4.spec.FontSpec;
import com.helger.pdflayout4.spec.PreloadFont;

import eu.toop.commons.codelist.EPredefinedDocumentTypeIdentifier;
import eu.toop.commons.concept.EConceptType;
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
import eu.toop.commons.error.EToopErrorCode;
import eu.toop.commons.error.ToopErrorException;
import eu.toop.commons.exchange.AsicReadEntry;
import eu.toop.commons.exchange.AsicWriteEntry;
import eu.toop.commons.exchange.ToopMessageBuilder140;
import eu.toop.commons.exchange.ToopRequestWithAttachments140;
import eu.toop.commons.exchange.ToopResponseWithAttachments140;
import eu.toop.commons.jaxb.ToopXSDHelper140;
import eu.toop.commons.usecase.ReverseDocumentTypeMapping;
import eu.toop.demoui.DPDataset;
import eu.toop.demoui.DPUIConfig;
import eu.toop.demoui.DPUIDatasets;
import eu.toop.iface.IToopInterfaceDP;
import eu.toop.iface.ToopInterfaceClient;
import eu.toop.iface.ToopInterfaceConfig;
import eu.toop.kafkaclient.ToopKafkaClient;
import oasis.names.specification.ubl.schema.xsd.unqualifieddatatypes_21.IdentifierType;
import oasis.names.specification.ubl.schema.xsd.unqualifieddatatypes_21.TextType;

public final class DemoUIToopInterfaceDP implements IToopInterfaceDP
{
  private static boolean _canUseConcept (@Nonnull final TDEConceptRequestType aConcept)
  {
    // This class can only deliver to "DP" concept types without child entries
    return EConceptType.DP.getID ().equals (aConcept.getConceptTypeCode ().getValue ()) &&
           aConcept.hasNoConceptRequestEntries ();
  }

  private static void _setError (@Nonnull final String sLogPrefix,
                                 @Nonnull final TDEDataElementResponseValueType aValue,
                                 @Nonnull @Nonempty final String sErrorMsg)
  {
    aValue.setErrorIndicator (ToopXSDHelper140.createIndicator (true));
    // Either error code or description
    if (false)
      aValue.setErrorCode (ToopXSDHelper140.createCode (EToopErrorCode.GEN.getID ()));
    else
      aValue.setResponseDescription (ToopXSDHelper140.createText ("MockError from DemoDP: " + sErrorMsg));
    ToopKafkaClient.send (EErrorLevel.ERROR, () -> sLogPrefix + "MockError from DemoDP: " + sErrorMsg);
  }

  private static void _applyStaticDataset (@Nonnull final String sLogPrefix,
                                           @Nonnull final TDEConceptRequestType aConcept,
                                           @Nullable final DPDataset aDataset)
  {
    final TDEDataElementResponseValueType aValue = new TDEDataElementResponseValueType ();
    aConcept.addDataElementResponseValue (aValue);

    aValue.setErrorIndicator (ToopXSDHelper140.createIndicator (false));
    aValue.setAlternativeResponseIndicator (ToopXSDHelper140.createIndicator (false));

    final TextType conceptName = aConcept.getConceptName ();
    if (conceptName == null || StringHelper.hasNoText (conceptName.getValue ()))
    {
      _setError (sLogPrefix, aValue, "Concept name is missing in request: " + aConcept);
    }
    else
    {
      if (aDataset == null)
      {
        _setError (sLogPrefix, aValue, "No DP dataset found");
      }
      else
      {
        final String sConceptName = conceptName.getValue ();
        final String sConceptValue = aDataset.getConceptValue (sConceptName);
        if (sConceptValue == null)
        {
          _setError (sLogPrefix, aValue, "Concept [" + sConceptName + "] is missing in DP dataset");
        }
        else
        {
          aValue.setResponseDescription (ToopXSDHelper140.createText (sConceptValue));

          ToopKafkaClient.send (EErrorLevel.INFO,
                                () -> sLogPrefix + "Populated concept [" + sConceptName + "]: [" + sConceptValue + "]");
        }
      }
    }
  }

  @Nonnull
  private static TDETOOPResponseType _createResponseFromRequest (@Nonnull final TDETOOPRequestType aRequest,
                                                                 @Nonnull final String sLogPrefix)
  {
    // build response
    final TDETOOPResponseType aResponse = ToopMessageBuilder140.createResponse (aRequest);
    {
      // Required for response
      aResponse.getRoutingInformation ()
               .setDataProviderElectronicAddressIdentifier (ToopXSDHelper140.createIdentifier ("elonia@register.example.org"));

      final TDEDataProviderType p = new TDEDataProviderType ();
      p.setDPIdentifier (ToopXSDHelper140.createIdentifier ("demo-agency",
                                                            DPUIConfig.getResponderIdentifierScheme (),
                                                            DPUIConfig.getResponderIdentifierValue ()));
      p.setDPName (ToopXSDHelper140.createText ("EloniaDP"));
      final TDEAddressType pa = new TDEAddressType ();
      pa.setCountryCode (ToopXSDHelper140.createCodeWithLOA (DPUIConfig.getProviderCountryCode ()));
      p.setDPLegalAddress (pa);
      aResponse.addDataProvider (p);
    }

    // Document type must be switch from request to response
    final IdentifierType aDocTypeID = aRequest.getRoutingInformation ().getDocumentTypeIdentifier ();
    final EPredefinedDocumentTypeIdentifier eRequestDocType = EPredefinedDocumentTypeIdentifier.getFromDocumentTypeIdentifierOrNull (aDocTypeID.getSchemeID (),
                                                                                                                                     aDocTypeID.getValue ());
    if (eRequestDocType != null)
    {
      final EPredefinedDocumentTypeIdentifier eResponseDocType = ReverseDocumentTypeMapping.getReverseDocumentTypeOrNull (eRequestDocType);
      if (eResponseDocType == null)
      {
        // Found no reverse document type
        ToopKafkaClient.send (EErrorLevel.ERROR,
                              () -> sLogPrefix +
                                    "Found no response document type for '" +
                                    aDocTypeID.getSchemeID () +
                                    "::" +
                                    aDocTypeID.getValue () +
                                    "'");
      }
      else
      {
        // Set new doc type in response
        ToopKafkaClient.send (EErrorLevel.INFO,
                              () -> sLogPrefix +
                                    "Switching document type '" +
                                    eRequestDocType.getURIEncoded () +
                                    "' to '" +
                                    eResponseDocType.getURIEncoded () +
                                    "'");
        aResponse.getRoutingInformation ()
                 .setDocumentTypeIdentifier (ToopXSDHelper140.createIdentifier (eResponseDocType.getScheme (),
                                                                                eResponseDocType.getID ()));
      }
    }
    return aResponse;
  }

  private static void _applyConceptValues (@Nonnull final TDEDataElementRequestType aDER,
                                           final String sLogPrefix,
                                           final DPDataset dataset)
  {
    final TDEConceptRequestType aFirstLevelConcept = aDER.getConceptRequest ();
    if (aFirstLevelConcept != null)
    {
      boolean bDidApplyResponse = false;
      if (_canUseConcept (aFirstLevelConcept))
      {
        // Apply on first level - highly unlikely but who knows....
        _applyStaticDataset (sLogPrefix, aFirstLevelConcept, dataset);
        bDidApplyResponse = true;
      }
      else
        second: for (final TDEConceptRequestType aSecondLevelConcept : aFirstLevelConcept.getConceptRequest ())
        {
          if (_canUseConcept (aSecondLevelConcept))
          {
            // Apply on second level - used if directly started with TC concepts
            _applyStaticDataset (sLogPrefix, aSecondLevelConcept, dataset);
            bDidApplyResponse = true;
            break second;
          }
          for (final TDEConceptRequestType aThirdLevelConcept : aSecondLevelConcept.getConceptRequest ())
          {
            if (_canUseConcept (aThirdLevelConcept))
            {
              // Apply on third level
              _applyStaticDataset (sLogPrefix, aThirdLevelConcept, dataset);
              bDidApplyResponse = true;
              break second;
            }
            // 3 level nesting is maximum
          }
        }

      if (!bDidApplyResponse)
      {
        ToopKafkaClient.send (EErrorLevel.ERROR, () -> sLogPrefix + "Found no place to provide response value in Data");
      }
    }
  }

  private static byte [] _createFakePDF (@Nonnull final TDETOOPRequestType aRequest)
  {
    try (final NonBlockingByteArrayOutputStream aBAOS = new NonBlockingByteArrayOutputStream ())
    {
      final FontSpec r10 = new FontSpec (PreloadFont.REGULAR, 10);
      final FontSpec r10b = new FontSpec (PreloadFont.REGULAR_BOLD, 10);
      final PLPageSet aPS1 = new PLPageSet (PDRectangle.A4).setMargin (30);

      aPS1.setPageHeader (new PLText ("Demo document created by TOOP Demo UI",
                                      r10).setHorzAlign (EHorzAlignment.CENTER));
      aPS1.setPageFooter (new PLText ("Page " +
                                      EPLPlaceholder.PAGESET_PAGE_NUMBER.getVariable () +
                                      " of " +
                                      EPLPlaceholder.PAGESET_PAGE_COUNT.getVariable (),
                                      r10b).setReplacePlaceholder (true).setHorzAlign (EHorzAlignment.RIGHT));
      aPS1.addElement (new PLText ("This is the response to the request with UUID " +
                                   aRequest.getDocumentUniversalUniqueIdentifier ().getValue (),
                                   r10));
      aPS1.addElement (new PLText ("This dummy document was created at " +
                                   PDTFactory.getCurrentLocalDateTime ().toString (),
                                   r10));

      final PageLayoutPDF aPageLayout = new PageLayoutPDF ();
      aPageLayout.addPageSet (aPS1);
      aPageLayout.renderTo (aBAOS);
      return aBAOS.getBufferOrCopy ();
    }
    catch (final PDFCreationException ex)
    {
      throw new RuntimeException (ex);
    }
  }

  public void onToopRequest (@Nonnull final ToopRequestWithAttachments140 aRequestWA) throws IOException
  {
    final TDETOOPRequestType aRequest = aRequestWA.getRequest ();
    final ICommonsList <AsicReadEntry> attachments = aRequestWA.attachments ();

    final String sRequestID = aRequest.getDocumentUniversalUniqueIdentifier ().getValue ();
    final String sLogPrefix = "[" + sRequestID + "] ";
    ToopKafkaClient.send (EErrorLevel.INFO,
                          () -> sLogPrefix +
                                "Received DP Backend Request" +
                                (attachments.isEmpty () ? "" : " with " + attachments.size () + " attachment(s)"));

    DemoUIToopInterfaceHelper.dumpRequest (aRequest);

    // Record matching to dataset

    // Try to find dataset for natural person
    final TDEDataRequestSubjectType ds = aRequest.getDataRequestSubject ();
    final String naturalPersonIdentifier;
    final String legalEntityIdentifier;

    if (ds.getNaturalPerson () != null &&
        ds.getNaturalPerson ().getPersonIdentifier () != null &&
        StringHelper.hasText (ds.getNaturalPerson ().getPersonIdentifier ().getValue ()))
    {
      naturalPersonIdentifier = ds.getNaturalPerson ().getPersonIdentifier ().getValue ();
      ToopKafkaClient.send (EErrorLevel.INFO,
                            () -> sLogPrefix + "Record matching NaturalPerson: " + naturalPersonIdentifier);
    }
    else
      naturalPersonIdentifier = null;

    if (ds.getLegalPerson () != null &&
        ds.getLegalPerson ().getLegalPersonUniqueIdentifier () != null &&
        StringHelper.hasText (ds.getLegalPerson ().getLegalPersonUniqueIdentifier ().getValue ()))
    {
      legalEntityIdentifier = ds.getLegalPerson ().getLegalPersonUniqueIdentifier ().getValue ();
      ToopKafkaClient.send (EErrorLevel.INFO,
                            () -> sLogPrefix + "Record matching LegalPerson: " + legalEntityIdentifier);
    }
    else
      legalEntityIdentifier = null;

    // Get datasets from config
    final DPUIDatasets dpDatasets = DPUIDatasets.INSTANCE;

    final ICommonsList <DPDataset> datasets = dpDatasets.getDatasetsByIdentifier (naturalPersonIdentifier,
                                                                                  legalEntityIdentifier);

    final DPDataset dataset = datasets.getFirst ();
    if (dataset != null)
      ToopKafkaClient.send (EErrorLevel.ERROR, () -> sLogPrefix + "Dataset found");
    else
      ToopKafkaClient.send (EErrorLevel.ERROR, () -> sLogPrefix + "No dataset found");

    // build response
    final TDETOOPResponseType aResponse = _createResponseFromRequest (aRequest, sLogPrefix);
    aResponse.setSpecificationIdentifier (ToopXSDHelper140.createSpecificationIdentifierResponse ());

    // handle document request
    final ICommonsList <AsicWriteEntry> documentEntries = new CommonsArrayList <> ();
    if (aResponse.hasDocumentRequestEntries ())
    {
      final TDEDocumentRequestType documentRequestType = aResponse.getDocumentRequestAtIndex (0);
      if (documentRequestType != null)
      {
        ToopKafkaClient.send (EErrorLevel.INFO, () -> sLogPrefix + "Handling a document request");

        final String sPDFName = "SeaWindDOC.pdf";

        final TDEDocumentType tdeDocument = new TDEDocumentType ();
        tdeDocument.setDocumentURI (ToopXSDHelper140.createIdentifier ("file:/attachments/" + sPDFName));
        tdeDocument.setDocumentMimeTypeCode (ToopXSDHelper140.createCode (CMimeType.APPLICATION_PDF.getAsString ()));
        tdeDocument.setDocumentTypeCode (documentRequestType.getDocumentRequestTypeCode ());

        final TDEIssuerType issuerType = new TDEIssuerType ();
        issuerType.setDocumentIssuerIdentifier (ToopXSDHelper140.createIdentifier ("elonia",
                                                                                   "toop-doctypeid-qns",
                                                                                   "EE12345678"));
        issuerType.setDocumentIssuerName (ToopXSDHelper140.createText ("EE-EMA"));

        final TDEDocumentResponseType documentResponseType = new TDEDocumentResponseType ();
        documentResponseType.addDocument (tdeDocument);
        documentResponseType.setDocumentName (ToopXSDHelper140.createText ("ISMCompliance"));
        documentResponseType.setDocumentDescription (ToopXSDHelper140.createText ("Document of Compliance (DOC)"));
        documentResponseType.setDocumentIdentifier (ToopXSDHelper140.createIdentifier ("077SM/16"));
        documentResponseType.setDocumentIssueDate (ToopXSDHelper140.createDateWithLOANow ());
        documentResponseType.setDocumentIssuePlace (ToopXSDHelper140.createText ("Pallen, Elonia"));
        documentResponseType.setDocumentIssuer (issuerType);
        documentResponseType.setLegalReference (ToopXSDHelper140.createText ("SOLAS 1974"));
        documentResponseType.setDocumentRemarks (new ArrayList <> ());
        documentResponseType.setErrorIndicator (ToopXSDHelper140.createIndicator (false));

        documentRequestType.setDocumentResponse (new CommonsArrayList <> (documentResponseType));

        // Dynamically create PDF
        final byte [] fakeDocument = _createFakePDF (aRequest);
        final AsicWriteEntry entry = new AsicWriteEntry (sPDFName, fakeDocument, CMimeType.APPLICATION_PDF);
        documentEntries.add (entry);
      }
    }

    // add all the mapped values in the response
    for (final TDEDataElementRequestType aDER : aResponse.getDataElementRequest ())
    {
      _applyConceptValues (aDER, sLogPrefix, dataset);
    }

    // send back to toop-connector at /from-dp
    // The URL must be configured in toop-interface.properties file
    try
    {
      DemoUIToopInterfaceHelper.dumpResponse (aResponse);
      // Signing happpens internally
      ToopInterfaceClient.sendResponseToToopConnector (aResponse,
                                                       ToopInterfaceConfig.getToopConnectorDPUrl (),
                                                       documentEntries);
    }
    catch (final ToopErrorException ex)
    {
      throw new RuntimeException (ex);
    }
  }

  public void onToopErrorResponse (@Nonnull final ToopResponseWithAttachments140 aResponseWA) throws IOException
  {
    final TDETOOPResponseType aResponse = aResponseWA.getResponse ();
    final ICommonsList <AsicReadEntry> attachments = aResponseWA.attachments ();

    DemoUIToopInterfaceHelper.dumpResponse (aResponse);

    final IdentifierType docUuid = aResponse.getDocumentUniversalUniqueIdentifier ();
    final String sRequestID = (docUuid != null ? docUuid.getValue () : "");
    final String sLogPrefix = "[" + sRequestID + "] ";

    final StringBuilder sb = new StringBuilder ();
    sb.append (sLogPrefix);
    sb.append ("Received TOOP Error Response from TC.");
    sb.append (" Contains ").append (attachments.size ()).append (" documents(s)\n");

    if (aResponse.hasErrorEntries ())
    {
      sb.append (" Contains ").append (aResponse.getErrorCount ()).append (" error(s)\n");
      for (final TDEErrorType error : aResponse.getError ())
        sb.append (error).append ("\n");
      ToopKafkaClient.send (EErrorLevel.ERROR, sb::toString);
    }
    else
    {
      // Warnings only
      ToopKafkaClient.send (EErrorLevel.WARN, sb::toString);
    }
  }
}
