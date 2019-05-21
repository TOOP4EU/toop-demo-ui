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

import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.commons.datetime.PDTFactory;
import com.helger.commons.io.file.FileOperationManager;
import com.helger.commons.string.StringHelper;
import com.helger.datetime.util.PDTIOHelper;

import eu.toop.commons.dataexchange.v140.TDETOOPRequestType;
import eu.toop.commons.dataexchange.v140.TDETOOPResponseType;
import eu.toop.commons.jaxb.ToopWriter;
import eu.toop.demoui.DCUIConfig;

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

}
