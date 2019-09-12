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

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.commons.datetime.PDTFactory;
import com.helger.commons.io.file.FileOperationManager;
import com.helger.commons.string.StringHelper;
import com.helger.datetime.util.PDTIOHelper;

import eu.toop.commons.dataexchange.v140.TDEDocumentResponseType;
import eu.toop.commons.dataexchange.v140.TDEDocumentType;
import eu.toop.commons.dataexchange.v140.TDETOOPRequestType;
import eu.toop.commons.dataexchange.v140.TDETOOPResponseType;
import eu.toop.commons.jaxb.ToopWriter;
import eu.toop.demoui.DCUIConfig;
import eu.toop.demoui.bean.DocumentDataBean;

public final class DemoUIToopInterfaceHelper {
  private static final Logger LOGGER = LoggerFactory.getLogger (DemoUIToopInterfaceHelper.class);

  private DemoUIToopInterfaceHelper () {}

  public static void dumpRequest (@Nonnull final TDETOOPRequestType aRequest) {
    final LocalDateTime aNow = PDTFactory.getCurrentLocalDateTime ();
    // Never use ":" in filenames (Windows...)
    final String filePath = DCUIConfig.getDumpRequestDirectory () + "/" + aNow.getYear () + "/"
                            + StringHelper.getLeadingZero (aNow.getMonthValue (), 2) + "/"
                            + StringHelper.getLeadingZero (aNow.getDayOfMonth (), 2) + "/" + "request-dump-"
                            + PDTIOHelper.getTimeForFilename (aNow.toLocalTime ()) + ".log";
    final File f = new File (filePath);
    FileOperationManager.INSTANCE.createDirRecursiveIfNotExisting (f.getParentFile ());
    if (ToopWriter.request140 ().write (aRequest, f).isFailure ())
      LOGGER.error ("Failed to write request to '" + filePath + "'");
  }

  public static void dumpResponse (@Nonnull final TDETOOPResponseType aResponse) {
    final LocalDateTime aNow = PDTFactory.getCurrentLocalDateTime ();
    final String filePath = DCUIConfig.getDumpResponseDirectory () + "/" + aNow.getYear () + "/"
                            + StringHelper.getLeadingZero (aNow.getMonthValue (), 2) + "/"
                            + StringHelper.getLeadingZero (aNow.getDayOfMonth (), 2) + "/" + "response-dump-"
                            + PDTIOHelper.getTimeForFilename (aNow.toLocalTime ()) + ".log";
    final File f = new File (filePath);
    FileOperationManager.INSTANCE.createDirRecursiveIfNotExisting (f.getParentFile ());
    if (ToopWriter.response140 ().write (aResponse, f).isFailure ())
      LOGGER.error ("Failed to write response to '" + filePath + "'");
  }

  public static List<TDEDocumentResponseType> getDocumentResponseDataList(TDETOOPResponseType aResponse) {
    final List<TDEDocumentResponseType> docResponseList = new ArrayList<>();

    if (aResponse.getDocumentRequestCount () > 0) {
      aResponse.getDocumentRequest().forEach( dRec -> {
        dRec.getDocumentResponse().forEach(dResp -> {
          docResponseList.add(dResp);
        });
      });
    }
    return docResponseList;
  }

  public static List<DocumentDataBean> getDocumentResponseDataBeanList(TDETOOPResponseType aResponse) {
    final List<DocumentDataBean> docResponseList = new ArrayList<>();
//    DocumentDataBean doc;

    if (aResponse.getDocumentRequestCount () > 0) {
      aResponse.getDocumentRequest().forEach( dRec -> {
        dRec.getDocumentResponse().forEach(dResp -> {
          DocumentDataBean doc = new DocumentDataBean();
          doc.setDocumentDescription(dResp.getDocumentDescription().getValue());
          doc.setDocumentName(dResp.getDocumentName().getValue());
          doc.setDocumentIdentifier(dResp.getDocumentIdentifier().getValue());
          doc.setDocumentIssueDate(dResp.getDocumentIssueDate().getValue().toString());
          doc.setDocumentIssuePlace(dResp.getDocumentIssuePlace().getValue());
          doc.setLegalReference(dResp.getLegalReference().getValue());
          doc.setDocumentURI(dResp.getDocument().get(0).getDocumentURI().getValue());
          doc.setDocumentMIMEType(dResp.getDocument().get(0).getDocumentMimeTypeCode().getValue());
          docResponseList.add(doc);
        });
      });
    }
    return docResponseList;
  }

  public static List<TDEDocumentType> getDocumentDataList(TDETOOPResponseType aResponse) {
    final List<TDEDocumentType> docList = new ArrayList<>();

    if (aResponse.getDocumentRequestCount () > 0) {
      aResponse.getDocumentRequest().forEach( dRec -> {
        dRec.getDocumentResponse().forEach(dResp -> {
          dResp.getDocument().forEach(doc -> {
            docList.add(doc);
          });
        });
      });
    }
    return docList;
  }



}
