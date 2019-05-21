package eu.toop.demoui;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import eu.toop.demoui.endpoints.DemoUIToopInterfaceDP;
import eu.toop.iface.ToopInterfaceManager;
import eu.toop.kafkaclient.ToopKafkaClient;
import eu.toop.kafkaclient.ToopKafkaSettings;

@WebListener
public class DPUIInitListener implements ServletContextListener
{
  public void contextInitialized (final ServletContextEvent aSce)
  {
    ToopInterfaceManager.setInterfaceDP (new DemoUIToopInterfaceDP ());

    ToopKafkaSettings.setKafkaEnabled (true);
    ToopKafkaSettings.defaultProperties ().put ("bootstrap.servers", DPUIConfig.getTrackerURL ());
    ToopKafkaSettings.setKafkaTopic (DPUIConfig.getTrackerTopic ());
  }

  public void contextDestroyed (final ServletContextEvent aSce)
  {
    ToopKafkaClient.close ();
  }
}
