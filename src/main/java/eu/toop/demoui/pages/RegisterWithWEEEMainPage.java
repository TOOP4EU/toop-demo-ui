package eu.toop.demoui.pages;

import com.vaadin.ui.Button;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.themes.ValoTheme;

import eu.toop.demoui.form.FreedoniaForm;
import eu.toop.demoui.form.IdentityForm;
import eu.toop.demoui.form.MainCompanyForm;
import eu.toop.demoui.view.HomeView;

public class RegisterWithWEEEMainPage extends CustomLayout {

  HomeView _view;
  ProgressBar spinner = new ProgressBar ();

  public RegisterWithWEEEMainPage (HomeView view) {
    super ("RegisterWithWEEEMainPage");
    _view = view;

    setHeight ("100%");

    IdentityForm identityForm = new IdentityForm (view.getIdentity (), true, clickEvent -> {
    });
    addComponent (identityForm, "identityForm");

    Button toopButton = new Button ("Get company info");
    toopButton.addStyleName (ValoTheme.BUTTON_BORDERLESS);
    toopButton.addStyleName (" freedonia");
    addComponent (toopButton, "toopButton");

    spinner.setStyleName ("spinner");
    spinner.setIndeterminate (true);
    spinner.setVisible (false);
    spinner.setCaption ("Please wait while your request for data is processed...");
    addComponent (spinner, "spinner");

    toopButton.addClickListener (new Button.ClickListener () {
      public void buttonClick (Button.ClickEvent event) {

        ConfirmToopDataFetchingPage consentWindow = new ConfirmToopDataFetchingPage (view) {
          @Override
          public void onConsent () {
            // Show a loading icon while toop data is being retrieved.
            spinner.setVisible (true);
            toopButton.setEnabled (false);
          }
        };
      }
    });
  }

  public void addMainCompanyForm() {
    spinner.setVisible (false);

    MainCompanyForm mainCompanyForm = new MainCompanyForm (_view.getMainCompany (), false, null);

    FreedoniaForm freedoniaForm = new FreedoniaForm (mainCompanyForm, "Preview of company details");
    addComponent (freedoniaForm, "mainCompanyForm");
    _view.setMainCompanyForm (mainCompanyForm);

    Button nextButton = new Button ("I have previewed and want to proceed");
    nextButton.addStyleName (ValoTheme.BUTTON_BORDERLESS);
    nextButton.addStyleName (" freedonia");
    addComponent (nextButton, "nextButton");
    nextButton.addClickListener(new Button.ClickListener() {
      public void buttonClick(Button.ClickEvent event) {
        mainCompanyForm.save ();
        _view.setCurrentPage (new RegisterWithWEEENewDetailsPage (_view));
      }
    });
  }
}