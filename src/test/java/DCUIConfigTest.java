import eu.toop.demoui.DCUIConfig;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class DCUIConfigTest {

  @Test
  public void testBasic() {

    DCUIConfig dcuiConfig = new DCUIConfig ();

    assertEquals (dcuiConfig.getDatasets ().size (), 1);

    for (DCUIConfig.Dataset dataset : dcuiConfig.getDatasets ()) {
      assertEquals (dataset.getNaturalPersonIdentifier (), "12345");
      assertEquals (dataset.getLegalPersonIdentifier (), "");
      assertEquals (dataset.getConcepts ().size(), 13);

      for (Map.Entry<String, String> concept : dataset.getConcepts ().entrySet()) {

        assertNotNull (concept.getKey ());
        assertNotNull (concept.getValue ());
      }
    }
  }
}
