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
package eu.toop.demoui;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigObject;

public final class DPDataset implements Serializable {

  private final String naturalPersonIdentifier;
  private final String legalPersonIdentifier;
  private final Map<String, String> concepts = new HashMap<>();

  public DPDataset(final Config conf) {

    naturalPersonIdentifier = conf.getString ("NaturalPerson.identifier");
    legalPersonIdentifier = conf.getString ("LegalPerson.identifier");

    for(final ConfigObject _concept : conf.getObjectList ("Concepts")) {
      final Config concept = _concept.toConfig();

      final String conceptName = concept.getString("name");
      final String conceptValue = concept.getString("value");

      concepts.put(conceptName, conceptValue);
    }
  }

  public String getNaturalPersonIdentifier () {

    return naturalPersonIdentifier;
  }

  public String getLegalPersonIdentifier () {

    return legalPersonIdentifier;
  }

  public Map<String, String> getConcepts () {

    return concepts;
  }

  public String getConceptValue(final String conceptName) {

    return concepts.get (conceptName);
  }
}