package eu.toop.demoui.layouts;

import com.vaadin.ui.Button;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.themes.ValoTheme;

import eu.toop.demoui.view.BaseView;

public class RegisterWithWEEEMainPage extends CustomLayout {

  private final BaseView _view;
  private final ProgressBar spinner = new ProgressBar ();

  public RegisterWithWEEEMainPage (final BaseView view) {

    super ("RegisterWithWEEEMainPage");
    _view = view;

    setHeight ("100%");

    final IdentityForm identityForm = new IdentityForm (view.getIdentity (), true, clickEvent -> {

    });
    addComponent (identityForm, "identityForm");

    final Button toopButton = new Button ("Get company info");
    toopButton.addStyleName (ValoTheme.BUTTON_BORDERLESS);
    toopButton.addStyleName (" freedonia");
    addComponent (toopButton, "toopButton");

    spinner.setStyleName ("spinner");
    spinner.setIndeterminate (true);
    spinner.setVisible (false);
    spinner.setCaption ("Please wait while your request for data is processed...");
    addComponent (spinner, "spinner");

    toopButton.addClickListener ((event) -> {

      final ConfirmToopDataFetchingPage consentWindow = new ConfirmToopDataFetchingPage (view) {
        @Override
        public void onConsent () {
          // Show a loading icon while toop data is being retrieved.
          spinner.setVisible (true);
          toopButton.setEnabled (false);
        }
      };
    });
  }

  public void addMainCompanyForm () {

    spinner.setVisible (false);

    final MainCompanyForm mainCompanyForm = new MainCompanyForm (_view.getMainCompany (), false, null);

    final BaseForm baseForm = new BaseForm (mainCompanyForm, "Preview of company details");
    addComponent (baseForm, "mainCompanyForm");
    _view.setMainCompanyForm (mainCompanyForm);

    final Button nextButton = new Button ("I have previewed and want to proceed");
    nextButton.addStyleName (ValoTheme.BUTTON_BORDERLESS);
    nextButton.addStyleName (" freedonia");
    addComponent (nextButton, "nextButton");
    nextButton.addClickListener ((event) -> {

      mainCompanyForm.save ();
      _view.setCurrentPage (new RegisterWithWEEENewDetailsPage (_view));
    });
  }
}