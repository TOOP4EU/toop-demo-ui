package eu.toop.demoui.components;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;

public class FreedoniaHeader extends Header {
  public FreedoniaHeader () {
    GridLayout grid = new GridLayout(2, 1);
    grid.setMargin (false);
    grid.setWidth("100%");
    grid.setStyleName ("header freedonia");
    addComponent (grid);

    HorizontalLayout serviceTitleLayout = new HorizontalLayout ();
    serviceTitleLayout.setSizeUndefined ();

    Label countryName = new Label ("Freedonia");
    countryName.setStyleName ("countryName");
    serviceTitleLayout.addComponent (countryName);

    Label serviceName = new Label ("| Online public services portal");
    serviceName.setStyleName ("serviceName");
    serviceTitleLayout.addComponent (serviceName);

    grid.addComponent (serviceTitleLayout, 0, 0);
    grid.setComponentAlignment (serviceTitleLayout, Alignment.MIDDLE_LEFT);

    Label serviceInfoBar = new Label ("About | Contact | Help ");
    serviceInfoBar.setStyleName ("serviceInfoBar");
    grid.addComponent (serviceInfoBar, 1, 0);
    grid.setComponentAlignment (serviceInfoBar, Alignment.MIDDLE_RIGHT);
  }
}
