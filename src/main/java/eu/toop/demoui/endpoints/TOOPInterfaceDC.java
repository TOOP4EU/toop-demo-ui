package eu.toop.demoui.endpoints;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.vaadin.ui.UI;
import eu.toop.demoui.bean.Organization;
import eu.toop.demoui.view.StartView;
import eu.toop.iface.ITOOPInterfaceDC;
import eu.toop.iface.mockup.TOOPMessageBundle;
import eu.toop.iface.mockup.TOOPMessageBundleBuilder;

public class TOOPInterfaceDC implements ITOOPInterfaceDC {

    private UI ui;

    public TOOPInterfaceDC(UI ui) {
        this.ui = ui;
    }

    @Override
    public void doPost(final HttpServletRequest req, final HttpServletResponse resp)
	    throws ServletException, IOException {
        final PrintWriter aPW = resp.getWriter();
        aPW.write("<html><body>OK</body></html>");
        aPW.flush();

        try {
            TOOPMessageBundle bundleRead = new TOOPMessageBundleBuilder().parse(req.getInputStream());
            ui.access(() -> {
                // Push a new organization bean to the UI
                if (ui.getNavigator().getCurrentView() instanceof StartView) {
                    StartView startView = (StartView)ui.getNavigator().getCurrentView();
                    Organization organization = new Organization();
                    organization.setCompanyName(bundleRead.getMsDataRequest().identifier); // TODO: Actual data from the retrieved ToopMessageBundle, however it is the wrong attribute. Should be the company name.
                    organization.setCompanyType("Limited partnership"); // TODO: Replace this mockup data with real data fetched from a TOOPMessageBundle
                    startView.organizationForm.setOrganizationBean(organization);
                }
            });
        } catch (Exception e) {

        }
    }
}
