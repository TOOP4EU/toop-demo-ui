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
package eu.toop.demoui.builder;

import eu.toop.commons.dataexchange.v140.TDENaturalPersonType;

public class NaturalPerson extends TDENaturalPersonType {

    final String naturalPersonIdentifier, naturalPersonFirstName, naturalPersonFamilyName;

    private NaturalPerson(Builder builder) {
        this.naturalPersonIdentifier = builder.naturalPersonIdentifier;
        this.naturalPersonFirstName = builder.naturalPersonFirstName;
        this.naturalPersonFamilyName = builder.naturalPersonFamilyName;
    }

    public String getNaturalPersonIdentifier() {
        return naturalPersonIdentifier;
    }

    public String getNaturalPersonFirstName() {
        return naturalPersonFirstName;
    }

    public String getNaturalPersonFamilyName() {
        return naturalPersonFamilyName;
    }

    public static class Builder {
        String naturalPersonIdentifier, naturalPersonFirstName, naturalPersonFamilyName;

        public Builder withNaturalPersonIdentifier(final String naturalPersonIdentifier) {
            this.naturalPersonIdentifier = naturalPersonIdentifier;
            return this;
        }
        public Builder withNaturalPersonFirstName(final String naturalPersonFirstName) {
            this.naturalPersonFirstName = naturalPersonFirstName;
            return this;
        }

        public Builder withNaturalPersonFamilyName(final String naturalPersonFamilyName) {
            this.naturalPersonFamilyName = naturalPersonFamilyName;
            return this;
        }

        public NaturalPerson build() {
            return new NaturalPerson(this);
        }
    }
}
