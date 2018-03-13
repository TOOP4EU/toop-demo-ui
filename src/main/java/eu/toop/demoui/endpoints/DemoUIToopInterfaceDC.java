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

import javax.annotation.Nonnull;

import com.helger.commons.error.level.EErrorLevel;
import com.vaadin.ui.UI;

import eu.toop.commons.dataexchange.TDETOOPDataResponseType;
import eu.toop.demoui.view.HomeView;
import eu.toop.iface.IToopInterfaceDC;
import eu.toop.kafkaclient.ToopKafkaClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DemoUIToopInterfaceDC implements IToopInterfaceDC {

  private static final Logger s_aLogger = LoggerFactory.getLogger (DemoUIToopInterfaceDC.class);
  private final UI _ui;

  public DemoUIToopInterfaceDC (final UI ui) {
    this._ui = ui;
  }

  public void onToopResponse (@Nonnull final TDETOOPDataResponseType aResponse) throws IOException {

    s_aLogger.info ("DemoUIToopInterfaceDC got " + aResponse);
    ToopKafkaClient.send (EErrorLevel.INFO, "[DC] TDETOOPDataResponseType arrived: " + aResponse);

    try {
      _ui.access ( () -> {
        // Push a new organization bean to the UI
        if (_ui.getNavigator ().getCurrentView () instanceof HomeView) {
          final HomeView homeView = (HomeView) _ui.getNavigator ().getCurrentView ();

          homeView.getMainCompany ().setCompanyName("Mockup Company Name");
          homeView.getMainCompany ().setCompanyType("Mockup Company Type");
          homeView.getMainCompanyForm ().setOrganizationBean (homeView.getMainCompany ());
          homeView.getMainCompanyForm ().save ();
        }
      });
    } catch (final Exception e) {

    }
  }
}
