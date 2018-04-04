package eu.toop.demoui.pages;

import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.Label;

import eu.toop.demoui.view.BaseView;
import eu.toop.demoui.view.PhaseOne;

public class SuccessPage extends CustomLayout {
  public SuccessPage (BaseView view) {

    super ("SuccessPage");

    setHeight ("100%");

    String companyName = view.getMainCompany ().getCompanyName ();
    if (companyName.isEmpty ()) {
      companyName = "Zizi mat";
    }
    addComponent (new Label (companyName), "companyName");
  }
}