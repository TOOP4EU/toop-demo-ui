package eu.toop.demoui.pages;

import com.vaadin.ui.Button;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.themes.ValoTheme;

import eu.toop.demoui.form.NewCompanyForm;
import eu.toop.demoui.view.BaseView;
import eu.toop.demoui.view.PhaseOne;

public class RegisterWithWEEENewDetailsPage extends CustomLayout {
  public RegisterWithWEEENewDetailsPage (BaseView view) {

    super ("RegisterWithWEEENewDetailsPage");

    setHeight ("100%");

    NewCompanyForm newCompanyForm = new NewCompanyForm (view.getNewCompany (), false);
    addComponent (newCompanyForm, "newCompanyForm");

    Button nextButton = new Button ("Proceed");
    nextButton.addStyleName (ValoTheme.BUTTON_BORDERLESS);
    nextButton.addStyleName (" freedonia");
    addComponent (nextButton, "nextButton");
    nextButton.addClickListener (new Button.ClickListener () {
      public void buttonClick (Button.ClickEvent event) {

        newCompanyForm.save ();
        view.setCurrentPage (new ReviewPage (view));
      }
    });
  }
}