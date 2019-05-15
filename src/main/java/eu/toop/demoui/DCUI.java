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
package eu.toop.demoui;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;

import com.helger.commons.http.CHttpHeader;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.UI;

import eu.toop.demoui.endpoints.DemoUIToopInterfaceDC;
import eu.toop.demoui.endpoints.DemoUIToopInterfaceDP;
import eu.toop.demoui.view.DynamicRequest;
import eu.toop.demoui.view.MockRequestToSwedenDPOne;
import eu.toop.demoui.view.MockRequestToSwedenDPTwo;
import eu.toop.demoui.view.PhaseOne;
import eu.toop.demoui.view.PhaseTwo;
import eu.toop.demoui.view.RequestToItalyOne;
import eu.toop.demoui.view.RequestToSlovakiaOne;
import eu.toop.demoui.view.RequestToSlovakiaTwo;
import eu.toop.demoui.view.RequestToSloveniaOne;
import eu.toop.demoui.view.RequestToSwedenOne;
import eu.toop.demoui.view.RequestToSwedenTwo;
import eu.toop.iface.ToopInterfaceManager;
import eu.toop.kafkaclient.ToopKafkaClient;

/**
 * This UI is the application entry point. A UI may either represent a browser
 * window (or tab) or some part of an HTML page where a Vaadin application is
 * embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is
 * intended to be overridden to add component to the user interface and
 * initialize non-component functionality.
 */
@Theme ("DCUITheme")
public class DCUI extends UI {

  @Override
  protected void init (final VaadinRequest vaadinRequest) {

    // Add a custom request handler
    VaadinSession.getCurrent ().addRequestHandler ( (session, request, response) -> {

      if ("/redirectToEidModule".equals (request.getPathInfo ())) {
        // Redirect to eID Module
        response.setStatus (HttpServletResponse.SC_TEMPORARY_REDIRECT);
        response.setHeader (CHttpHeader.LOCATION, DCUIConfig.getEidModuleURL ());

        return true;
      }
      return false;
    });

    setPollInterval (1000);
    getPage ().setTitle ("TOOP Demo User Interface");

    // CCTF-163: move this somehow to DCUIInitListener
    ToopInterfaceManager.setInterfaceDC (new DemoUIToopInterfaceDC (this));
    ToopInterfaceManager.setInterfaceDP (new DemoUIToopInterfaceDP ());

    final Navigator navigator = new Navigator (this, this);
    navigator.addView ("", new PhaseOne ());
    navigator.addView ("PhaseOne", new PhaseOne ());
    final PhaseTwo phaseTwo = new PhaseTwo ();
    navigator.addView ("loginSuccess", phaseTwo);
    final RequestToSwedenOne requestToSwedenOne = new RequestToSwedenOne ();
    navigator.addView ("requestToSwedenOne", requestToSwedenOne);

    final RequestToSwedenTwo requestToSwedenTwo = new RequestToSwedenTwo ();
    navigator.addView ("requestToSwedenTwo", requestToSwedenTwo);

    final RequestToSloveniaOne requestToSloveniaOne = new RequestToSloveniaOne ();
    navigator.addView ("requestToSloveniaOne", requestToSloveniaOne);

    final RequestToSlovakiaOne requestToSlovakiaOne = new RequestToSlovakiaOne ();
    navigator.addView ("requestToSlovakiaOne", requestToSlovakiaOne);
    final RequestToSlovakiaTwo requestToSlovakiaTwo = new RequestToSlovakiaTwo ();
    navigator.addView ("requestToSlovakiaTwo", requestToSlovakiaTwo);

    final RequestToItalyOne requestToItalyOne = new RequestToItalyOne ();
    navigator.addView ("requestToItalyOne", requestToItalyOne);

    // Temporary mock endpoints for ToopRequests to the Swedish pilot
    final MockRequestToSwedenDPOne mockRequestToSwedenDPOne = new MockRequestToSwedenDPOne ();
    navigator.addView ("mockRequestToSwedenDPOne", mockRequestToSwedenDPOne);
    final MockRequestToSwedenDPTwo mockRequestToSwedenDPTwo = new MockRequestToSwedenDPTwo ();
    navigator.addView ("mockRequestToSwedenDPTwo", mockRequestToSwedenDPTwo);

    final DynamicRequest dynamicRequest = new DynamicRequest();
    navigator.addView("dynamicRequest", dynamicRequest);

    final String eidasAttributes = vaadinRequest.getParameter ("eidasAttributes");
    if (eidasAttributes != null) {
      phaseTwo.setEidasAttributes (eidasAttributes);
      navigator.navigateTo ("loginSuccess");
    }
  }

  @Override
  public void close () {
    try {
      ToopKafkaClient.close ();
    } finally {
      super.close ();
    }
  }

  @WebServlet (urlPatterns = { "/ui/*", "/VAADIN/*" }, asyncSupported = true)
  @VaadinServletConfiguration (ui = DCUI.class, productionMode = false)
  public static class DCUIServlet extends VaadinServlet {
    // empty
  }
}
