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

import eu.toop.iface.IToopInterfaceDP;

public class DemoUIToopInterfaceDP implements IToopInterfaceDP {

  private final UI _ui;

  public DemoUIToopInterfaceDP(final UI ui) {
    this._ui = ui;
  }

  @Override
  public void doPost(final HttpServletRequest req, final HttpServletResponse resp)
      throws ServletException, IOException {
    final String pathInfo = req.getPathInfo();
    final PrintWriter aPW = resp.getWriter();
    aPW.write("<html><body>OK</body></html>");
    aPW.flush();
  }
}
