/**
 * Copyright (C) 2018 toop.eu
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.toop.demoui.endpoints;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;

import com.helger.commons.error.level.EErrorLevel;

import eu.toop.commons.codelist.EPredefinedDocumentTypeIdentifier;
import eu.toop.commons.codelist.ReverseDocumentTypeMapping;
import eu.toop.commons.dataexchange.TDEAddressType;
import eu.toop.commons.dataexchange.TDEConceptRequestType;
import eu.toop.commons.dataexchange.TDEDataElementRequestType;
import eu.toop.commons.dataexchange.TDEDataElementResponseValueType;
import eu.toop.commons.dataexchange.TDEDataProviderType;
import eu.toop.commons.dataexchange.TDETOOPRequestType;
import eu.toop.commons.dataexchange.TDETOOPResponseType;
import eu.toop.commons.exchange.ToopMessageBuilder;
import eu.toop.commons.jaxb.ToopXSDHelper;
import eu.toop.iface.IToopInterfaceDP;
import eu.toop.iface.ToopInterfaceClient;
import eu.toop.kafkaclient.ToopKafkaClient;
import oasis.names.specification.ubl.schema.xsd.unqualifieddatatypes_21.TextType;

public class DemoUIToopInterfaceDP implements IToopInterfaceDP {

  private static boolean _canUseConcept (@Nonnull final TDEConceptRequestType aConcept) {
    // This class can only deliver to "DP" concept types without child entries
    return aConcept.hasNoConceptRequestEntries () && "DP".equals (aConcept.getConceptTypeCode ().getValue ());
  }

  private static void _applyStaticDataset (@Nonnull final TDEConceptRequestType aConcept) {

    final TextType conceptName = aConcept.getConceptName ();
    final TDEDataElementResponseValueType aValue = new TDEDataElementResponseValueType ();

    aValue.setAlternativeResponseIndicator (ToopXSDHelper.createIndicator (false));
    aValue.setErrorIndicator (ToopXSDHelper.createIndicator (false));

    final Map<String, String> conceptValues = new HashMap<> ();
    conceptValues.put("EloniaAddress", "Gamlavegen 234, 321 44, Velma, Elonia");
    conceptValues.put("EloniaBusinessCode", "JF 234556-6213");
    conceptValues.put("EloniaCompanyType", "Limited");
    conceptValues.put("EloniaRegistrationDate", "2012-01-12");
    conceptValues.put("EloniaCompanyName", "Zizi mat");
    conceptValues.put("EloniaCompanyNaceCode", "C27.9");
    conceptValues.put("EloniaActivityDeclaration", "Manufacture of other electrical equipment");
    conceptValues.put("EloniaRegistrationAuthority", "Elonia Tax Agency");
    conceptValues.put("EloniaLegalStatus", "Active");

    if (conceptName != null && conceptName.getValue () != null) {

      if (conceptValues.containsKey (conceptName.getValue ())) {
        aValue.setResponseDescription (ToopXSDHelper.createText (conceptValues.get(conceptName.getValue ())));
      } else {
        aValue.setErrorIndicator (ToopXSDHelper.createIndicator (true));
        aValue.setErrorCode (ToopXSDHelper.createCode ("MockError from DemoDP"));
      }

      aConcept.getDataElementResponseValue ().add (aValue);
    }
  }

  @Nonnull
  private static TDETOOPResponseType _createResponseFromRequest (@Nonnull final TDETOOPRequestType aRequest,
                                                                 @Nonnull final String sLogPrefix) {
    // build response
    final TDETOOPResponseType aResponse = ToopMessageBuilder.createResponse (aRequest);
    {
      // Required for response
      final TDEDataProviderType p = new TDEDataProviderType ();
      p.setDPIdentifier (ToopXSDHelper.createIdentifier ("iso6523-actorid-upis", "9999:elonia"));
      p.setDPName (ToopXSDHelper.createText ("EloniaDP"));
      p.setDPElectronicAddressIdentifier (ToopXSDHelper.createIdentifier ("elonia@register.example.org"));
      final TDEAddressType pa = new TDEAddressType ();
      pa.setCountryCode (ToopXSDHelper.createCode ("SV"));
      p.setDPLegalAddress (pa);
      aResponse.setDataProvider (p);
    }

    // Document type must be switch from request to response
    final EPredefinedDocumentTypeIdentifier eRequestDocType = EPredefinedDocumentTypeIdentifier.getFromDocumentTypeIdentifierOrNull (aRequest.getDocumentTypeIdentifier ()
                                                                                                                                             .getSchemeID (),
                                                                                                                                     aRequest.getDocumentTypeIdentifier ()
                                                                                                                                             .getValue ());
    if (eRequestDocType != null) {
      try {
        final EPredefinedDocumentTypeIdentifier eResponseDocType = ReverseDocumentTypeMapping.getReverseDocumentType (eRequestDocType);

        // Set new doc type in response
        ToopKafkaClient.send (EErrorLevel.INFO,
                              () -> sLogPrefix + "Switching document type '" + eRequestDocType.getURIEncoded ()
                                    + "' to '" + eResponseDocType.getURIEncoded () + "'");
        aResponse.setDocumentTypeIdentifier (ToopXSDHelper.createIdentifier (eResponseDocType.getScheme (),
                                                                             eResponseDocType.getID ()));
      } catch (final IllegalArgumentException ex) {
        // Found no reverse document type
        ToopKafkaClient.send (EErrorLevel.INFO,
                              () -> sLogPrefix + "Found no response document type for '"
                                    + aRequest.getDocumentTypeIdentifier ().getSchemeID () + "::"
                                    + aRequest.getDocumentTypeIdentifier ().getValue () + "'");
      }
    }
    return aResponse;
  }

  private static void applyConceptValues (final TDEDataElementRequestType aDER, final String sLogPrefix) {

    final TDEConceptRequestType aFirstLevelConcept = aDER.getConceptRequest ();
    if (aFirstLevelConcept != null) {
      for (final TDEConceptRequestType aSecondLevelConcept : aFirstLevelConcept.getConceptRequest ()) {
        for (final TDEConceptRequestType aThirdLevelConcept : aSecondLevelConcept.getConceptRequest ()) {
          if (_canUseConcept (aThirdLevelConcept)) {
            _applyStaticDataset (aThirdLevelConcept);
          } else {
            // 3 level nesting is maximum
            ToopKafkaClient.send (EErrorLevel.ERROR,
                () -> sLogPrefix + "A third level concept that is unusable - weird: "
                    + aThirdLevelConcept);
          }
        }
      }
    }
  }

  public void onToopRequest (@Nonnull final TDETOOPRequestType aRequest) throws IOException {

    final String sRequestID = aRequest.getDataRequestIdentifier ().getValue ();
    final String sLogPrefix = "[" + sRequestID + "] ";
    ToopKafkaClient.send (EErrorLevel.INFO, () -> sLogPrefix + "Received DP Backend Request");

    // build response
    final TDETOOPResponseType aResponse = _createResponseFromRequest (aRequest, sLogPrefix);

    // add all the mapped values in the response
    for (final TDEDataElementRequestType aDER : aResponse.getDataElementRequest ()) {
      applyConceptValues (aDER, sLogPrefix);
    }

    // send back to toop-connector at /from-dp
    // The URL must be configured in toop-interface.properties file
    ToopInterfaceClient.sendResponseToToopConnector (aResponse);
  }
}
