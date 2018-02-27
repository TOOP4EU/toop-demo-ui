package eu.toop.demoui.pages;

import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import eu.toop.demoui.bean.Identity;
import eu.toop.demoui.components.Body;
import eu.toop.demoui.components.EloniaHeader;
import eu.toop.demoui.form.IdentityForm;
import eu.toop.demoui.view.StartView;

public class ConfirmDetailsPage extends BasePage {

  private IdentityForm identityForm;

  public ConfirmDetailsPage (StartView view) {
    super(view);

    setStyleName ("pageConfirmDetails");

    VerticalLayout main = new VerticalLayout ();
    setHeader (new EloniaHeader ());
    setBody (new Body (main));

    main.setSizeUndefined ();

    {
      Label label = new Label ("Confirm your personal details");
      main.addComponent (label);
    }
    {
      Label label = new Label ("Your personal details");
      main.addComponent (label);
    }

    identityForm = new IdentityForm (view.getIdentity (), true, clickEvent -> {});
    main.addComponent (identityForm);

    Button nextButton = new Button ("Confirm and proceed");
    main.addComponent (nextButton);

    nextButton.addClickListener(new Button.ClickListener() {
      public void buttonClick(Button.ClickEvent event) {
        view.setCurrentPage (new MainCompanyPage (getView ()));
      }
    });
  }
}
