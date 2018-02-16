package eu.toop.demoui.endpoints;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.vaadin.ui.UI;

import eu.toop.iface.IToopInterfaceDP;

public class DemoUIToopInterfaceDP implements IToopInterfaceDP {

    private final UI ui;

    public DemoUIToopInterfaceDP(final UI ui) {
	this.ui = ui;
    }

    @Override
    public void doPost(final HttpServletRequest req, final HttpServletResponse resp)
	    throws ServletException, IOException {
	final String pathInfo = req.getPathInfo();
	final PrintWriter aPW = resp.getWriter();
	aPW.write("<html><body><h1>TOOPInterfaceDP (doPost)</h1>");
	aPW.write("<div>Path info: " + pathInfo + "</div>");
	aPW.write("<div>Parameter map: " + req.getParameterMap() + "</div>");
	aPW.write("</body></html>");
	aPW.flush();
    }
}
