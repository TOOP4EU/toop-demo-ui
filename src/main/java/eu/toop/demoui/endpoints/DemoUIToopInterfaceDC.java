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
package eu.toop.demoui.endpoints;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.vaadin.ui.UI;

import eu.toop.commons.dataexchange.TDETOOPDataResponseType;
import eu.toop.commons.exchange.ToopMessageBuilder;
import eu.toop.demoui.view.HomeView;
import eu.toop.iface.IToopInterfaceDC;

public class DemoUIToopInterfaceDC implements IToopInterfaceDC {

  private final UI _ui;

  public DemoUIToopInterfaceDC (final UI ui) {
    this._ui = ui;
  }

  @Override
  public void doPost (final HttpServletRequest req,
                      final HttpServletResponse resp) throws ServletException, IOException {
    final PrintWriter aPW = resp.getWriter ();
    aPW.write ("<html><body>OK</body></html>");
    aPW.flush ();

    try {
      final TDETOOPDataResponseType bundleRead = ToopMessageBuilder.parseResponseMessage (req.getInputStream ());

      _ui.access ( () -> {
        // Push a new organization bean to the UI
        if (_ui.getNavigator ().getCurrentView () instanceof HomeView) {
          // final MainCompanyView mainCompanyView = (MainCompanyView) _ui.getNavigator
          // ().getCurrentView ();
          // final MainCompany mainCompany = new MainCompany ();
          // TODO: Real values are read from a retrieved ToopMessageBundle, however
          // the correct values have to be read instead. These are just placeholders.
          // mainCompany.setCompanyName (bundleRead.getToopDataRequest ().getRequestID
          // ());
          // mainCompany.setCompanyType (bundleRead.getToopDataRequest ().getRequestID
          // ());
          // mainCompanyView.getOrganizationForm ().setOrganizationBean (mainCompany);
        }
      });
    } catch (final Exception e) {

    }
  }
}
