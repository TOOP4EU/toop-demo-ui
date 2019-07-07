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
