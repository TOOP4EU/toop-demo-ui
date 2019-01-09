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

import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Nonnull;

import com.helger.commons.error.level.EErrorLevel;
import com.vaadin.navigator.Navigator;
import com.vaadin.ui.UI;

import eu.toop.commons.dataexchange.v120.TDEConceptRequestType;
import eu.toop.commons.dataexchange.v120.TDEDataElementRequestType;
import eu.toop.commons.dataexchange.v120.TDEDataElementResponseValueType;
import eu.toop.commons.dataexchange.v120.TDEDataProviderType;
import eu.toop.commons.dataexchange.v120.TDETOOPResponseType;
import eu.toop.commons.jaxb.ToopWriter;
import eu.toop.demoui.DCUIConfig;
import eu.toop.demoui.bean.MainCompany;
import eu.toop.demoui.layouts.RegisterWithWEEEMainPage;
import eu.toop.demoui.view.PhaseTwo;
import eu.toop.demoui.view.RequestToSloveniaOne;
import eu.toop.demoui.view.RequestToSwedenOne;
import eu.toop.demoui.view.RequestToSwedenTwo;
import eu.toop.iface.IToopInterfaceDC;
import eu.toop.kafkaclient.ToopKafkaClient;

public class DemoUIToopInterfaceDC implements IToopInterfaceDC {
  private final UI ui;

  public DemoUIToopInterfaceDC (final UI ui) {

    this.ui = ui;
  }

