package eu.toop.demoui.view;

import com.google.gson.*;
import com.helger.commons.error.level.EErrorLevel;
import com.vaadin.navigator.ViewChangeListener;
import eu.toop.demoui.bean.Identity;
import eu.toop.demoui.layouts.RegisterWithWEEEMainPage;
import eu.toop.kafkaclient.ToopKafkaClient;

public class PhaseTwo extends BaseView {

  private String eidasAttributes = "";

  public PhaseTwo () {

    setCurrentPage (new RegisterWithWEEEMainPage (this));
  }

  @Override
  public void enter (ViewChangeListener.ViewChangeEvent event) {

    Identity newIdentity = new Identity ();

    JsonObject jsonObject = new JsonParser ().parse (eidasAttributes).getAsJsonObject ();
    JsonArray attr = jsonObject.get ("eidasAttributes").getAsJsonArray ();
    for (JsonElement element : attr) {
      JsonObject jsonObj = element.getAsJsonObject ();

      final String friendlyName = jsonObj.get ("friendlyName").getAsString ();
      final String value = jsonObj.get ("value").getAsString ();

      if (friendlyName.equals ("personIdentifier")) {
        newIdentity.setIdentifier (value);
        ToopKafkaClient.send (EErrorLevel.INFO, () -> "[DC] Received eID Identifier: " + newIdentity.getIdentifier ());
      }
      if (friendlyName.equals ("firstName")) {
        newIdentity.setFirstName (value);
        ToopKafkaClient.send (EErrorLevel.INFO, () -> "[DC] Received eID First name: " + newIdentity.getFirstName ());
      }
      if (friendlyName.equals ("familyName")) {
        newIdentity.setFamilyName (value);
        ToopKafkaClient.send (EErrorLevel.INFO, () -> "[DC] Received eID Family name: " + newIdentity.getFamilyName ());
      }
    }
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
