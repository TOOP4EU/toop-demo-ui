package eu.toop.demoui.components;

import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class InfoStart extends VerticalLayout {
  public InfoStart () {
    setMargin (false);
    setSizeFull ();
    setStyleName ("infoFreedonia");

    VerticalLayout labelList = new VerticalLayout ();
    labelList.setMargin (false);
    labelList.setSizeUndefined ();
    labelList.setWidth ("100%");
    addComponent (labelList);

    Label label1 = new Label("By clicking next you will be transferred to the eIDAS system to securely authenticate with this application.");
    label1.setWidth ("100%");
    labelList.addComponent (label1);

    Label label2 = new Label("Please recall, that using the eIDAS system you trustly provide us your identity attributes such as name, address, etc.");
    label2.setWidth ("100%");
    label2.setStyleName ("bold");
    labelList.addComponent (label2);

    Label label3 = new Label("eIDAS will provide us with those attributes from the attribute providers you suggest.");
    label3.setWidth ("100%");
    labelList.addComponent (label3);

    Label label4 = new Label("eIDAS will request your consent before sending us any information.");
    label4.setWidth ("100%");
    labelList.addComponent (label4);
  }
}
