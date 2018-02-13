package eu.toop.demoui.endpoints;

import eu.toop.iface.ITOOPInterfaceDC;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class TOOPInterfaceDC implements ITOOPInterfaceDC {
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final String pathInfo = req.getPathInfo();
        final PrintWriter aPW = resp.getWriter();
        aPW.write("<html><body><h1>TOOPInterfaceDC (doGet)</h1>");
        aPW.write("<div>Path info: " + pathInfo + "</div>");
        aPW.write("<div>Parameter map: " + req.getParameterMap() + "</div>");
        aPW.write("</body></html>");
        aPW.flush();
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final String pathInfo = req.getPathInfo();
        final PrintWriter aPW = resp.getWriter();
        aPW.write("<html><body><h1>TOOPInterfaceDC (doPost)</h1>");
        aPW.write("<div>Path info: " + pathInfo + "</div>");
        aPW.write("<div>Parameter map: " + req.getParameterMap() + "</div>");
        aPW.write("</body></html>");
        aPW.flush();
    }
}
