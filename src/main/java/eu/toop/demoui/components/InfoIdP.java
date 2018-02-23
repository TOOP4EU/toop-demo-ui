package eu.toop.demoui.components;

import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class InfoIdP extends VerticalLayout {
  public InfoIdP () {
    setMargin (false);
    setSizeFull ();
    setStyleName ("infoElonia");

    VerticalLayout labelList = new VerticalLayout ();
    labelList.setMargin (false);
    labelList.setSizeUndefined ();
    labelList.setWidth ("100%");
    addComponent (labelList);

    {
      Label label = new Label("Freedonia is requesting the following attributes:");
      label.setWidth ("100%");
      labelList.addComponent (label);
    }
    {
      Label label = new Label("Your basic information");
      label.setWidth ("100%");
      label.setStyleName ("bold");
      labelList.addComponent (label);
    }
    {
      Label label = new Label("Personal ID");
      label.setWidth ("100%");
      labelList.addComponent (label);
    }
    {
      Label label = new Label("First name");
      label.setWidth ("100%");
      labelList.addComponent (label);
    }
    {
      Label label = new Label("Last name");
      label.setWidth ("100%");
      labelList.addComponent (label);
    }
    {
      Label label = new Label("Date of birth");
      label.setWidth ("100%");
      labelList.addComponent (label);
    }
    {
      Label label = new Label("Please note that if you do not provide further information this process will be cancelled.");
      label.setWidth ("100%");
      labelList.addComponent (label);
    }
  }
}
