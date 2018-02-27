package eu.toop.demoui.pages;

import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import eu.toop.demoui.components.Body;
import eu.toop.demoui.components.FreedoniaHeader;
import eu.toop.demoui.form.IdentityForm;
import eu.toop.demoui.form.NewCompanyForm;
import eu.toop.demoui.form.MainCompanyForm;
import eu.toop.demoui.view.StartView;

public class ReviewPage extends BasePage {
  public ReviewPage (StartView view) {
    super(view);

    setStyleName ("pageNewCompany");

    VerticalLayout main = new VerticalLayout ();
    setHeader (new FreedoniaHeader ());
    setBody (new Body (main));

    main.setSizeUndefined ();

    {
      Label label = new Label ("Review your information before submitting");
      main.addComponent (label);
    }

    {
      Label label = new Label ("Your personal details");
      main.addComponent (label);
    }

    main.addComponent (new IdentityForm (view.getIdentity (), true, clickEvent -> {}));

    {
      Label label = new Label ("Parent company main details");
      main.addComponent (label);
    }

    main.addComponent (new MainCompanyForm (view.getMainCompany (), true, clickEvent -> {}));

    {
      Label label = new Label ("Add the new branch details to register with Freedonia");
      main.addComponent (label);
    }

    main.addComponent (new NewCompanyForm (view.getNewCompany (), true));

    Button nextButton = new Button ("Proceed");
    main.addComponent (nextButton);

    nextButton.addClickListener(new Button.ClickListener() {
      public void buttonClick(Button.ClickEvent event) {
        view.setCurrentPage (new SuccessPage (getView ()));
      }
    });
  }
}