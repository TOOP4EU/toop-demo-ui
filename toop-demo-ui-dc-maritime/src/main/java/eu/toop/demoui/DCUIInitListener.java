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
package eu.toop.demoui;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.apache.kafka.clients.producer.ProducerConfig;

import eu.toop.demoui.endpoints.DemoUIToopInterfaceDC;
import eu.toop.iface.ToopInterfaceManager;
import eu.toop.kafkaclient.ToopKafkaClient;
import eu.toop.kafkaclient.ToopKafkaSettings;

@WebListener
public class DCUIInitListener implements ServletContextListener {
  public void contextInitialized (final ServletContextEvent aSce) {
    ToopInterfaceManager.setInterfaceDC (new DemoUIToopInterfaceDC ());

    if (DCUIConfig.isTrackerEnabled ()) {
      ToopKafkaSettings.setKafkaEnabled (true);
      ToopKafkaSettings.defaultProperties ().put (ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, DCUIConfig.getTrackerURL ());
      ToopKafkaSettings.setKafkaTopic (DCUIConfig.getTrackerTopic ());
    } else
      ToopKafkaSettings.setKafkaEnabled (false);
  }

  public void contextDestroyed (final ServletContextEvent aSce) {
    ToopKafkaClient.close ();
  }
}
