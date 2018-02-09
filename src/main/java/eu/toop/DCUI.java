package eu.toop;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import eu.toop.view.StartView;

/**
 * This UI is the application entry point. A UI may either represent a browser window 
 * (or tab) or some part of an HTML page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be 
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@Theme("DCUITheme")
public class DCUI extends UI {

    Navigator navigator;

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        getPage().setTitle("DC user interface");

        navigator = new Navigator(this, this);
        navigator.addView("", new StartView());
    }

    @WebServlet(urlPatterns = "/*", name = "DCUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = DCUI.class, productionMode = false)
    public static class DCUIServlet extends VaadinServlet {
    }
}