  public void onToopResponse (@Nonnull final TDETOOPResponseType aResponse) throws IOException {

    dumpResponse (aResponse);

    final String sRequestID = aResponse.getDataRequestIdentifier ().getValue ();
    final String sLogPrefix = "[" + sRequestID + "] [DC] ";

    final TDEDataProviderType aDP = aResponse.getDataProviderCount () == 0 ? null
        : aResponse.getDataProviderAtIndex (0);

    ToopKafkaClient.send (EErrorLevel.INFO,
        () -> sLogPrefix + "Received data from Data Provider: "
            + (aDP == null ? "null"
                : " DPIdentifier: " + aDP.getDPIdentifier ().getValue () + ", " + " DPName: "
                    + aDP.getDPName ().getValue () + ", " + " DPElectronicAddressIdentifier: "
                    + aDP.getDPElectronicAddressIdentifier ().getValue ()));

    // Push a new organization bean to the UI
    try {
      ui.access ( () -> {

        final UI threadUI = UI.getCurrent ();
        ToopKafkaClient.send (EErrorLevel.INFO, () -> sLogPrefix + "Current UI: " + threadUI);
        final Navigator threadUINavigator = threadUI.getNavigator ();
        ToopKafkaClient.send (EErrorLevel.INFO, () -> sLogPrefix + "Current Navigator: " + threadUINavigator);

        final MainCompany bean = new MainCompany ();

        // Inspect all mapped values
        for (final TDEDataElementRequestType aDER : aResponse.getDataElementRequest ()) {

          final TDEConceptRequestType aFirstLevelConcept = aDER.getConceptRequest ();

          final String sourceConceptName = aFirstLevelConcept.getConceptName ().getValue ();

          for (final TDEConceptRequestType aSecondLevelConcept : aFirstLevelConcept.getConceptRequest ()) {

            final String toopConceptName = aSecondLevelConcept.getConceptName ().getValue ();

            for (final TDEConceptRequestType aThirdLevelConcept : aSecondLevelConcept.getConceptRequest ()) {

              final String destinationConceptName = aThirdLevelConcept.getConceptName ().getValue ();

              final String mappedConcept = sourceConceptName + " - " + toopConceptName + " - " + destinationConceptName;

              for (final TDEDataElementResponseValueType aThirdLevelConceptDERValue : aThirdLevelConcept
                  .getDataElementResponseValue ()) {
                ToopKafkaClient.send (EErrorLevel.INFO, sLogPrefix + "Received a mapped concept ( " + mappedConcept
                    + " ), response: " + aThirdLevelConceptDERValue);

                String aValue = "";
                if (aThirdLevelConceptDERValue.getResponseCode () != null) {
                  aValue = aThirdLevelConceptDERValue.getResponseCode ().getValue ();
                } else if (aThirdLevelConceptDERValue.getResponseIdentifier () != null) {
                  aValue = aThirdLevelConceptDERValue.getResponseIdentifier ().getValue ();
                } else if (aThirdLevelConceptDERValue.getResponseNumeric () != null
                    && aThirdLevelConceptDERValue.getResponseNumeric ().getValue () != null) {
                  aValue = aThirdLevelConceptDERValue.getResponseNumeric ().getValue ().toString ();
                } else if (aThirdLevelConceptDERValue.getResponseDescription () != null
                    && aThirdLevelConceptDERValue.getResponseDescription ().getValue () != null) {
                  aValue = aThirdLevelConceptDERValue.getResponseDescription ().getValue ();
                } else {
                  ToopKafkaClient.send (EErrorLevel.WARN, () -> sLogPrefix + "Unsupported response value provided: "
                      + aThirdLevelConceptDERValue.toString ());
                }

                if (sourceConceptName.equals ("FreedoniaAddress")) {
                  bean.setAddress (aValue);
                } else if (sourceConceptName.equals ("FreedoniaSSNumber")) {
                  bean.setSSNumber (aValue);
                } else if (sourceConceptName.equals ("FreedoniaBusinessCode")) {
                  bean.setBusinessCode (aValue);
                } else if (sourceConceptName.equals ("FreedoniaVATNumber")) {
                  bean.setVATNumber (aValue);
                } else if (sourceConceptName.equals ("FreedoniaCompanyType")) {
                  bean.setCompanyType (aValue);
                } else if (sourceConceptName.equals ("FreedoniaRegistrationDate")) {
                  bean.setRegistrationDate (aValue);
                } else if (sourceConceptName.equals ("FreedoniaRegistrationNumber")) {
                  bean.setRegistrationNumber (aValue);
                } else if (sourceConceptName.equals ("FreedoniaCompanyName")) {
                  bean.setCompanyName (aValue);
                } else if (sourceConceptName.equals ("FreedoniaCompanyNaceCode")) {
                  bean.setCompanyNaceCode (aValue);
                } else if (sourceConceptName.equals ("FreedoniaActivityDeclaration")) {
                  bean.setActivityDeclaration (aValue);
                } else if (sourceConceptName.equals ("FreedoniaRegistrationAuthority")) {
                  bean.setRegistrationAuthority (aValue);
                } else if (sourceConceptName.equals ("FreedoniaLegalStatus")) {
                  bean.setLegalStatus (aValue);
                } else if (sourceConceptName.equals ("FreedoniaLegalStatusEffectiveDate")) {
                  bean.setLegalStatusEffectiveDate (aValue);
                } else {
                  ToopKafkaClient.send (EErrorLevel.WARN,
                      () -> sLogPrefix + "Unsupported source concept name: '" + sourceConceptName + "'");
                }
              }
            }
          }
        }

        if (threadUINavigator.getCurrentView () instanceof PhaseTwo) {
          final PhaseTwo homeView = (PhaseTwo) threadUINavigator.getCurrentView ();
          if (homeView.getCurrentPage () instanceof RegisterWithWEEEMainPage) {
            homeView.setMainCompany (bean);
            final RegisterWithWEEEMainPage page = (RegisterWithWEEEMainPage) homeView.getCurrentPage ();
            page.addMainCompanyForm ();
          }
        } else if (threadUINavigator.getCurrentView () instanceof RequestToSwedenOne) {
          final RequestToSwedenOne homeView = (RequestToSwedenOne) threadUINavigator.getCurrentView ();
          if (homeView.getCurrentPage () instanceof RegisterWithWEEEMainPage) {
            homeView.setMainCompany (bean);
            final RegisterWithWEEEMainPage page = (RegisterWithWEEEMainPage) homeView.getCurrentPage ();
            page.addMainCompanyForm ();
          }
        } else if (threadUINavigator.getCurrentView () instanceof RequestToSwedenTwo) {
          final RequestToSwedenTwo homeView = (RequestToSwedenTwo) threadUINavigator.getCurrentView ();
          if (homeView.getCurrentPage () instanceof RegisterWithWEEEMainPage) {
            homeView.setMainCompany (bean);
            final RegisterWithWEEEMainPage page = (RegisterWithWEEEMainPage) homeView.getCurrentPage ();
            page.addMainCompanyForm ();
          }
        } else if (threadUINavigator.getCurrentView () instanceof RequestToSloveniaOne) {
          final RequestToSloveniaOne homeView = (RequestToSloveniaOne) threadUINavigator.getCurrentView ();
          if (homeView.getCurrentPage () instanceof RegisterWithWEEEMainPage) {
            homeView.setMainCompany (bean);
            final RegisterWithWEEEMainPage page = (RegisterWithWEEEMainPage) homeView.getCurrentPage ();
            page.addMainCompanyForm ();
          }
        }

        ToopKafkaClient.send (EErrorLevel.INFO, () -> sLogPrefix + "Pushed new bean data to the Demo UI: " + bean);
      });
    } catch (final Exception e) {
      ToopKafkaClient.send (EErrorLevel.INFO, () -> sLogPrefix + "Failed to push new bean data to the Demo UI", e);
    }
  }

  private void dumpResponse (@Nonnull final TDETOOPResponseType aResponse) {

    FileWriter fw = null;
    try {

      final DateFormat dateFormat = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss.SSS");
      final String filePath = String.format ("%s/response-dump-%s.log", DCUIConfig.getDumpResponseDirectory (),
          dateFormat.format (new Date ()));

      final String responseXml = ToopWriter.response ().getAsString (aResponse);
      fw = new FileWriter (filePath);
      if (responseXml != null) {
        fw.write (responseXml);
      }
    } catch (final IOException e) {
      e.printStackTrace ();
    } finally {
      if (fw != null) {
        try {
          fw.close ();
        } catch (final IOException e) {
          e.printStackTrace ();
        }
      }
    }
  }
}
