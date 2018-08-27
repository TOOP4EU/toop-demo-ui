package eu.toop.demoui.view;

import java.time.LocalDate;

import com.helger.commons.error.level.EErrorLevel;
import com.vaadin.navigator.ViewChangeListener;

import eu.toop.demoui.bean.Identity;
import eu.toop.demoui.layouts.RegisterWithWEEEMainPage;
import eu.toop.kafkaclient.ToopKafkaClient;

public class RequestToSwedenOne extends BaseView {

    private String eidasAttributes = "";

    public RequestToSwedenOne () {

      setCurrentPage (new RegisterWithWEEEMainPage (this));
    }

    @Override
    public void enter (ViewChangeListener.ViewChangeEvent event) {

      Identity newIdentity = new Identity ();
      newIdentity.setIdentifier ("SE/GF/199005109999");
      newIdentity.setFirstName ("Sven");
      newIdentity.setFamilyName ("Svensson");
      newIdentity.setBirthPlace ("Stockholm");
      newIdentity.setBirthDate (LocalDate.of (1990, 05, 10));
      newIdentity.setNationality ("SE");

      newIdentity.setLegalPersonIdentifier ("SE/GF/5591051858");
      newIdentity.setLegalPersonName ("Testbolag 1 AB");
      newIdentity.setLegalPersonNationality ("SE");

      ToopKafkaClient.send (EErrorLevel.INFO, () -> "[DC] Received eID Identifier: " + newIdentity.getIdentifier ());
      ToopKafkaClient.send (EErrorLevel.INFO, () -> "[DC] Received eID First name: " + newIdentity.getFirstName ());
      ToopKafkaClient.send (EErrorLevel.INFO, () -> "[DC] Received eID Family name: " + newIdentity.getFamilyName ());

      setIdentity (newIdentity);

      setCurrentPage (new RegisterWithWEEEMainPage (this));
    }

    public String getEidasAttributes () {

      return eidasAttributes;
    }

    public void setEidasAttributes (String eidasAttributes) {

      this.eidasAttributes = eidasAttributes;
    }

    @Override
    public boolean equals(Object obj) {
      return super.equals (obj);
    }

    @Override
    public int hashCode() {
      return super.hashCode ();
    }
}
