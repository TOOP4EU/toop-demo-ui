/**
 * Copyright (C) 2018-2020 toop.eu
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

import java.util.ArrayList;

import javax.annotation.Nonnull;

import org.apache.pdfbox.pdmodel.common.PDRectangle;

import com.helger.commons.collection.impl.CommonsArrayList;
import com.helger.commons.collection.impl.ICommonsList;
import com.helger.commons.datetime.PDTFactory;
import com.helger.commons.error.level.EErrorLevel;
import com.helger.commons.io.stream.NonBlockingByteArrayOutputStream;
import com.helger.commons.mime.CMimeType;
import com.helger.pdflayout4.PDFCreationException;
import com.helger.pdflayout4.PageLayoutPDF;
import com.helger.pdflayout4.base.EPLPlaceholder;
import com.helger.pdflayout4.base.PLPageSet;
import com.helger.pdflayout4.element.text.PLText;
import com.helger.pdflayout4.spec.EHorzAlignment;
import com.helger.pdflayout4.spec.FontSpec;
import com.helger.pdflayout4.spec.PreloadFont;

import eu.toop.commons.dataexchange.v140.TDEDocumentRequestType;
import eu.toop.commons.dataexchange.v140.TDEDocumentResponseType;
import eu.toop.commons.dataexchange.v140.TDEDocumentType;
import eu.toop.commons.dataexchange.v140.TDEIssuerType;
import eu.toop.commons.dataexchange.v140.TDETOOPRequestType;
import eu.toop.commons.dataexchange.v140.TDETOOPResponseType;
import eu.toop.commons.exchange.AsicWriteEntry;
import eu.toop.commons.jaxb.ToopXSDHelper140;
import eu.toop.kafkaclient.ToopKafkaClient;

final class HandlerDocumentRequestPDF
{
  private HandlerDocumentRequestPDF ()
  {}

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

  public static void handle (@Nonnull final String sLogPrefix,
                             @Nonnull final TDETOOPRequestType aRequest,
                             @Nonnull final TDETOOPResponseType aResponse,
                             @Nonnull final ICommonsList <AsicWriteEntry> aDocumentEntries)
  {
    final TDEDocumentRequestType documentRequestType = aResponse.getDocumentRequestAtIndex (0);
    if (documentRequestType != null)
    {
      ToopKafkaClient.send (EErrorLevel.INFO, () -> sLogPrefix + "Handling a document request/PDF");

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
      aDocumentEntries.add (entry);
    }
  }
}
