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
	aPW.write("<html><body><h1>TOOPInterfaceDC (doPost)</h1>");
	aPW.write("<div>Path info: " + pathInfo + "</div>");
	aPW.write("<div>Parameter map: " + req.getParameterMap() + "</div>");
	aPW.write("</body></html>");
	aPW.flush();

	// TODO: Read the HttpServletRequest input stream and deserialize into a
	// TOOPMessageBundle so that we can use its content to populate the organization
	// form in the UI.

	ui.access(() -> {
	    // Push a new organization bean to the UI
	    if (ui.getNavigator().getCurrentView() instanceof StartView) {
		final StartView startView = (StartView) ui.getNavigator().getCurrentView();
		final Organization organization = new Organization();
		// TODO: Replace this mockup data with real data fetched from a
		// TOOPMessageBundle
		organization.setCompanyName("Zizi mat Ltd");
		// TODO: Replace this mockup data with real data fetched from a
		// TOOPMessageBundle
		organization.setCompanyType("Limited partnership");
		startView.organizationForm.setOrganizationBean(organization);
	    }
	});
    }
}
