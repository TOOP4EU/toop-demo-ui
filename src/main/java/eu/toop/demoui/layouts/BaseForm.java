package eu.toop.demoui.layouts;

import com.vaadin.ui.AbstractLayout;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.Label;

public class BaseForm extends CustomLayout {
  public BaseForm (final AbstractLayout form, final String title) {

    super ("BaseForm");
    setSizeUndefined ();
    addComponent (new Label (title), "title");
    addComponent (form, "form");
  }

  public boolean equals(Object obj) {
    return super.equals (obj);
  }
}
