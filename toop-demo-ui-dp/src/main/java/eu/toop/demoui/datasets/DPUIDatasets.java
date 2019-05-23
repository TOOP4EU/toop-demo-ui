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
package eu.toop.demoui.datasets;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.helger.commons.annotation.ReturnsMutableObject;
import com.helger.commons.collection.impl.CommonsArrayList;
import com.helger.commons.collection.impl.ICommonsList;
import com.helger.commons.equals.EqualsHelper;
import com.helger.commons.io.resource.ClassPathResource;
import com.helger.commons.regex.RegExHelper;
import com.helger.commons.string.StringHelper;
import com.helger.xml.EXMLParserFeature;
import com.helger.xml.microdom.IMicroDocument;
import com.helger.xml.microdom.IMicroElement;
import com.helger.xml.microdom.serialize.MicroReader;
import com.helger.xml.serialize.read.SAXReaderSettings;

public class DPUIDatasets
{
  public static final DPUIDatasets INSTANCE = new DPUIDatasets ("datasets.xml");

  private final ICommonsList <DPDataset> m_aDatasets = new CommonsArrayList <> ();

  DPUIDatasets (@Nonnull final String sFilename)
  {
    final IMicroDocument aDoc = MicroReader.readMicroXML (new ClassPathResource (sFilename),
                                                          new SAXReaderSettings ().setFeatureValues (EXMLParserFeature.AVOID_XML_ATTACKS));
    if (aDoc == null)
      throw new IllegalArgumentException ("Failed to read data sets from '" + sFilename + "'");
    for (final IMicroElement eItem : aDoc.getDocumentElement ().getAllChildElements ())
    {
      m_aDatasets.add (new DPDataset (eItem));
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
