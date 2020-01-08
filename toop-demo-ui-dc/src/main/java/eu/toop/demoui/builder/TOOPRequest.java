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

import eu.toop.commons.codelist.EPredefinedDocumentTypeIdentifier;
import eu.toop.commons.codelist.EPredefinedProcessIdentifier;
import eu.toop.commons.dataexchange.v140.TDEDataRequestSubjectType;
import eu.toop.commons.dataexchange.v140.TDEDocumentRequestType;
import eu.toop.commons.dataexchange.v140.TDETOOPRequestType;
import eu.toop.commons.exchange.ToopMessageBuilder140;
import oasis.names.specification.ubl.schema.xsd.unqualifieddatatypes_21.IdentifierType;

public class TOOPRequest extends TDETOOPRequestType {
    final private TDEDataRequestSubjectType aDS;
    final private String srcCountryCode, dstCountryCode;
    final private EPredefinedDocumentTypeIdentifier eDocumentTypeID;
    final private EPredefinedProcessIdentifier eProcessID;
    final private TDEDocumentRequestType documentRequestType;
    final private IdentifierType senderIdentifier;

    private TOOPRequest(Builder builder) {
        this.aDS = builder.aDS;
        this.srcCountryCode = builder.srcCountryCode;
        this.dstCountryCode = builder.dstCountryCode;
        this.eDocumentTypeID = builder.eDocumentTypeID;
        this.eProcessID = builder.eProcessID;
        this.documentRequestType = builder.documentRequestType;
        this.senderIdentifier = builder.senderIdentifier;
    }

    public TDETOOPRequestType createMockRequest() {
        return ToopMessageBuilder140.createMockRequest(
                aDS,
                srcCountryCode,
                dstCountryCode,
                senderIdentifier,
                eDocumentTypeID,
                eProcessID,
                null);
    }

    public IdentifierType getSenderIdentifier() {
        return senderIdentifier;
    }

    public TDEDataRequestSubjectType getaDS() {
        return aDS;
    }

    public String getSrcCountryCode() {
        return srcCountryCode;
    }

    public String getDstCountryCode() {
        return dstCountryCode;
    }

    public EPredefinedDocumentTypeIdentifier geteDocumentTypeID() {
        return eDocumentTypeID;
    }

    public EPredefinedProcessIdentifier geteProcessID() {
        return eProcessID;
    }

    public TDEDocumentRequestType getDocumentRequestType() {
        return documentRequestType;
    }

    public static class Builder {
        TDEDataRequestSubjectType aDS;
        String srcCountryCode, dstCountryCode;
        EPredefinedDocumentTypeIdentifier eDocumentTypeID;
        EPredefinedProcessIdentifier eProcessID;
        TDEDocumentRequestType documentRequestType;
        IdentifierType senderIdentifier;

        public Builder withDS(final TDEDataRequestSubjectType aDS) {
            this.aDS = aDS;
            return this;
        }

        public Builder withSrcCountryCode(final String srcCountryCode) {
            this.srcCountryCode = srcCountryCode;
            return this;
        }
        public Builder withDstCountryCode(final String dstCountryCode) {
            this.dstCountryCode = dstCountryCode;
            return this;
        }

        public Builder witheDocumentTypeID(EPredefinedDocumentTypeIdentifier eDocumentTypeID) {
            this.eDocumentTypeID = eDocumentTypeID;
            return this;
        }

        public Builder witheProcessID(EPredefinedProcessIdentifier eProcessID) {
            this.eProcessID = eProcessID;
            return this;
        }

        public Builder withDocumentRequestType(TDEDocumentRequestType documentRequestType) {
            this.documentRequestType = documentRequestType;
            return this;
        }

        public Builder withSenderIdentifier(IdentifierType senderIdentifier) {
            this.senderIdentifier = senderIdentifier;
            return this;
        }

        public TOOPRequest build() {
            return new TOOPRequest(this);
        }




    }


}
