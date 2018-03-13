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
import eu.toop.kafkaclient.ToopKafkaClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.ui.UI;

import eu.toop.commons.dataexchange.TDETOOPDataRequestType;
import eu.toop.iface.IToopInterfaceDP;

public class DemoUIToopInterfaceDP implements IToopInterfaceDP {
  private static final Logger s_aLogger = LoggerFactory.getLogger (DemoUIToopInterfaceDP.class);
  private final UI _ui;

  public DemoUIToopInterfaceDP (final UI ui) {
    this._ui = ui;
  }

  public void onToopRequest (@Nonnull final TDETOOPDataRequestType aRequest) throws IOException {
    // TODO
    s_aLogger.info ("DemoUIToopInterfaceDP got " + aRequest);
    ToopKafkaClient.send (EErrorLevel.INFO, "[DP] TDETOOPDataRequestType arrived: " + aRequest);

    // TODO fetch values
    // TODO build response
    // TODO send back to toop-connector at /from-dp
  }
}
