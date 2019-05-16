package eu.toop.demoui.endpoints;

import java.io.File;

import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.commons.annotation.Nonempty;
import com.helger.commons.datetime.PDTFactory;
import com.helger.commons.datetime.PDTToString;
import com.helger.commons.io.file.FileOperationManager;

import eu.toop.commons.dataexchange.v140.TDETOOPRequestType;
import eu.toop.commons.dataexchange.v140.TDETOOPResponseType;
import eu.toop.commons.jaxb.ToopWriter;
import eu.toop.demoui.DCUIConfig;

public final class DemoUIToopInterfaceHelper {
  private static final Logger LOGGER = LoggerFactory.getLogger (DemoUIToopInterfaceHelper.class);

  private DemoUIToopInterfaceHelper () {}

  @Nonnull
  @Nonempty
  private static String _getCurrentDateTimeForFilename () {
    // Never use ":" in filenames (Windows...)
    return PDTToString.getAsString ("uuuu-MM-dd-HH-mm-ss", PDTFactory.getCurrentLocalDateTime ());
  }

  public static void dumpRequest (@Nonnull final TDETOOPRequestType aRequest) {
    final String filePath = DCUIConfig.getDumpRequestDirectory () + "/request-dump-" + _getCurrentDateTimeForFilename ()
                            + ".log";
    final File f = new File (filePath);
    FileOperationManager.INSTANCE.createDirRecursiveIfNotExisting (f.getParentFile ());
    if (ToopWriter.request140 ().write (aRequest, f).isFailure ())
      LOGGER.error ("Failed to write request to '" + filePath + "'");
  }

  public static void dumpResponse (@Nonnull final TDETOOPResponseType aResponse) {
    final String filePath = DCUIConfig.getDumpResponseDirectory () + "/response-dump-"
                            + _getCurrentDateTimeForFilename () + ".log";
    final File f = new File (filePath);
    FileOperationManager.INSTANCE.createDirRecursiveIfNotExisting (f.getParentFile ());
    if (ToopWriter.response140 ().write (aResponse, f).isFailure ())
      LOGGER.error ("Failed to write response to '" + filePath + "'");
  }

}
