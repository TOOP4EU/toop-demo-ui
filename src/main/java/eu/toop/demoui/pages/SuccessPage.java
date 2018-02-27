package eu.toop.demoui.pages;

import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import eu.toop.demoui.components.Body;
import eu.toop.demoui.components.FreedoniaHeader;
import eu.toop.demoui.view.StartView;

public class SuccessPage extends BasePage {
  public SuccessPage (StartView view) {
    super(view);

    setStyleName ("pageNewCompany");

    VerticalLayout main = new VerticalLayout ();
    setHeader (new FreedoniaHeader ());
    setBody (new Body (main));

    main.setSizeUndefined ();

    {
      Label label = new Label ("Success");
      main.addComponent (label);
    }
  }
}
