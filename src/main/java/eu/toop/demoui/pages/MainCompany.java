package eu.toop.demoui.pages;

import com.vaadin.ui.*;
import eu.toop.demoui.bean.Identity;
import eu.toop.demoui.bean.Organization;
import eu.toop.demoui.components.Body;
import eu.toop.demoui.components.FreedoniaHeader;
import eu.toop.demoui.form.IdentityForm;
import eu.toop.demoui.form.OrganizationForm;
import eu.toop.demoui.view.StartView;

public class MainCompany extends Base {
  public MainCompany (StartView view) {
    super(view);

    setStyleName ("pageMainCompany");

    VerticalLayout main = new VerticalLayout ();
    setHeader (new FreedoniaHeader ());
    setBody (new Body (main));

    main.setSizeUndefined ();

    {
      Label label = new Label ("Register a new branch in Freedonia");
      main.addComponent (label);
    }

    {
      Label label = new Label ("Your personal details");
      main.addComponent (label);
    }

    main.addComponent (new IdentityForm (new Identity (), clickEvent -> {}));

    {
      Label label = new Label ("Parent company main details");
      main.addComponent (label);
    }

    main.addComponent (new OrganizationForm (new Organization (), clickEvent -> {}));

    Button nextButton = new Button ("Confirm and proceed");
    main.addComponent (nextButton);

    nextButton.addClickListener(new Button.ClickListener() {
      public void buttonClick(Button.ClickEvent event) {
        view.setCurrentPage (new IdP (getView ()));
      }
    });
  }
}
