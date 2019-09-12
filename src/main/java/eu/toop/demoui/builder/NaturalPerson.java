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
