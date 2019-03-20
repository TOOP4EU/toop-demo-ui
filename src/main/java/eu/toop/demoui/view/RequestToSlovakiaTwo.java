/**
 * Copyright (C) 2018-2019 toop.eu
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
package eu.toop.demoui.view;

import com.helger.commons.error.level.EErrorLevel;
import com.vaadin.navigator.ViewChangeListener;
import eu.toop.demoui.bean.Identity;
import eu.toop.demoui.layouts.RegisterWithWEEEMainPage;
import eu.toop.kafkaclient.ToopKafkaClient;

import java.time.LocalDate;

public class RequestToSlovakiaTwo extends BaseView {

    private String eidasAttributes = "";

    public RequestToSlovakiaTwo() {

      setCurrentPage (new RegisterWithWEEEMainPage (this));
    }

    @Override
    public void enter (ViewChangeListener.ViewChangeEvent event) {

      Identity newIdentity = new Identity ();
      newIdentity.setIdentifier ("SK/GF/31365078");
      newIdentity.setFirstName ("Jozef");
      newIdentity.setFamilyName ("Mrkvicka");
      newIdentity.setBirthPlace ("Bratislava");
      newIdentity.setBirthDate (LocalDate.of (1987, 7, 2));
      newIdentity.setNationality ("SK");

      newIdentity.setLegalPersonIdentifier ("SK/GF/31365078");
      newIdentity.setLegalPersonNationality ("SK");

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
