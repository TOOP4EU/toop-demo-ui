package eu.toop.demoui.pages;

import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import eu.toop.demoui.components.Body;
import eu.toop.demoui.components.FreedoniaHeader;
import eu.toop.demoui.form.NewCompanyForm;
import eu.toop.demoui.view.StartView;

public class NewCompanyPage extends BasePage {

  private NewCompanyForm newCompanyForm;

  public NewCompanyPage (StartView view) {
    super(view);

    setStyleName ("pageNewCompany");

    VerticalLayout main = new VerticalLayout ();
    setHeader (new FreedoniaHeader ());
    setBody (new Body (main));

    main.setSizeUndefined ();

    {
      Label label = new Label ("Register a new branch in Freedonia");
      main.addComponent (label);
    }

    {
      Label label = new Label ("Add the new branch details to register with Freedonia");
      main.addComponent (label);
    }

    newCompanyForm = new NewCompanyForm (view.getNewCompany (), false);
    main.addComponent (newCompanyForm);

    Button nextButton = new Button ("Proceed");
    main.addComponent (nextButton);

    nextButton.addClickListener(new Button.ClickListener() {
      public void buttonClick(Button.ClickEvent event) {
        newCompanyForm.save ();
        view.setCurrentPage (new ReviewPage (getView ()));
      }
    });
  }
}
