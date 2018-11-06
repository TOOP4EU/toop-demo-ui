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
package eu.toop.demoui.layouts;

import com.helger.commons.error.level.EErrorLevel;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.Page;
import com.vaadin.server.Resource;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.themes.ValoTheme;

import eu.toop.kafkaclient.ToopKafkaClient;

public class ChooseAuthenticationMethodPage extends CustomLayout {
  @SuppressWarnings ("deprecation")
  public ChooseAuthenticationMethodPage () {

    super ("ChooseAuthenticationMethodPage");

    setHeight ("100%");

    final Button freedoniaCredentialsButton = new Button ("Freedonia credentials");
    freedoniaCredentialsButton.addStyleName (ValoTheme.BUTTON_BORDERLESS);
    freedoniaCredentialsButton.addStyleName (" freedonia-auth-method-button");
    addComponent (freedoniaCredentialsButton, "freedoniaCredentialsButton");

    final Button nextButton = new Button ("European eID");
    nextButton.addStyleName (ValoTheme.BUTTON_BORDERLESS);
    nextButton.addStyleName (" freedonia-auth-method-button");
    addComponent (nextButton, "nextButton");
    nextButton.addClickListener (event -> {

      ToopKafkaClient.send (EErrorLevel.INFO, () -> "[DC] Redirecting to eID Module for authentication");

      final Resource redirectResource = new ExternalResource ("/ui/redirectToEidModule");
      Page.getCurrent ().open (redirectResource, "_self", false);
    });
  }
}