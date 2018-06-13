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

      if (jsonObj.get ("friendlyName").getAsString ().equals ("personIdentifier")) {
        newIdentity.setIdentifier (jsonObj.get ("value").getAsString ());
        ToopKafkaClient.send (EErrorLevel.INFO, () -> "[DC] Received eID Identifier: " + newIdentity.getIdentifier ());
      }
      if (jsonObj.get ("friendlyName").getAsString ().equals ("firstName")) {
        newIdentity.setFirstName (jsonObj.get ("value").getAsString ());
        ToopKafkaClient.send (EErrorLevel.INFO, () -> "[DC] Received eID First name: " + newIdentity.getFirstName ());
      }
      if (jsonObj.get ("friendlyName").getAsString ().equals ("familyName")) {
        newIdentity.setFamilyName (jsonObj.get ("value").getAsString ());
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
}
