package eu.toop.demoui.endpoints;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.vaadin.ui.UI;

import eu.toop.demoui.bean.Organization;
import eu.toop.demoui.view.StartView;
import eu.toop.iface.IToopInterfaceDC;
import eu.toop.iface.mockup.ToopMessageBundle;
import eu.toop.iface.mockup.ToopMessageBundleBuilder;

public class DemoUIToopInterfaceDC implements IToopInterfaceDC {

	private final UI ui;

	public DemoUIToopInterfaceDC(final UI ui) {
		this.ui = ui;
	}

	@Override
	public void doPost(final HttpServletRequest req, final HttpServletResponse resp)
			throws ServletException, IOException {
		final String pathInfo = req.getPathInfo();
		final PrintWriter aPW = resp.getWriter();
		aPW.write("<html><body>OK</body></html>");
		aPW.flush();

		try {
			ToopMessageBundle bundleRead = new ToopMessageBundleBuilder().parse(req.getInputStream());
			ui.access(() -> {
				// Push a new organization bean to the UI
				if (ui.getNavigator().getCurrentView() instanceof StartView) {
					final StartView startView = (StartView) ui.getNavigator().getCurrentView();
					final Organization organization = new Organization();
					// TODO: Real values are read from a retrieved ToopMessageBundle, however
					// the correct values have to be read instead. These are just placeholders.
					organization.setCompanyName(bundleRead.getMsDataRequest().identifier);
					organization.setCompanyType(bundleRead.getMsDataRequest().identifier);
					startView.organizationForm.setOrganizationBean(organization);
				}
			});
		} catch (Exception e) {

		}
	}
}
