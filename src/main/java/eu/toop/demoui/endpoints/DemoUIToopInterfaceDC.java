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
import com.vaadin.ui.UI;

import eu.toop.commons.dataexchange.TDEConceptRequestType;
import eu.toop.commons.dataexchange.TDEDataElementRequestType;
import eu.toop.commons.dataexchange.TDEDataElementResponseValueType;
import eu.toop.commons.dataexchange.TDETOOPDataResponseType;
import eu.toop.demoui.view.HomeView;
import eu.toop.iface.IToopInterfaceDC;
import eu.toop.kafkaclient.ToopKafkaClient;

public class DemoUIToopInterfaceDC implements IToopInterfaceDC {
  private final UI _ui;

  public DemoUIToopInterfaceDC (final UI ui) {
    this._ui = ui;
  }

  public void onToopResponse (@Nonnull final TDETOOPDataResponseType aResponse) throws IOException {
    ToopKafkaClient.send (EErrorLevel.INFO, () -> "[DC] TDETOOPDataResponseType (raw object dump): " + aResponse);

    ToopKafkaClient.send (EErrorLevel.INFO, "[DC] Received data from Data Provider: "
      + " DPIdentifier: " + aResponse.getDataProvider ().getDPIdentifier ().getValue () + ", "
      + " DPName: " + aResponse.getDataProvider ().getDPName ().getValue () + ", "
      + " DPElectronicAddressIdentifier: " + aResponse.getDataProvider ().getDPElectronicAddressIdentifier ().getValue ());

    // Inspect all mapped values
    for (final TDEDataElementRequestType aDER : aResponse.getDataElementRequest ()) {

      final TDEConceptRequestType aFirstLevelConcept = aDER.getConceptRequest ();

      final String sourceConceptName = aFirstLevelConcept.getConceptName ().getValue ();

      for (final TDEConceptRequestType aSecondLevelConcept : aFirstLevelConcept.getConceptRequest ()) {

        final String toopConceptName = aSecondLevelConcept.getConceptName ().getValue ();

        for (final TDEConceptRequestType aThirdLevelConcept : aSecondLevelConcept.getConceptRequest ()) {

          final String destinationConceptName = aThirdLevelConcept.getConceptName ().getValue ();

          String mappedConcept = sourceConceptName + " - " + toopConceptName + " - " + destinationConceptName;

          for (TDEDataElementResponseValueType aThirdLevelConceptDERValue : aThirdLevelConcept.getDataElementResponseValue ()) {
            ToopKafkaClient.send (EErrorLevel.INFO, "[DC] Received a mapped concept ( " + mappedConcept + " ), response: " + aThirdLevelConceptDERValue);

            // Push a new organization bean to the UI
            try {

              _ui.accessSynchronously (new Runnable() {
                @Override
                public void run() {
                  final HomeView homeView = (HomeView) _ui.getNavigator ().getCurrentView ();
                  if (_ui.getNavigator ().getCurrentView () instanceof HomeView) {

                    if (sourceConceptName.equals ("FreedoniaBusinessCode")) {
                      homeView.getMainCompany ().setCompanyCode (aThirdLevelConceptDERValue.getResponseCode ().getValue ());
                    }
                    if (sourceConceptName.equals ("FreedoniaAddress")) {
                      homeView.getMainCompany ().setAddressData (aThirdLevelConceptDERValue.getResponseCode ().getValue ());
                    }

                    homeView.getMainCompanyForm ().setOrganizationBean (homeView.getMainCompany ());
                    homeView.getMainCompanyForm ().save ();

                    _ui.push ();
                  }
                }
              });

            } catch (final Exception e) {

            }

          }
        }
      }
    }
  }
}
