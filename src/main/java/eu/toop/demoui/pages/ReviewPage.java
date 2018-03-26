package eu.toop.demoui.pages;

import com.vaadin.ui.Button;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.themes.ValoTheme;

import eu.toop.demoui.form.IdentityForm;
import eu.toop.demoui.form.MainCompanyForm;
import eu.toop.demoui.form.NewCompanyForm;
import eu.toop.demoui.view.HomeView;

public class ReviewPage extends CustomLayout {
  public ReviewPage (HomeView view) {
    super ("ReviewPage");

    setHeight ("100%");

    IdentityForm identityForm = new IdentityForm (view.getIdentity (), true, clickEvent -> {});
    addComponent (identityForm, "identityForm");

    NewCompanyForm newCompanyForm = new NewCompanyForm (view.getNewCompany (), true);
    addComponent (newCompanyForm, "newCompanyForm");

    MainCompanyForm mainCompanyForm = new MainCompanyForm (view.getMainCompany (), true, null);
    addComponent (mainCompanyForm, "mainCompanyForm");

    Button nextButton = new Button ("Proceed");
    nextButton.addStyleName (ValoTheme.BUTTON_BORDERLESS);
    nextButton.addStyleName (" freedonia");
    addComponent (nextButton, "nextButton");
    nextButton.addClickListener(new Button.ClickListener() {
      public void buttonClick(Button.ClickEvent event) {
        view.setCurrentPage (new SuccessPage (view));
      }
    });
  }
}