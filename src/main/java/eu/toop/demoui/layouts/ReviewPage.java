package eu.toop.demoui.layouts;

import com.vaadin.ui.Button;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.themes.ValoTheme;

import eu.toop.demoui.view.BaseView;

public class ReviewPage extends CustomLayout {
  public ReviewPage (final BaseView view) {

    super ("ReviewPage");

    setHeight ("100%");

    final IdentityForm identityForm = new IdentityForm (view.getIdentity (), true);
    addComponent (identityForm, "identityForm");

    final NewCompanyForm newCompanyForm = new NewCompanyForm (view.getNewCompany (), true);
    addComponent (newCompanyForm, "newCompanyForm");

    final MainCompanyForm mainCompanyForm = new MainCompanyForm (view.getMainCompany (), true, null);
    addComponent (mainCompanyForm, "mainCompanyForm");

    final Button nextButton = new Button ("Proceed");
    nextButton.addStyleName (ValoTheme.BUTTON_BORDERLESS);
    nextButton.addStyleName (" freedonia");
    addComponent (nextButton, "nextButton");
    nextButton.addClickListener (event -> view.setCurrentPage (new SuccessPage (view)));
  }
}