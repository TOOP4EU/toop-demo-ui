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

import java.util.*;
import java.util.regex.Pattern;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigObject;
import com.typesafe.config.ConfigParseOptions;
import com.typesafe.config.ConfigSyntax;

public class DCUIConfig {

  private static ResourceBundle rb = ResourceBundle.getBundle("dcui");
  private final List<Dataset> datasets = new ArrayList<> ();
  private final List<String> countryCodes = new ArrayList<>();

  public DCUIConfig() {

    ConfigParseOptions opt = ConfigParseOptions.defaults();
    opt.setSyntax(ConfigSyntax.CONF);

    Config conf = ConfigFactory.parseResources("dataset.conf").resolve();

    for (ConfigObject _dataset : conf.getObjectList("Datasets")) {
      Config dataset = _dataset.toConfig();

      datasets.add(new Dataset (dataset));
    }
  }

  public static String getEidModuleURL () {
    return rb.getString("toop.eidmodule.url");
  }

  public static String getConceptNamespace() {
    return rb.getString("toop.concept.namespace");
  }

  public static String getDestinationCountryCode () {

    return rb.getString ("destination.country.code");
  }

  public static String getSenderIdentifierScheme () {

    return rb.getString ("sender.identifier.scheme");
  }

  public static String getSenderIdentifierValue () {

    return rb.getString ("sender.identifier.value");
  }

  public static String getSenderCountryCode () {

    return rb.getString ("sender.country.code");
  }

  public static String getResponderIdentifierScheme () {

    return rb.getString ("responder.identifier.scheme");
  }

  public static String getResponderIdentifierValue () {

    return rb.getString ("responder.identifier.value");
  }

  public static String getProviderCountryCode () {

    return rb.getString ("provider.country.code");
  }

  public static String getDumpRequestDirectory() {

    return rb.getString ("dump.request.directory");
  }

  public static String getDumpResponseDirectory() {

    return rb.getString ("dump.response.directory");
  }

  public static String getTrackerURL () {
    return rb.getString ("toop.tracker.url");
  }

  public static String getTrackerTopic () {
    return rb.getString ("toop.tracker.topic");
  }

  public List<Dataset> getDatasets () {

    return datasets;
  }

  public List<Dataset> getDatasetsByIdentifier(String naturalPersonIdentifier, String legalPersonIdentifier) {

    List<Dataset> _datasets = new ArrayList<> ();

    if (naturalPersonIdentifier == null && legalPersonIdentifier == null) {
      return _datasets;
    }

    for (Dataset dataset : datasets) {

      boolean npMatch = true;
      boolean leMatch = true;

      if (naturalPersonIdentifier != null &&
          !dataset.getNaturalPersonIdentifier ().equals (stripCodesFromIdentifier(naturalPersonIdentifier))) {
        npMatch = false;
      }

      if (legalPersonIdentifier != null &&
          !dataset.getLegalPersonIdentifier ().equals (stripCodesFromIdentifier(legalPersonIdentifier))) {
        leMatch = false;
      }

      if (npMatch && leMatch) {
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

  public static List<String> getCountryCodes () {
    return Arrays.asList (rb.getStringArray ("country.codes"));
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
