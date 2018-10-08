import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Map;

import org.junit.Test;

import eu.toop.demoui.DCUIConfig;

public class DCUIConfigTest {

  @Test
  public void testBasic() {

    final DCUIConfig dcuiConfig = new DCUIConfig ();

    assertEquals (dcuiConfig.getDatasets ().size (), 1);

    for (final DCUIConfig.Dataset dataset : dcuiConfig.getDatasets ()) {
      assertEquals (dataset.getNaturalPersonIdentifier (), "12345");
      assertEquals (dataset.getLegalPersonIdentifier (), "");
      assertEquals (dataset.getConcepts ().size(), 13);

      for (final Map.Entry<String, String> concept : dataset.getConcepts ().entrySet()) {

        assertNotNull (concept.getKey ());
        assertNotNull (concept.getValue ());
      }
    }
  }
}
