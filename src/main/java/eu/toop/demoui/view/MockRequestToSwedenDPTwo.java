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
package eu.toop.demoui.view;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.helger.commons.error.level.EErrorLevel;
import com.helger.commons.string.StringHelper;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.VerticalLayout;

import eu.toop.commons.codelist.EPredefinedDocumentTypeIdentifier;
import eu.toop.commons.codelist.EPredefinedProcessIdentifier;
import eu.toop.commons.concept.ConceptValue;
import eu.toop.commons.dataexchange.v140.TDEAddressWithLOAType;
import eu.toop.commons.dataexchange.v140.TDEDataRequestSubjectType;
import eu.toop.commons.dataexchange.v140.TDELegalPersonType;
import eu.toop.commons.dataexchange.v140.TDENaturalPersonType;
import eu.toop.commons.error.ToopErrorException;
import eu.toop.commons.jaxb.ToopXSDHelper140;
import eu.toop.iface.ToopInterfaceClient;
import eu.toop.kafkaclient.ToopKafkaClient;

public class MockRequestToSwedenDPTwo extends VerticalLayout implements View {
  public MockRequestToSwedenDPTwo () {

  }

  @Override
  public void enter (final ViewChangeListener.ViewChangeEvent event) {
    final String srcCountryCode = "SE";
    final String dataSubjectTypeCode = "12345";
    final String naturalPersonIdentifier = "SE/GF/199105109999";
    final String naturalPersonFirstName = "Olof";
    final String naturalPersonFamilyName = "Olofsson";
    final String naturalPersonBirthPlace = "Stockholm";
    final LocalDate naturalPersonBirthDate = LocalDate.of (1991, 05, 10);
    final String naturalPersonNationality = "SE";
    final String legalPersonIdentifier = "SE/GF/5591051841";
    final String legalPersonName = "Testbolag 2 AB";
    final String legalPersonNationality = "SE";

    // Send the request to the Message-Processor
    try {

      final String conceptNamespace = "http://example.register.fre/freedonia-business-register";

      final List<ConceptValue> conceptList = new ArrayList<> ();

      conceptList.add (new ConceptValue (conceptNamespace, "FreedoniaStreetAddress"));
      conceptList.add (new ConceptValue (conceptNamespace, "FreedoniaSSNumber"));
      conceptList.add (new ConceptValue (conceptNamespace, "FreedoniaBusinessCode"));
      conceptList.add (new ConceptValue (conceptNamespace, "FreedoniaVATNumber"));
      conceptList.add (new ConceptValue (conceptNamespace, "FreedoniaCompanyType"));
      conceptList.add (new ConceptValue (conceptNamespace, "FreedoniaRegistrationDate"));
      conceptList.add (new ConceptValue (conceptNamespace, "FreedoniaRegistrationNumber"));
      conceptList.add (new ConceptValue (conceptNamespace, "FreedoniaCompanyName"));
      conceptList.add (new ConceptValue (conceptNamespace, "FreedoniaCompanyNaceCode"));
      conceptList.add (new ConceptValue (conceptNamespace, "FreedoniaActivityDeclaration"));
      conceptList.add (new ConceptValue (conceptNamespace, "FreedoniaRegistrationAuthority"));
      conceptList.add (new ConceptValue (conceptNamespace, "FreedoniaLegalStatus"));

      // Notify the logger and Package-Tracker that we are sending a TOOP Message!
      ToopKafkaClient.send (EErrorLevel.INFO, () -> "[DC] Requesting concepts: "
          + StringHelper.getImplodedMapped (", ", conceptList, x -> x.getNamespace () + "#" + x.getValue ()));

      final TDEDataRequestSubjectType aDS = new TDEDataRequestSubjectType ();
      aDS.setDataRequestSubjectTypeCode (ToopXSDHelper140.createCode (dataSubjectTypeCode));
      {
        final TDENaturalPersonType aNP = new TDENaturalPersonType ();
        aNP.setPersonIdentifier (ToopXSDHelper140.createIdentifierWithLOA (naturalPersonIdentifier));
        aNP.setFamilyName (ToopXSDHelper140.createTextWithLOA (naturalPersonFamilyName));
        aNP.setFirstName (ToopXSDHelper140.createTextWithLOA (naturalPersonFirstName));
        aNP.setBirthDate (ToopXSDHelper140.createDateWithLOANow ());
        final TDEAddressWithLOAType aAddress = new TDEAddressWithLOAType ();
        // Destination country to use
        aAddress.setCountryCode (ToopXSDHelper140.createCodeWithLOA (naturalPersonNationality));
        aNP.setNaturalPersonLegalAddress (aAddress);
        aDS.setNaturalPerson (aNP);
      }
      {
        final TDELegalPersonType aLE = new TDELegalPersonType ();
        aLE.setLegalPersonUniqueIdentifier (ToopXSDHelper140.createIdentifierWithLOA (legalPersonIdentifier));
        aLE.setLegalEntityIdentifier (ToopXSDHelper140.createIdentifierWithLOA (legalPersonIdentifier));
        aLE.setLegalName (ToopXSDHelper140.createTextWithLOA (legalPersonName));
        final TDEAddressWithLOAType aAddress = new TDEAddressWithLOAType ();
        // Destination country to use
        aAddress.setCountryCode (ToopXSDHelper140.createCodeWithLOA (legalPersonNationality));
        aLE.setLegalPersonLegalAddress (aAddress);
        aDS.setLegalPerson (aLE);
      }

      ToopInterfaceClient.createRequestAndSendToToopConnector (aDS, srcCountryCode, naturalPersonNationality,
          ToopXSDHelper140.createIdentifier ("iso6523-actorid-upis", "9999:freedonia"),
          EPredefinedDocumentTypeIdentifier.REQUEST_REGISTEREDORGANIZATION,
          EPredefinedProcessIdentifier.DATAREQUESTRESPONSE, conceptList);
    } catch (final IOException | ToopErrorException ex) {
      // Convert from checked to unchecked
      throw new RuntimeException (ex);
    }
  }
}
