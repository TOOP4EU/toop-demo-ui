package eu.toop.demoui;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;

import eu.toop.demoui.endpoints.TOOPInterfaceDC;
import eu.toop.demoui.endpoints.TOOPInterfaceDP;
import eu.toop.demoui.view.StartView;
import eu.toop.demoui.view.eIDModuleView;
import eu.toop.iface.ToopInterface;

/**
 * This UI is the application entry point. A UI may either represent a browser window
 * (or tab) or some part of an HTML page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@Theme("DCUITheme")
public class DCUI extends UI {

    private Navigator navigator;

    @Override
    protected void init(final VaadinRequest vaadinRequest) {
        ToopInterface.setInterfaceDC(new TOOPInterfaceDC(this));
        ToopInterface.setInterfaceDP(new TOOPInterfaceDP(this));

        getPage().setTitle("TOOP Demo User Interface");

        navigator = new Navigator(this, this);
        navigator.addView("", new StartView());
        navigator.addView("eIDModuleView", new eIDModuleView());
    }

    @WebServlet(urlPatterns = {"/ui/*", "/VAADIN/*"}, asyncSupported = true)
    @VaadinServletConfiguration(ui = DCUI.class, productionMode = false)
    public static class DCUIServlet extends VaadinServlet {
    }
}
