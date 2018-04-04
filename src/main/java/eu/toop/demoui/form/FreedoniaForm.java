package eu.toop.demoui.form;

import com.vaadin.ui.AbstractLayout;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.Label;

public class FreedoniaForm extends CustomLayout {
  public FreedoniaForm (AbstractLayout form, String title) {

    super ("FreedoniaForm");
    setSizeUndefined ();
    addComponent (new Label (title), "title");
    addComponent (form, "form");
  }
}
