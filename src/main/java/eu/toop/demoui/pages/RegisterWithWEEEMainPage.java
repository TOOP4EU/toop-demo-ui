package eu.toop.demoui.pages;

import com.vaadin.ui.Button;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.themes.ValoTheme;
import eu.toop.demoui.bean.Identity;
import eu.toop.demoui.form.IdentityForm;
import eu.toop.demoui.form.MainCompanyForm;
import eu.toop.demoui.view.HomeView;

import java.time.LocalDate;

public class RegisterWithWEEEMainPage extends CustomLayout {
  public RegisterWithWEEEMainPage (HomeView view) {
    super ("RegisterWithWEEEMainPage");

    setHeight ("100%");

    Identity identity = new Identity ();
    identity.setFirstName ("Jan");
    identity.setFamilyName ("Doe");
    identity.setBirthPlace("Fridili");
    identity.setBirthDate (LocalDate.parse("1986-02-01"));
    identity.setIdentifier ("EL/EL/12345");
    identity.setNationality ("EL");
    view.setIdentity (identity);

    IdentityForm identityForm = new IdentityForm (view.getIdentity (), true, clickEvent -> {});
    addComponent (identityForm, "identityForm");

    MainCompanyForm mainCompanyForm = new MainCompanyForm (view.getMainCompany (), false, null);
    addComponent (mainCompanyForm, "mainCompanyForm");

    Button toopButton = new Button ("Get company info");
    toopButton.addStyleName (ValoTheme.BUTTON_BORDERLESS);
    toopButton.addStyleName (" freedonia");
    addComponent (toopButton, "toopButton");

    toopButton.addClickListener(new Button.ClickListener() {
      public void buttonClick(Button.ClickEvent event) {

        ConfirmToopDataFetchingPage consentWindow = new ConfirmToopDataFetchingPage (view, mainCompanyForm);
        /*
        try {
          final String NS = "http://toop.eu/organization";
          ToopInterfaceManager.requestConcepts (Arrays.asList (new ConceptValue (NS, "companyName"),
            new ConceptValue (NS, "companyType")));
        } catch (final IOException ex) {
          // Convert from checked to unchecked
          throw new UncheckedIOException (ex);
        }

        // TODO: This should happen when the response is handled async
        MainCompany mainCompany = view.getMainCompany ();
        mainCompany.setCompany ("Hello world");
        view.setMainCompany (mainCompany);
        mainCompanyForm.setOrganizationBean (mainCompany);
        mainCompanyForm.save ();
        */
      }
    });

    Button nextButton = new Button ("Proceed");
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
}