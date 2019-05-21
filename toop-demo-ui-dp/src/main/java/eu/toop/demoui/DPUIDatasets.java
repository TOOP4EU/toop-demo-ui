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
import javax.annotation.Nullable;

import com.helger.commons.annotation.ReturnsMutableObject;
import com.helger.commons.regex.RegExHelper;
import com.helger.commons.string.StringHelper;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigObject;
import com.typesafe.config.ConfigParseOptions;
import com.typesafe.config.ConfigSyntax;

public class DPUIDatasets
{
  public static final DPUIDatasets INSTANCE = new DPUIDatasets ();

  private final List <DPDataset> m_aDatasets = new ArrayList <> ();

  private DPUIDatasets ()
  {
    final ConfigParseOptions opt = ConfigParseOptions.defaults ();
    opt.setSyntax (ConfigSyntax.CONF);

    final Config conf = ConfigFactory.parseResources ("dataset.conf").resolve ();

    for (final ConfigObject _dataset : conf.getObjectList ("Datasets"))
    {
      final Config dataset = _dataset.toConfig ();

      m_aDatasets.add (new DPDataset (dataset));
    }
  }

  @Nonnull
  @ReturnsMutableObject
  public List <DPDataset> getDatasets ()
  {
    return m_aDatasets;
  }

  @Nonnull
  public List <DPDataset> getDatasetsByIdentifier (@Nullable final String naturalPersonIdentifier,
                                                   @Nullable final String legalPersonIdentifier)
  {
    final List <DPDataset> ret = new ArrayList <> ();
    if (naturalPersonIdentifier != null || legalPersonIdentifier != null)
    {
      final String sStrippedNP = _stripCodesFromIdentifier (naturalPersonIdentifier);
      final String sStrippedLP = _stripCodesFromIdentifier (legalPersonIdentifier);
      for (final DPDataset dataset : m_aDatasets)
      {
        final boolean npMatch = naturalPersonIdentifier == null ||
                                dataset.getNaturalPersonIdentifier ().equals (sStrippedNP);
        final boolean leMatch = legalPersonIdentifier == null ||
                                dataset.getLegalPersonIdentifier ().equals (sStrippedLP);
        if (npMatch && leMatch)
        {
          ret.add (dataset);
        }
      }
    }
    return ret;
  }

  @Nonnull
  public List <DPDataset> getDatasetsByNaturalPersonIdentifier (@Nullable final String naturalPersonIdentifier)
  {
    final List <DPDataset> ret = new ArrayList <> ();
    final String sStripped = _stripCodesFromIdentifier (naturalPersonIdentifier);
    for (final DPDataset dataset : m_aDatasets)
      if (dataset.getNaturalPersonIdentifier ().equals (sStripped))
        ret.add (dataset);
    return ret;
  }

  @Nonnull
  public List <DPDataset> getDatasetsByLegalPersonIdentifier (@Nullable final String legalPersonIdentifier)
  {
    final List <DPDataset> ret = new ArrayList <> ();
    final String sStripped = _stripCodesFromIdentifier (legalPersonIdentifier);
    for (final DPDataset dataset : m_aDatasets)
      if (dataset.getLegalPersonIdentifier ().equals (sStripped))
        ret.add (dataset);
    return ret;
  }

  private static boolean _isValidIdentifier (@Nullable final String identifier)
  {
    return StringHelper.hasText (identifier) && RegExHelper.stringMatchesPattern ("([A-Z][A-Z]\\/){2}.+", identifier);
  }

  @Nullable
  private static String _stripCodesFromIdentifier (@Nullable final String identifier)
  {
    if (!_isValidIdentifier (identifier))
      return null;

    // Strip of "countryCode/countryCode/" where countryCode is 2 letters,
    // making it 2+1+2+1=6
    return identifier.substring (6);
  }
}
