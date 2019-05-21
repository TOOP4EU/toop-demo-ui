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

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import com.helger.commons.annotation.ReturnsMutableObject;
import com.helger.commons.regex.RegExHelper;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigObject;
import com.typesafe.config.ConfigParseOptions;
import com.typesafe.config.ConfigSyntax;

public class DPUIDatasets {

  private final List<DPDataset> datasets = new ArrayList<> ();

  public DPUIDatasets() {

    final ConfigParseOptions opt = ConfigParseOptions.defaults();
    opt.setSyntax(ConfigSyntax.CONF);

    final Config conf = ConfigFactory.parseResources("dataset.conf").resolve();

    for (final ConfigObject _dataset : conf.getObjectList("Datasets")) {
      final Config dataset = _dataset.toConfig();

      datasets.add(new DPDataset (dataset));
    }
  }

  @Nonnull
  @ReturnsMutableObject
  public List<DPDataset> getDatasets () {
    return datasets;
  }

  public List<DPDataset> getDatasetsByIdentifier(final String naturalPersonIdentifier, final String legalPersonIdentifier) {

    final List<DPDataset> _datasets = new ArrayList<> ();

    if (naturalPersonIdentifier == null && legalPersonIdentifier == null) {
      return _datasets;
    }

    for (final DPDataset dataset : datasets) {

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

  public List<DPDataset> getDatasetsByNaturalPersonIdentifier(final String naturalPersonIdentifier) {

    final List<DPDataset> _datasets = new ArrayList<> ();
    for (final DPDataset dataset : datasets) {

      if (dataset.getNaturalPersonIdentifier ().equals (stripCodesFromIdentifier(naturalPersonIdentifier))) {
        _datasets.add (dataset);
      }
    }

    return _datasets;
  }

  public List<DPDataset> getDatasetsByLegalPersonIdentifier(final String legalPersonIdentifier) {

    final List<DPDataset> _datasets = new ArrayList<> ();
    for (final DPDataset dataset : datasets) {

      if (dataset.getLegalPersonIdentifier ().equals (stripCodesFromIdentifier(legalPersonIdentifier))) {
        _datasets.add (dataset);
      }
    }

    return _datasets;
  }

  private static boolean isValidIdentifier(final String identifier) {
    return RegExHelper.stringMatchesPattern ("([A-Z][A-Z]\\/){2}.+", identifier);
  }

  private String stripCodesFromIdentifier(final String identifier) {

    if (!isValidIdentifier(identifier)) {
      return null;
    }

    return identifier.substring (6);
  }
}
