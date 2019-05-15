package eu.toop.demoui;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import eu.toop.kafkaclient.ToopKafkaClient;
import eu.toop.kafkaclient.ToopKafkaSettings;

@WebListener
public class DCUIInitListener implements ServletContextListener {

  public void contextInitialized (final ServletContextEvent aSce) {
    ToopKafkaSettings.setKafkaEnabled (true);
    ToopKafkaSettings.defaultProperties ().put ("bootstrap.servers", DCUIConfig.getTrackerURL ());
    ToopKafkaSettings.setKafkaTopic (DCUIConfig.getTrackerTopic ());
  }

  public void contextDestroyed (final ServletContextEvent aSce) {
    ToopKafkaClient.close ();
  }
}
