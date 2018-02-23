package eu.toop.demoui.components;

import com.vaadin.ui.*;

public class Body extends VerticalLayout {
  public Body (VerticalLayout mainColumn) {
    setSizeFull ();
    setMargin (false);
    setStyleName ("body");

    GridLayout grid = new GridLayout(1, 1);
    grid.setMargin (false);
    grid.setWidth ("100%");
    grid.setHeight ("100%");
    addComponent (grid);

    HorizontalLayout serviceTitleLayout = new HorizontalLayout ();
    serviceTitleLayout.setSizeUndefined ();

    mainColumn.setMargin (false);
    mainColumn.setSizeFull ();
    mainColumn.setStyleName ("mainColumn");

    grid.addComponent (mainColumn, 0, 0);
    grid.setComponentAlignment (mainColumn, Alignment.TOP_LEFT);

    grid.setColumnExpandRatio(1, 500);
  }

  public Body (VerticalLayout leftColumn, VerticalLayout rightColumn) {
    setSizeFull ();
    setMargin (false);
    setStyleName ("body");

    GridLayout grid = new GridLayout(2, 1);
    grid.setMargin (false);
    grid.setWidth ("100%");
    grid.setHeight ("100%");
    addComponent (grid);

    HorizontalLayout serviceTitleLayout = new HorizontalLayout ();
    serviceTitleLayout.setSizeUndefined ();

    leftColumn.setMargin (false);
    leftColumn.setSizeFull ();
    leftColumn.setWidth ("400px");

    grid.addComponent (leftColumn, 0, 0);
    grid.setComponentAlignment (leftColumn, Alignment.TOP_LEFT);

    rightColumn.setMargin (false);
    rightColumn.setSizeFull ();
    rightColumn.setStyleName ("rightColumn");

    grid.addComponent (rightColumn, 1, 0);
    grid.setComponentAlignment (rightColumn, Alignment.TOP_LEFT);

    grid.setColumnExpandRatio(1, 500);
  }
}
