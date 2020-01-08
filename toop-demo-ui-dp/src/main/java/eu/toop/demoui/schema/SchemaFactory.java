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
package eu.toop.demoui.schema;

import com.helger.commons.mime.CMimeType;
import eu.toop.commons.dataexchange.v140.TDEDocumentType;
import eu.toop.commons.dataexchange.v140.TDEIssuerType;
import eu.toop.commons.jaxb.ToopXSDHelper140;

import java.util.UUID;

public class SchemaFactory {

    public static TDEIssuerType createDefaultTDEIssuerType() {

        final TDEIssuerType issuerType = new TDEIssuerType();

        issuerType.setDocumentIssuerIdentifier(ToopXSDHelper140.createIdentifier("elonia", "9916", "EE12345678"));
        issuerType.setDocumentIssuerName(ToopXSDHelper140.createText("EE-EMA"));

        return issuerType;
    }

    public static TDEIssuerType createDefaultTDEIssuerType(String issuerName) {

        final TDEIssuerType issuerType = new TDEIssuerType();

        issuerType.setDocumentIssuerIdentifier(ToopXSDHelper140.createIdentifier("elonia", "9916", "EE12345678"));
        issuerType.setDocumentIssuerName(ToopXSDHelper140.createText(issuerName));

        return issuerType;
    }

    public static TDEDocumentType createDefaultTDEDocumentType() {

        final TDEDocumentType tdeDocument = new TDEDocumentType();

        tdeDocument.setDocumentURI(ToopXSDHelper140.createIdentifier(UUID.randomUUID().toString()));
        tdeDocument.setDocumentMimeTypeCode(ToopXSDHelper140.createCode(CMimeType.APPLICATION_PDF.getAsString()));

        return tdeDocument;
    }

}
