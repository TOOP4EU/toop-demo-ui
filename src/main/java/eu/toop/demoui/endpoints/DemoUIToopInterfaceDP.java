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
import java.math.BigDecimal;

import javax.annotation.Nonnull;

import com.helger.commons.error.level.EErrorLevel;
import com.vaadin.ui.UI;

import eu.toop.commons.dataexchange.TDEAddressType;
import eu.toop.commons.dataexchange.TDEConceptRequestType;
import eu.toop.commons.dataexchange.TDEDataElementRequestType;
import eu.toop.commons.dataexchange.TDEDataElementResponseValueType;
import eu.toop.commons.dataexchange.TDEDataProviderType;
import eu.toop.commons.dataexchange.TDETOOPDataRequestType;
import eu.toop.commons.dataexchange.TDETOOPDataResponseType;
import eu.toop.commons.doctype.EToopDocumentType;
import eu.toop.commons.exchange.ToopMessageBuilder;
import eu.toop.commons.jaxb.ToopXSDHelper;
import eu.toop.iface.IToopInterfaceDP;
import eu.toop.iface.ToopInterfaceClient;
import eu.toop.kafkaclient.ToopKafkaClient;

public class DemoUIToopInterfaceDP implements IToopInterfaceDP {
  private final UI _ui;

  public DemoUIToopInterfaceDP (final UI ui) {

    this._ui = ui;
  }

  private static boolean _canUseConcept (@Nonnull final TDEConceptRequestType aConcept) {
    // This class can only deliver to "DP" concept types without child entries
    return aConcept.hasNoConceptRequestEntries () && "DP".equals (aConcept.getConceptTypeCode ().getValue ());
  }

  private static void _searchAndApplyValue (@Nonnull final TDEConceptRequestType aConcept) {

    final TDEDataElementResponseValueType aValue = new TDEDataElementResponseValueType ();
    // Whatsoever
    aValue.setAlternativeResponseIndicator (ToopXSDHelper.createIndicator (false));
    if (Math.random () < 0.1) {
      // Dummy error
      aValue.setErrorIndicator (ToopXSDHelper.createIndicator (true));
      aValue.setErrorCode (ToopXSDHelper.createCode ("MockError from DemoDP"));
    } else {
      // Dummy no error
      // Default value for error does not work
      aValue.setErrorIndicator (ToopXSDHelper.createIndicator (false));
      if (Math.random () < 0.3)
        aValue.setResponseNumeric (ToopXSDHelper.createNumeric (BigDecimal.valueOf (Math.random () * 100)));
      else if (Math.random () < 0.5)
        aValue.setResponseIdentifier (ToopXSDHelper.createIdentifier ("DemoDP-Identifier-" + Math.random ()));
      else
        aValue.setResponseCode (ToopXSDHelper.createCode ("DemoDP-Code-" + Math.random ()));
    }
    aConcept.getDataElementResponseValue ().add (aValue);
  }

  @Nonnull
  private static TDETOOPDataResponseType _createResponseFromRequest (@Nonnull final TDETOOPDataRequestType aRequest) {
    // build response
    final TDETOOPDataResponseType aResponse = ToopMessageBuilder.createResponse (aRequest);
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
    final EToopDocumentType eRequestDocType = EToopDocumentType.getFromIDOrNull (aRequest.getDocumentTypeIdentifier ()
            .getSchemeID (),
        aRequest.getDocumentTypeIdentifier ()
            .getValue ());
    boolean bFoundNewDocType = false;
    if (eRequestDocType != null) {
      final EToopDocumentType eResponseDocType = eRequestDocType.getMatchingResponseDocumentType ();
      if (eResponseDocType != null) {
        // Set new doc type in response
        ToopKafkaClient.send (EErrorLevel.INFO, () -> "Switching document type '" + eRequestDocType.getURIEncoded ()
            + "' to '" + eResponseDocType.getURIEncoded () + "'");
        aResponse.setDocumentTypeIdentifier (ToopXSDHelper.createIdentifier (eResponseDocType.getScheme (),
            eResponseDocType.getValue ()));
        bFoundNewDocType = true;
      }
    }
    if (!bFoundNewDocType) {
      ToopKafkaClient.send (EErrorLevel.INFO,
          () -> "Found no response document type for '"
              + aRequest.getDocumentTypeIdentifier ().getSchemeID () + "::"
              + aRequest.getDocumentTypeIdentifier ().getValue () + "'");
    }
    return aResponse;
  }

  public void onToopRequest (@Nonnull final TDETOOPDataRequestType aRequest) throws IOException {

    final String sRequestID = aRequest.getDataRequestIdentifier ().getValue ();
    final String sLogPrefix = "[" + sRequestID + "] ";
    ToopKafkaClient.send (EErrorLevel.INFO, () -> sLogPrefix + "Received DP Backend Request");

    // build response
    final TDETOOPDataResponseType aResponse = _createResponseFromRequest (aRequest);

    // add all the mapped values in the response
    for (final TDEDataElementRequestType aDER : aResponse.getDataElementRequest ()) {
      final TDEConceptRequestType aFirstLevelConcept = aDER.getConceptRequest ();
      if (_canUseConcept (aFirstLevelConcept)) {
        _searchAndApplyValue (aFirstLevelConcept);
      } else
        for (final TDEConceptRequestType aSecondLevelConcept : aFirstLevelConcept.getConceptRequest ())
          if (_canUseConcept (aSecondLevelConcept)) {
            _searchAndApplyValue (aSecondLevelConcept);
          } else
            for (final TDEConceptRequestType aThirdLevelConcept : aSecondLevelConcept.getConceptRequest ())
              if (_canUseConcept (aThirdLevelConcept)) {
                _searchAndApplyValue (aThirdLevelConcept);
              } else {
                // 3 level nesting is maximum
                ToopKafkaClient.send (EErrorLevel.ERROR,
                    () -> sLogPrefix + "A third level concept that is unusable - weird: "
                        + aThirdLevelConcept);
              }
    }

    // send back to toop-connector at /from-dp
    // The URL must be configured in toop-interface.properties file
    ToopInterfaceClient.sendResponseToToopConnector (aResponse);
  }
}
