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

import java.util.UUID;

import com.helger.commons.string.StringHelper;

import eu.toop.commons.codelist.EPredefinedDocumentTypeIdentifier;
import eu.toop.commons.codelist.EPredefinedProcessIdentifier;
import eu.toop.commons.dataexchange.v140.TDEAddressWithLOAType;
import eu.toop.commons.dataexchange.v140.TDEDataRequestSubjectType;
import eu.toop.commons.dataexchange.v140.TDEDocumentRequestType;
import eu.toop.commons.dataexchange.v140.TDELegalPersonType;
import eu.toop.commons.dataexchange.v140.TDENaturalPersonType;
import eu.toop.commons.dataexchange.v140.TDETOOPRequestType;
import eu.toop.commons.jaxb.ToopXSDHelper140;
import eu.toop.commons.usecase.EToopEntityType;
import eu.toop.demoui.DCUIConfig;
import eu.toop.demoui.builder.model.Request;
import oasis.names.specification.ubl.schema.xsd.unqualifieddatatypes_21.CodeType;
import oasis.names.specification.ubl.schema.xsd.unqualifieddatatypes_21.IdentifierType;

public class TOOPRequestMaker {
    private Request formValues;

    public TOOPRequestMaker(Request formValues) {
        this.formValues = formValues;
    }

    public TDETOOPRequestType createTOOPRequest() {
        TDETOOPRequestType aReq = new TOOPRequest.Builder()
                .withDS(createDataRequestSubject())
                .withSrcCountryCode(srcCountry())
                .withDstCountryCode(dstCountry())
                .witheDocumentTypeID(eDocumentTypeID())
                .witheProcessID(eProcessID())
                .withSenderIdentifier(senderIdentifier())
                .build()
                .createMockRequest();
        final UUID uuid = generateUUID();
        aReq.setDocumentUniversalUniqueIdentifier(documentUniversalUniqueIdentifier(uuid));
        aReq.setSpecificationIdentifier(specificationIdentifier(uuid));
        aReq.addDocumentRequest(createDocumentRequestType());
        return aReq;
    }

    private TDEDataRequestSubjectType createDataRequestSubject() {
        final TDEDataRequestSubjectType aDS;

        /* check if NaturalPerson value hasText -> Build DataSubject with NaturalPerson
        *  if legalEntityId (IMONumber at Maritime or Company ID at GBM) hasText -> Build DataSubject with LegalPerson
        * */

        if (StringHelper.hasText(formValues.getNaturalPersonIdentifier())) {

            aDS = new DataSubjectType.Builder().withNP(createNaturalPerson()).build();
            aDS.setDataRequestSubjectTypeCode(ToopXSDHelper140.createCode(EToopEntityType.NATURAL_PERSON.getID()));
            return aDS;

        } else if(StringHelper.hasText(formValues.getId())) {
            aDS = new DataSubjectType.Builder().withLP(createLegalPerson()).build();
            aDS.setDataRequestSubjectTypeCode(ToopXSDHelper140.createCode(EToopEntityType.LEGAL_ENTITY.getID()));
            return aDS;

        }

        return  new DataSubjectType.Builder().build();

    }

    private TDENaturalPersonType createNaturalPerson() {
        final TDENaturalPersonType aNP = new TDENaturalPersonType();
        aNP.setPersonIdentifier(ToopXSDHelper140.createIdentifierWithLOA (identifierPrefix() + formValues.getNaturalPersonIdentifier()));
        aNP.setFirstName(ToopXSDHelper140.createTextWithLOA (formValues.getNaturalPersonFirstName()));
        aNP.setFamilyName(ToopXSDHelper140.createTextWithLOA (formValues.getNaturalPersonFamilyName()));
        // mandatory field
        aNP.setBirthDate (ToopXSDHelper140.createDateWithLOANow ());
        final TDEAddressWithLOAType aAddress = new TDEAddressWithLOAType ();
        aAddress.setCountryCode (ToopXSDHelper140.createCodeWithLOA (formValues.getCountryCode()));
        aNP.setNaturalPersonLegalAddress (aAddress);

        return aNP;

    }

    private TDELegalPersonType createLegalPerson() {
        final TDELegalPersonType aLP = new TDELegalPersonType();
        aLP.setLegalPersonUniqueIdentifier(ToopXSDHelper140.createIdentifierWithLOA (identifierPrefix() + formValues.getId()));
        aLP.setLegalName(ToopXSDHelper140.createTextWithLOA (formValues.getLegalName()));
        final TDEAddressWithLOAType aAddress = new TDEAddressWithLOAType();
        aAddress.setCountryCode (ToopXSDHelper140.createCodeWithLOA (formValues.getCountryCode()));
        aLP.setLegalPersonLegalAddress(aAddress);

        return aLP;
    }

