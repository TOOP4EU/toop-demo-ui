/**
 * Copyright (C) 2018 toop.eu
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigObject;
import com.typesafe.config.ConfigParseOptions;
import com.typesafe.config.ConfigSyntax;

public class DCUIConfig {

  private final List<Dataset> datasets = new ArrayList<> ();

  public DCUIConfig() {

    ConfigParseOptions opt = ConfigParseOptions.defaults();
    opt.setSyntax(ConfigSyntax.CONF);

    Config conf = ConfigFactory.parseResources("dataset.conf").resolve();

    for (ConfigObject _dataset : conf.getObjectList("Datasets")) {
      Config dataset = _dataset.toConfig();

      datasets.add(new Dataset (dataset));
    }
  }

  public List<Dataset> getDatasets () {

    return datasets;
  }

  public List<Dataset> getDatasetsByIdentifier(String naturalPersonIdentifier, String legalPersonIdentifier) {

    List<Dataset> _datasets = new ArrayList<> ();
    for (Dataset dataset : datasets) {

      if (dataset.getNaturalPersonIdentifier ().equals (stripCodesFromIdentifier(naturalPersonIdentifier)) ||
          dataset.getLegalPersonIdentifier ().equals (stripCodesFromIdentifier(legalPersonIdentifier))) {
        _datasets.add (dataset);
      }
    }

    return _datasets;
  }

  public List<Dataset> getDatasetsByNaturalPersonIdentifier(String naturalPersonIdentifier) {

    List<Dataset> _datasets = new ArrayList<> ();
    for (Dataset dataset : datasets) {

      if (dataset.getNaturalPersonIdentifier ().equals (stripCodesFromIdentifier(naturalPersonIdentifier))) {
        _datasets.add (dataset);
      }
    }

    return _datasets;
  }

  public List<Dataset> getDatasetsByLegalPersonIdentifier(String legalPersonIdentifier) {

    List<Dataset> _datasets = new ArrayList<> ();
    for (Dataset dataset : datasets) {

      if (dataset.getLegalPersonIdentifier ().equals (stripCodesFromIdentifier(legalPersonIdentifier))) {
        _datasets.add (dataset);
      }
    }

    return _datasets;
  }

  private boolean isValidIdentifier(String identifier) {
    return Pattern.matches("([A-Z][A-Z]\\/){2}.{1,}", identifier);
  }

  private String stripCodesFromIdentifier(String identifier) {

    if (!isValidIdentifier(identifier)) {
      return null;
    }

    return identifier.substring (6);
  }

  public class Dataset {

    private final String naturalPersonIdentifier;
    private final String legalPersonIdentifier;
    private final Map<String, String> concepts = new HashMap<>();

    public Dataset(Config conf) {

      naturalPersonIdentifier = conf.getString ("NaturalPerson.identifier");
      legalPersonIdentifier = conf.getString ("LegalPerson.identifier");

      for(ConfigObject _concept : conf.getObjectList ("Concepts")) {
        Config concept = _concept.toConfig();

        String conceptName = concept.getString("name");
        String conceptValue = concept.getString("value");

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

    public String getConceptValue(String conceptName) {

      return concepts.getOrDefault (conceptName, null);
    }
  }
}