package eu.toop.demoui.view;

import com.google.gson.*;
import com.helger.commons.error.level.EErrorLevel;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import eu.toop.demoui.bean.Identity;
import eu.toop.demoui.bean.MainCompany;
import eu.toop.demoui.bean.NewCompany;
import eu.toop.demoui.form.MainCompanyForm;
import eu.toop.demoui.pages.HomePage;
import eu.toop.demoui.pages.RegisterWithWEEEMainPage;
import eu.toop.kafkaclient.ToopKafkaClient;

public class PhaseTwo extends BaseView {

  public String eidasAttributes = "";

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

      if (jsonObj.get ("friendlyName").getAsString ().equals ("firstName")) {
        newIdentity.setFirstName (jsonObj.get ("value").getAsString ());
        ToopKafkaClient.send (EErrorLevel.INFO, () -> "[DC] Received eID Identifier: " + newIdentity.getFirstName ());
      }
      if (jsonObj.get ("friendlyName").getAsString ().equals ("familyName")) {
        newIdentity.setFamilyName (jsonObj.get ("value").getAsString ());
        ToopKafkaClient.send (EErrorLevel.INFO, () -> "[DC] Received eID First name: " + newIdentity.getFamilyName ());
      }
      if (jsonObj.get ("friendlyName").getAsString ().equals ("personIdentifier")) {
        newIdentity.setIdentifier (jsonObj.get ("value").getAsString ());
        ToopKafkaClient.send (EErrorLevel.INFO, () -> "[DC] Received eID Family name: " + newIdentity.getIdentifier ());
      }
    }
    setIdentity (newIdentity);

    setCurrentPage (new RegisterWithWEEEMainPage (this));
  }
}
