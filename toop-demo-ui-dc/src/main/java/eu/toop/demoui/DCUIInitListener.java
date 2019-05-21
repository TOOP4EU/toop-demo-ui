package eu.toop.demoui;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import eu.toop.demoui.endpoints.DemoUIToopInterfaceDC;
import eu.toop.iface.ToopInterfaceManager;
import eu.toop.kafkaclient.ToopKafkaClient;
import eu.toop.kafkaclient.ToopKafkaSettings;

@WebListener
public class DCUIInitListener implements ServletContextListener {
    public void contextInitialized(final ServletContextEvent aSce) {
	ToopInterfaceManager.setInterfaceDC(new DemoUIToopInterfaceDC(x -> DCUI.hackedThis.access(x)));

	ToopKafkaSettings.setKafkaEnabled(true);
	ToopKafkaSettings.defaultProperties().put("bootstrap.servers", DCUIConfig.getTrackerURL());
	ToopKafkaSettings.setKafkaTopic(DCUIConfig.getTrackerTopic());
    }

    public void contextDestroyed(final ServletContextEvent aSce) {
	ToopKafkaClient.close();
    }
}
