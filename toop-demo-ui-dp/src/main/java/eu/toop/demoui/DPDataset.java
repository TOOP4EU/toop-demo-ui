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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.helger.commons.collection.impl.CommonsHashMap;
import com.helger.commons.collection.impl.ICommonsMap;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigObject;

public final class DPDataset implements Serializable
{
  private final String m_sNaturalPersonIdentifier;
  private final String m_sLegalPersonIdentifier;
  private final ICommonsMap <String, String> m_aConcepts = new CommonsHashMap <> ();

  public DPDataset (final Config conf)
  {
    m_sNaturalPersonIdentifier = conf.getString ("NaturalPerson.identifier");
    m_sLegalPersonIdentifier = conf.getString ("LegalPerson.identifier");

    for (final ConfigObject _concept : conf.getObjectList ("Concepts"))
    {
      final Config concept = _concept.toConfig ();

      final String conceptName = concept.getString ("name");
      final String conceptValue = concept.getString ("value");

      m_aConcepts.put (conceptName, conceptValue);
    }
  }

  @Nullable
  public String getNaturalPersonIdentifier ()
  {
    return m_sNaturalPersonIdentifier;
  }

  @Nullable
  public String getLegalPersonIdentifier ()
  {
    return m_sLegalPersonIdentifier;
  }

  @Nonnull
  public ICommonsMap <String, String> getConcepts ()
  {
    return m_aConcepts;
  }

  @Nullable
  public String getConceptValue (@Nullable final String conceptName)
  {
    return m_aConcepts.get (conceptName);
  }
}
