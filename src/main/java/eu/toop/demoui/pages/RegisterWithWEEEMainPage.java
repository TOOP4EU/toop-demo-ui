package eu.toop.demoui.pages;

import com.vaadin.ui.Button;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.themes.ValoTheme;
import eu.toop.demoui.bean.Identity;
import eu.toop.demoui.form.FreedoniaForm;
import eu.toop.demoui.form.IdentityForm;
import eu.toop.demoui.form.MainCompanyForm;
import eu.toop.demoui.view.HomeView;

import java.time.LocalDate;

public class RegisterWithWEEEMainPage extends CustomLayout {
  public RegisterWithWEEEMainPage (HomeView view) {
    super ("RegisterWithWEEEMainPage");

    setHeight ("100%");

    IdentityForm identityForm = new IdentityForm (view.getIdentity (), true, clickEvent -> {});
    addComponent (identityForm, "identityForm");

    Button toopButton = new Button ("Get company info");
    toopButton.addStyleName (ValoTheme.BUTTON_BORDERLESS);
    toopButton.addStyleName (" freedonia");
    addComponent (toopButton, "toopButton");

    toopButton.addClickListener(new Button.ClickListener() {
      public void buttonClick(Button.ClickEvent event) {

        ConfirmToopDataFetchingPage consentWindow = new ConfirmToopDataFetchingPage (view) {
          @Override
          public void onConsent() {
            MainCompanyForm mainCompanyForm = new MainCompanyForm (view.getMainCompany (), false, null);

            FreedoniaForm freedoniaForm = new FreedoniaForm (mainCompanyForm, "Preview of company details");
            addComponent (freedoniaForm, "mainCompanyForm");
            view.setMainCompanyForm (mainCompanyForm);

            Button nextButton = new Button ("I have previewed and want to proceed");
            nextButton.addStyleName (ValoTheme.BUTTON_BORDERLESS);
            nextButton.addStyleName (" freedonia");
            addComponent (nextButton, "nextButton");
            nextButton.addClickListener(new Button.ClickListener() {
              public void buttonClick(Button.ClickEvent event) {
                mainCompanyForm.save ();
                view.setCurrentPage (new RegisterWithWEEENewDetailsPage (view));
              }
            });
          }
        };
      }
    });
  }
}