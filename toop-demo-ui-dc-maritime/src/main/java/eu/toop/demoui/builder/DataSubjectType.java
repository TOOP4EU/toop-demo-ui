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
package eu.toop.demoui.builder;

import eu.toop.commons.dataexchange.v140.TDEDataRequestSubjectType;
import eu.toop.commons.dataexchange.v140.TDELegalPersonType;
import eu.toop.commons.dataexchange.v140.TDENaturalPersonType;

public class DataSubjectType extends TDEDataRequestSubjectType {
    final TDENaturalPersonType aNP;
    final TDELegalPersonType aLP;

    private DataSubjectType(Builder builder) {
        this.aNP = builder.aNP;
        this.aLP = builder.aLP;
        super.setLegalPerson(builder.aLP);
        super.setNaturalPerson(builder.aNP);
    }

    public TDENaturalPersonType getaNP() {
        return aNP;
    }

    public TDELegalPersonType getaLP() {
        return aLP;
    }

    public static class Builder {
        TDENaturalPersonType aNP;
        TDELegalPersonType aLP;

        public Builder withNP(final TDENaturalPersonType aNP) {
            this.aNP = aNP;
            return this;
        }

        public Builder withLP(final TDELegalPersonType aLP) {
            this.aLP = aLP;
            return this;
        }

        public DataSubjectType build() {
            return new DataSubjectType(this);
        }
    }

}
