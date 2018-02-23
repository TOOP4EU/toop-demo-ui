package eu.toop.demoui.components;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;

public class EloniaHeader extends Header {
  public EloniaHeader () {
    GridLayout grid = new GridLayout(2, 1);
    grid.setMargin (false);
    grid.setWidth("100%");
    grid.setStyleName ("headerElonia");
    addComponent (grid);

    HorizontalLayout serviceTitleLayout = new HorizontalLayout ();
    serviceTitleLayout.setSizeUndefined ();

    Label countryName = new Label ("Elonia");
    countryName.setStyleName ("countryName");
    serviceTitleLayout.addComponent (countryName);

    Label serviceName = new Label ("| Identity provider");
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
