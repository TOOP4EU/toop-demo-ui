package eu.toop.demoui.view;

import com.helger.commons.error.level.EErrorLevel;
import com.helger.commons.string.StringHelper;
import com.helger.datetime.util.PDTXMLConverter;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.VerticalLayout;
import eu.toop.commons.codelist.EPredefinedDocumentTypeIdentifier;
import eu.toop.commons.codelist.EPredefinedProcessIdentifier;
import eu.toop.commons.concept.ConceptValue;
import eu.toop.commons.dataexchange.TDEAddressType;
import eu.toop.commons.dataexchange.TDEDataRequestSubjectType;
import eu.toop.commons.dataexchange.TDELegalEntityType;
import eu.toop.commons.dataexchange.TDENaturalPersonType;
import eu.toop.commons.jaxb.ToopXSDHelper;
import eu.toop.demoui.bean.Identity;
import eu.toop.iface.ToopInterfaceClient;
import eu.toop.kafkaclient.ToopKafkaClient;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MockRequestToSwedenDPOne extends VerticalLayout implements View {
  public MockRequestToSwedenDPOne () {

  }

  @Override
  public void enter(ViewChangeListener.ViewChangeEvent event) {
    final String dataSubjectTypeCode = "12345";
    final String naturalPersonIdentifier = "SE/GF/199005109999";
    final String naturalPersonFirstName = "Sven";
    final String naturalPersonFamilyName = "Svensson";
    final String naturalPersonBirthPlace = "Stockholm";
    final LocalDate naturalPersonBirthDate = LocalDate.of (1990, 05, 10);
    final String naturalPersonNationality = "SE";
    final String legalPersonIdentifier = "SE/GF/5591051858";
    final String legalPersonName = "Testbolag 1 AB";
    final String legalPersonNationality = "SE";

    // Send the request to the Message-Processor
    try {

      final String conceptNamespace = "http://example.register.fre/freedonia-business-register";

      final List<ConceptValue> conceptList = new ArrayList<> ();

      conceptList.add (new ConceptValue (conceptNamespace, "FreedoniaAddress"));
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
      ToopKafkaClient.send (EErrorLevel.INFO,
          () -> "[DC] Requesting concepts: "
              + StringHelper.getImplodedMapped (", ", conceptList,
              x -> x.getNamespace () + "#" + x.getValue ()));

      final TDEDataRequestSubjectType aDS = new TDEDataRequestSubjectType ();
      aDS.setDataRequestSubjectTypeCode (ToopXSDHelper.createCode (dataSubjectTypeCode));
      {
        final TDENaturalPersonType aNP = new TDENaturalPersonType ();
        aNP.setPersonIdentifier (ToopXSDHelper.createIdentifier (naturalPersonIdentifier));
        aNP.setFamilyName (ToopXSDHelper.createText (naturalPersonFamilyName));
        aNP.setFirstName (ToopXSDHelper.createText (naturalPersonFirstName));
        aNP.setBirthDate (PDTXMLConverter.getXMLCalendarDateNow ());
        final TDEAddressType aAddress = new TDEAddressType ();
        // Destination country to use
        aAddress.setCountryCode (ToopXSDHelper.createCode (naturalPersonNationality));
        aNP.setNaturalPersonLegalAddress (aAddress);
        aDS.setNaturalPerson (aNP);
      }
      {
        final TDELegalEntityType aLE = new TDELegalEntityType ();
        aLE.setLegalPersonUniqueIdentifier (ToopXSDHelper.createIdentifier (legalPersonIdentifier));
        aLE.setLegalEntityIdentifier (ToopXSDHelper.createIdentifier (legalPersonIdentifier));
        aLE.setLegalName (ToopXSDHelper.createText (legalPersonName));
        final TDEAddressType aAddress = new TDEAddressType ();
        // Destination country to use
        aAddress.setCountryCode (ToopXSDHelper.createCode (legalPersonNationality));
        aLE.setLegalEntityLegalAddress (aAddress);
        aDS.setLegalEntity (aLE);
      }

      ToopInterfaceClient.createRequestAndSendToToopConnector (aDS,
          ToopXSDHelper.createIdentifier ("iso6523-actorid-upis",
              "9999:freedonia"),
          "SE",
          EPredefinedDocumentTypeIdentifier.REQUEST_REGISTEREDORGANIZATION,
          EPredefinedProcessIdentifier.DATAREQUESTRESPONSE, conceptList);
    } catch (final IOException ex) {
      // Convert from checked to unchecked
      throw new UncheckedIOException (ex);
    }
  }
}
