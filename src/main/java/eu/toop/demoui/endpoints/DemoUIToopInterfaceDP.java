/**
 * Copyright (C) 2018 toop.eu
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
package eu.toop.demoui.endpoints;

import java.io.IOException;
import java.util.List;

import javax.annotation.Nonnull;

import com.helger.commons.error.level.EErrorLevel;

import eu.toop.commons.codelist.EPredefinedDocumentTypeIdentifier;
import eu.toop.commons.codelist.ReverseDocumentTypeMapping;
import eu.toop.commons.dataexchange.TDEAddressType;
import eu.toop.commons.dataexchange.TDEConceptRequestType;
import eu.toop.commons.dataexchange.TDEDataElementRequestType;
import eu.toop.commons.dataexchange.TDEDataElementResponseValueType;
import eu.toop.commons.dataexchange.TDEDataProviderType;
import eu.toop.commons.dataexchange.TDEDataRequestSubjectType;
import eu.toop.commons.dataexchange.TDETOOPRequestType;
import eu.toop.commons.dataexchange.TDETOOPResponseType;
import eu.toop.commons.error.ToopErrorException;
import eu.toop.commons.exchange.ToopMessageBuilder;
import eu.toop.commons.jaxb.ToopXSDHelper;
import eu.toop.demoui.DCUIConfig;
import eu.toop.iface.IToopInterfaceDP;
import eu.toop.iface.ToopInterfaceClient;
import eu.toop.kafkaclient.ToopKafkaClient;
import oasis.names.specification.ubl.schema.xsd.unqualifieddatatypes_21.TextType;

public class DemoUIToopInterfaceDP implements IToopInterfaceDP {

  private static boolean _canUseConcept (@Nonnull final TDEConceptRequestType aConcept) {
    // This class can only deliver to "DP" concept types without child entries
    return aConcept.hasNoConceptRequestEntries () && "DP".equals (aConcept.getConceptTypeCode ().getValue ());
  }

  private static void _applyStaticDataset (final TDEDataRequestSubjectType ds, @Nonnull final TDEConceptRequestType aConcept, @Nonnull final String sLogPrefix) {

    final TextType conceptName = aConcept.getConceptName ();
    final TDEDataElementResponseValueType aValue = new TDEDataElementResponseValueType ();
    aConcept.getDataElementResponseValue ().add (aValue);

    if (conceptName == null) {
      aValue.setErrorIndicator (ToopXSDHelper.createIndicator (true));
      aValue.setErrorCode (ToopXSDHelper.createCode ("MockError from DemoDP: Concept name missing"));
      ToopKafkaClient.send (EErrorLevel.ERROR, () -> sLogPrefix + "Concept name missing: " + aConcept);
      return;
    }

    aValue.setAlternativeResponseIndicator (ToopXSDHelper.createIndicator (false));
    aValue.setErrorIndicator (ToopXSDHelper.createIndicator (false));

    // Get datasets from config
    final DCUIConfig dcuiConfig = new DCUIConfig ();

    // Try to find dataset for natural person
    String naturalPersonIdentifier = null;
    String legalEntityIdentifier = null;

    if (ds.getNaturalPerson () != null) {
      if (ds.getNaturalPerson ().getPersonIdentifier () != null) {
        naturalPersonIdentifier = ds.getNaturalPerson ().getPersonIdentifier ().getValue ();
      }
    }

    if (ds.getLegalEntity () != null) {
      if (ds.getLegalEntity ().getLegalPersonUniqueIdentifier () != null) {
        legalEntityIdentifier = ds.getLegalEntity ().getLegalPersonUniqueIdentifier ().getValue ();
      }
    }

    final List<DCUIConfig.Dataset> datasets = dcuiConfig.getDatasetsByIdentifier (naturalPersonIdentifier, legalEntityIdentifier);

    if (datasets.size () == 0) {

      aValue.setErrorIndicator (ToopXSDHelper.createIndicator (true));
      aValue.setErrorCode (ToopXSDHelper.createCode ("MockError from DemoDP: No dataset found"));

      ToopKafkaClient.send (EErrorLevel.ERROR, () -> sLogPrefix + "No dataset found");
      return;
    }

    final DCUIConfig.Dataset dataset = datasets.get (0); // First dataset by default, ignore the rest

    final String conceptValue = dataset.getConceptValue (conceptName.getValue ());

    if (conceptValue == null) {
      aValue.setErrorIndicator (ToopXSDHelper.createIndicator (true));
      aValue.setErrorCode (ToopXSDHelper.createCode ("MockError from DemoDP: Concept [" + conceptName.getValue () + "] is missing in dataset"));
      ToopKafkaClient.send (EErrorLevel.ERROR, () -> sLogPrefix + "Failed to populate concept: Concept [" + conceptName.getValue () + "] is missing in dataset");
      return;
    }

    aValue.setResponseDescription (ToopXSDHelper.createText (conceptValue));

    ToopKafkaClient.send (EErrorLevel.INFO,
        () -> sLogPrefix + "Populated concept [" + conceptName.getValue () + "]: [" + conceptValue + "]");
  }

  @Nonnull
  private static TDETOOPResponseType _createResponseFromRequest (@Nonnull final TDETOOPRequestType aRequest,
                                                                 @Nonnull final String sLogPrefix) {
    // build response
    final TDETOOPResponseType aResponse = ToopMessageBuilder.createResponse (aRequest);
    {
      // Required for response
      final TDEDataProviderType p = new TDEDataProviderType ();
      p.setDPIdentifier (ToopXSDHelper.createIdentifier (DCUIConfig.getResponderIdentifierScheme (), DCUIConfig.getResponderIdentifierValue ()));
      p.setDPName (ToopXSDHelper.createText ("EloniaDP"));
      p.setDPElectronicAddressIdentifier (ToopXSDHelper.createIdentifier ("elonia@register.example.org"));
      final TDEAddressType pa = new TDEAddressType ();
      pa.setCountryCode (ToopXSDHelper.createCode (DCUIConfig.getProviderCountryCode ()));
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

  private static void applyConceptValues (final TDEDataRequestSubjectType ds, final TDEDataElementRequestType aDER, final String sLogPrefix) {

    final TDEConceptRequestType aFirstLevelConcept = aDER.getConceptRequest ();
    if (aFirstLevelConcept != null) {
      for (final TDEConceptRequestType aSecondLevelConcept : aFirstLevelConcept.getConceptRequest ()) {
        for (final TDEConceptRequestType aThirdLevelConcept : aSecondLevelConcept.getConceptRequest ()) {
          if (_canUseConcept (aThirdLevelConcept)) {
            _applyStaticDataset (ds, aThirdLevelConcept, sLogPrefix);
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
      applyConceptValues (aRequest.getDataRequestSubject (), aDER, sLogPrefix);
    }

    // send back to toop-connector at /from-dp
    // The URL must be configured in toop-interface.properties file
    try {
      ToopInterfaceClient.sendResponseToToopConnector (aResponse);
    } catch (final ToopErrorException ex) {
      throw new RuntimeException (ex);
    }
  }
}
