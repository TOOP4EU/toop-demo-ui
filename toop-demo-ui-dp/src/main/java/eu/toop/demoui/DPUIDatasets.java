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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.helger.commons.annotation.ReturnsMutableObject;
import com.helger.commons.collection.impl.CommonsArrayList;
import com.helger.commons.collection.impl.ICommonsList;
import com.helger.commons.equals.EqualsHelper;
import com.helger.commons.regex.RegExHelper;
import com.helger.commons.string.StringHelper;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigObject;

public class DPUIDatasets
{
  public static final DPUIDatasets INSTANCE = new DPUIDatasets ();

  private final ICommonsList <DPDataset> m_aDatasets = new CommonsArrayList <> ();

  private DPUIDatasets ()
  {
    final Config conf = ConfigFactory.parseResources ("dataset.conf").resolve ();
    for (final ConfigObject aConfigDataset : conf.getObjectList ("Datasets"))
    {
      final Config dataset = aConfigDataset.toConfig ();
      m_aDatasets.add (new DPDataset (dataset));
    }
  }

  @Nonnull
  @ReturnsMutableObject
  public ICommonsList <DPDataset> getDatasets ()
  {
    return m_aDatasets;
  }

  @Nullable
  private static String _stripCodesFromIdentifier (@Nullable final String identifier)
  {
    if (StringHelper.getLength (identifier) <= 6)
      return null;

    if (!RegExHelper.stringMatchesPattern ("([A-Z][A-Z]\\/){2}.+", identifier))
      return null;

    // Strip of "countryCode/countryCode/" where countryCode is 2 letters,
    // making it 2+1+2+1=6
    return identifier.substring (6);
  }

  @Nonnull
  public ICommonsList <DPDataset> getDatasetsByIdentifier (@Nullable final String naturalPersonIdentifier,
                                                           @Nullable final String legalPersonIdentifier)
  {
    final ICommonsList <DPDataset> ret = new CommonsArrayList <> ();
    if (naturalPersonIdentifier != null || legalPersonIdentifier != null)
    {
      final String sStrippedNP = _stripCodesFromIdentifier (naturalPersonIdentifier);
      final String sStrippedLP = _stripCodesFromIdentifier (legalPersonIdentifier);
      for (final DPDataset dataset : m_aDatasets)
      {
        final boolean npMatch = EqualsHelper.equals (dataset.getNaturalPersonIdentifier (), sStrippedNP);
        final boolean leMatch = EqualsHelper.equals (dataset.getLegalPersonIdentifier (), sStrippedLP);
        if (npMatch && leMatch)
        {
          ret.add (dataset);
        }
      }
    }
    return ret;
  }
}
