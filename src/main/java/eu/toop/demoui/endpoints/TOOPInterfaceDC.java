package eu.toop.demoui.endpoints;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.vaadin.server.VaadinSession;
import com.vaadin.ui.UI;
import eu.toop.iface.ITOOPInterfaceDC;

public class TOOPInterfaceDC implements ITOOPInterfaceDC {
    public void doPost(final HttpServletRequest req, final HttpServletResponse resp)
	    throws ServletException, IOException {
        final String pathInfo = req.getPathInfo();
        final PrintWriter aPW = resp.getWriter();
        aPW.write("<html><body><h1>TOOPInterfaceDC (doPost)</h1>");
        aPW.write("<div>Path info: " + pathInfo + "</div>");
        aPW.write("<div>Parameter map: " + req.getParameterMap() + "</div>");
        aPW.write("</body></html>");
        aPW.flush();
    }
}