    private TDEDocumentRequestType createDocumentRequestType() {
        final TDEDocumentRequestType documentRequestType = new TDEDocumentRequestType();

        if(formValues.geteDocumentTypeID().toString().contains("SHIPCERTIFICATE_LIST") ||
                formValues.geteDocumentTypeID().toString().contains("CREWCERTIFICATE_LIST")) {
                    documentRequestType.setDocumentURI(dURI("https://koolitus.emde.ee/cc/b0/67/123456"));
        } else {
            documentRequestType.setDocumentURI(dURI(formValues.getDocumentIdentifier()));
        }
//        documentRequestType.setDocumentURI(dURI("https://koolitus.emde.ee/cc/b0/67/123456"));
//        documentRequestType.setDocumentURI(dURI(formValues.getDocumentIdentifier()));
        documentRequestType.setDocumentRequestIdentifier(dRequestIdentifier("demo-agency", "toop-doctypeid-qns"));
        documentRequestType.setDocumentRequestTypeCode(dRequestTypeCode("ETR"));

        return documentRequestType;
    }

    private String identifierPrefix() {
        final String identifierPrefix = formValues.getCountryCode() + "/" + DCUIConfig.getSenderCountryCode () + "/";
        return identifierPrefix;
    }

    private String srcCountry() {
        return DCUIConfig.getSenderCountryCode();
    }

    private String dstCountry() {
        return formValues.getCountryCode();
    }

    private EPredefinedDocumentTypeIdentifier eDocumentTypeID() {
        return formValues.geteDocumentTypeID();
    }

    private EPredefinedProcessIdentifier eProcessID() {
        EPredefinedProcessIdentifier eProcessID;
        if (eDocumentTypeID() == EPredefinedDocumentTypeIdentifier.REQUEST_SHIPCERTIFICATE
                || eDocumentTypeID() == EPredefinedDocumentTypeIdentifier.REQUEST_SHIPCERTIFICATE_LIST
                || eDocumentTypeID() == EPredefinedDocumentTypeIdentifier.REQUEST_CREWCERTIFICATE
                || eDocumentTypeID() == EPredefinedDocumentTypeIdentifier.REQUEST_CREWCERTIFICATE_LIST) {
            eProcessID = EPredefinedProcessIdentifier.TWOPHASEDREQUESTRESPONSE;
        } else {
            eProcessID = EPredefinedProcessIdentifier.DOCUMENTREQUESTRESPONSE;
        }
        return eProcessID;
    }

    private IdentifierType senderIdentifier() {
        return ToopXSDHelper140.createIdentifier(DCUIConfig.getSenderIdentifierScheme(), DCUIConfig.getSenderIdentifierValue());
    }

    private UUID generateUUID() {
        return UUID.randomUUID();
    }

    public IdentifierType getUUID(TDETOOPRequestType aReq) {
        return aReq.getDocumentUniversalUniqueIdentifier();
    }

    private IdentifierType documentUniversalUniqueIdentifier(UUID uuid) {
        return ToopXSDHelper140.createIdentifier("UUID", null, uuid.toString());
    }

    private IdentifierType specificationIdentifier(UUID uuid) {
        return ToopXSDHelper140.createIdentifier(EPredefinedDocumentTypeIdentifier.DOC_TYPE_SCHEME,
                eDocumentTypeID().getID().substring(0, eDocumentTypeID().getID().indexOf("##")));
    }

    /* DOCUMENT REQUEST TYPE helpers */
    private IdentifierType dURI(String URI) {
//        return ToopXSDHelper140.createIdentifier ("https://koolitus.emde.ee/cc/b0/67/123456");
        return ToopXSDHelper140.createIdentifier (URI);
    }

    private IdentifierType dRequestIdentifier(String schemeAgencyID, String schemeID) {
//        return ToopXSDHelper140.createIdentifier ("demo-agency", "toop-doctypeid-qns", uuid().toString());
        return ToopXSDHelper140.createIdentifier (schemeAgencyID, schemeID, generateUUID().toString());
    }

    private CodeType dRequestTypeCode(String code) {
//        return ToopXSDHelper140.createCode("ETR");
        return ToopXSDHelper140.createCode(code);
    }

}
