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
package eu.toop.demoui.datasets;

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
import static org.junit.Assert.assertNull;

import java.util.Map;

import org.junit.Test;

import eu.toop.demoui.datasets.DPDataset;
import eu.toop.demoui.datasets.DPUIDatasets;

public class DPUIDatasetsTest
{
  @Test
  public void testBasic ()
  {
    final DPUIDatasets aDSList = DPUIDatasets.INSTANCE;

    assertEquals (aDSList.getDatasets ().size (), 1);
    final DPDataset dataset = aDSList.getDatasets ().get (0);

    assertEquals (dataset.getNaturalPersonIdentifier (), "12345");
    assertNull (dataset.getLegalPersonIdentifier ());
    assertEquals (dataset.getConcepts ().size (), 13);

    for (final Map.Entry <String, String> concept : dataset.getConcepts ().entrySet ())
    {
      assertNotNull (concept.getKey ());
      assertNotNull (concept.getValue ());
    }
  }

  @Test
  public void testGetDatasetsByIdentifier ()
  {
    final DPUIDatasets aDSList = DPUIDatasets.INSTANCE;

    assertEquals (1, aDSList.getDatasetsByIdentifier ("AT/BE/12345", "").size ());
    assertEquals (1, aDSList.getDatasetsByIdentifier ("AT/BE/12345", null).size ());
    // LP is invalid -> null
    assertEquals (1, aDSList.getDatasetsByIdentifier ("AT/BE/12345", "LP1").size ());
    // LP is valid, but not mapped
    assertEquals (0, aDSList.getDatasetsByIdentifier ("AT/BE/12345", "AT/BE/LP1").size ());
    // LP is invalid -> null
    // No such ID in list
    assertEquals (0, aDSList.getDatasetsByIdentifier ("AT/BE/bla", "LP1").size ());

    assertEquals (0, aDSList.getDatasetsByIdentifier ("bla", "").size ());
    assertEquals (0, aDSList.getDatasetsByIdentifier ("bla", null).size ());
    assertEquals (0, aDSList.getDatasetsByIdentifier ("AT/BE/", "LP1").size ());
    assertEquals (0, aDSList.getDatasetsByIdentifier (null, "LP1").size ());
    assertEquals (0, aDSList.getDatasetsByIdentifier ("", "").size ());
    assertEquals (0, aDSList.getDatasetsByIdentifier ("", null).size ());
    assertEquals (0, aDSList.getDatasetsByIdentifier (null, "").size ());
    assertEquals (0, aDSList.getDatasetsByIdentifier (null, null).size ());
  }

  @SuppressWarnings ("unused")
  @Test
  public void testReadAllPredefined ()
  {
    // Just ensure they can be read
    new DPUIDatasets ("datasets.xml");
    new DPUIDatasets ("datasets.elonia.xml");
    new DPUIDatasets ("datasets.template.xml");
  }
}
