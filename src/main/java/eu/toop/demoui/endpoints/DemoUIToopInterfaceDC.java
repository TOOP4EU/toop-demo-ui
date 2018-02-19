package eu.toop.demoui.endpoints;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.vaadin.ui.UI;

import eu.toop.commons.exchange.message.ToopMessageBuilder;
import eu.toop.commons.exchange.message.ToopResponseMessage;
import eu.toop.commons.exchange.mock.MSDataRequest;
import eu.toop.commons.exchange.mock.MSDataResponse;
import eu.toop.commons.exchange.mock.ToopDataRequest;
import eu.toop.commons.exchange.mock.ToopDataResponse;
import eu.toop.demoui.bean.Organization;
import eu.toop.demoui.view.StartView;
import eu.toop.iface.IToopInterfaceDC;

public class DemoUIToopInterfaceDC implements IToopInterfaceDC {

  private final UI _ui;

  public DemoUIToopInterfaceDC(final UI ui) {
    this._ui = ui;
  }

  @Override
  public void doPost(final HttpServletRequest req, final HttpServletResponse resp)
      throws ServletException, IOException {
    final PrintWriter aPW = resp.getWriter();
    aPW.write("<html><body>OK</body></html>");
    aPW.flush();

    try {
      final ToopResponseMessage bundleRead = ToopMessageBuilder.parseResponseMessage(req.getInputStream(),
          MSDataRequest.getDeserializerFunction(), ToopDataRequest.getDeserializerFunction(),
          MSDataResponse.getDeserializerFunction(), ToopDataResponse.getDeserializerFunction());

      _ui.access(() -> {
        // Push a new organization bean to the UI
        if (_ui.getNavigator().getCurrentView() instanceof StartView) {
          final StartView startView = (StartView) _ui.getNavigator().getCurrentView();
          final Organization organization = new Organization();
          // TODO: Real values are read from a retrieved ToopMessageBundle, however
          // the correct values have to be read instead. These are just placeholders.
          organization.setCompanyName(((MSDataRequest) bundleRead.getMSDataRequest()).getIdentifier());
          organization.setCompanyType(((MSDataRequest) bundleRead.getMSDataRequest()).getIdentifier());
          startView.organizationForm.setOrganizationBean(organization);
        }
      });
    } catch (final Exception e) {

    }
  }
}
