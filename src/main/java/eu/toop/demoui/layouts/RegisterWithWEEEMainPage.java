package eu.toop.demoui.layouts;

import com.vaadin.ui.Button;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.themes.ValoTheme;

import eu.toop.demoui.view.BaseView;

public class RegisterWithWEEEMainPage extends CustomLayout {

  private final BaseView view;
  private final ProgressBar spinner = new ProgressBar ();

  public RegisterWithWEEEMainPage (final BaseView view) {

    super ("RegisterWithWEEEMainPage");
    this.view = view;

    setHeight ("100%");

    final IdentityForm identityForm = new IdentityForm (view.getIdentity (), true);
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

      new ConfirmToopDataFetchingPage (view) {
        @Override
        public void onConsent () {
          // Show a loading icon while toop data is being retrieved.
          spinner.setVisible (true);
          toopButton.setEnabled (false);
        }

        @Override
        public void onSelfProvide () {
          toopButton.setEnabled (false);
          RegisterWithWEEEMainPage.this.view.setCurrentPage (new ManualDataEntry (RegisterWithWEEEMainPage.this.view));
        }
      };
    });
  }

  public void addMainCompanyForm () {

    spinner.setVisible (false);

    final MainCompanyForm mainCompanyForm = new MainCompanyForm (view.getMainCompany (), false, null);

    final BaseForm baseForm = new BaseForm (mainCompanyForm, "Preview of company details");
    addComponent (baseForm, "mainCompanyForm");
    view.setMainCompanyForm (mainCompanyForm);

    final Button nextButton = new Button ("I have previewed this information and want to proceed");
    nextButton.addStyleName (ValoTheme.BUTTON_BORDERLESS);
    nextButton.addStyleName (" freedonia");
    addComponent (nextButton, "nextButton");
    nextButton.addClickListener (event -> {

      mainCompanyForm.save ();
      //view.setCurrentPage (new RegisterWithWEEENewDetailsPage (view));
      view.setCurrentPage (new SuccessPage (view));
    });

    final Button gotoManualDataEntryButton = new Button("I do not wish this information to be used");
    gotoManualDataEntryButton.addStyleName (ValoTheme.BUTTON_BORDERLESS);
    gotoManualDataEntryButton.addStyleName (" freedonia");
    gotoManualDataEntryButton.addStyleName (" gotoManualDataEntryButton");
    addComponent (gotoManualDataEntryButton, "gotoManualDataEntryButton");
    gotoManualDataEntryButton.addClickListener (event -> view.setCurrentPage (new ManualDataEntry (view)));
  }
}