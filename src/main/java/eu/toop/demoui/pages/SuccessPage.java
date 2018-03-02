package eu.toop.demoui.pages;

import com.vaadin.ui.*;
import eu.toop.demoui.view.HomeView;

public class SuccessPage extends CustomLayout {
  public SuccessPage (HomeView view) {
    super ("SuccessPage");

    setHeight ("100%");

    String companyName = view.getMainCompany ().getCompanyName ();
    if (companyName.isEmpty ()) {
      companyName = "Zizi mat";
    }
    addComponent (new Label (companyName), "companyName");
  }
}