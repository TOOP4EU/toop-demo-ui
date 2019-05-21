package eu.toop.demoui;
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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Map;

import org.junit.Test;

public class DPUIDatasetsTest {

  @Test
  public void testBasic() {

    final DPUIDatasets dpDS = new DPUIDatasets ();

    assertEquals (dpDS.getDatasets ().size (), 1);

    for (final DPDataset dataset : dpDS.getDatasets ()) {
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
