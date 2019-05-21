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
import eu.toop.demoui.DPUIConfig;

public final class DemoUIToopInterfaceHelper {
  private static final Logger LOGGER = LoggerFactory.getLogger (DemoUIToopInterfaceHelper.class);

  private DemoUIToopInterfaceHelper () {}

  public static void dumpRequest (@Nonnull final TDETOOPRequestType aRequest) {
    final LocalDateTime aNow = PDTFactory.getCurrentLocalDateTime ();
    // Never use ":" in filenames (Windows...)
    final String filePath = DPUIConfig.getDumpRequestDirectory () + "/" + aNow.getYear () + "/"
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
    final String filePath = DPUIConfig.getDumpResponseDirectory () + "/" + aNow.getYear () + "/"
                            + StringHelper.getLeadingZero (aNow.getMonthValue (), 2) + "/"
                            + StringHelper.getLeadingZero (aNow.getDayOfMonth (), 2) + "/" + "response-dump-"
                            + PDTIOHelper.getTimeForFilename (aNow.toLocalTime ()) + ".log";
    final File f = new File (filePath);
    FileOperationManager.INSTANCE.createDirRecursiveIfNotExisting (f.getParentFile ());
    if (ToopWriter.response140 ().write (aResponse, f).isFailure ())
      LOGGER.error ("Failed to write response to '" + filePath + "'");
  }

}
