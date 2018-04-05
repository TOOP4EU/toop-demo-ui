package eu.toop.demoui.layouts;

import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.Label;

import eu.toop.demoui.view.BaseView;

public class SuccessPage extends CustomLayout {
  public SuccessPage (final BaseView view) {

    super ("SuccessPage");

    setHeight ("100%");

    String companyName = view.getMainCompany ().getCompanyName ();
    if (companyName.isEmpty ()) {
      companyName = "Zizi mat";
    }
    addComponent (new Label (companyName), "companyName");
  }
}