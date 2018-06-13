package eu.toop.demoui.layouts;

import com.vaadin.ui.Button;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.themes.ValoTheme;
import eu.toop.demoui.bean.MainCompany;
import eu.toop.demoui.view.BaseView;

public class ManualDataEntry extends CustomLayout {

  private final BaseView view;

  public ManualDataEntry (final BaseView view) {

    super ("ManualDataEntry");
    this.view = view;

    setHeight ("100%");

    final IdentityForm identityForm = new IdentityForm (view.getIdentity (), true);
    addComponent (identityForm, "identityForm");

    final Button manualDataEntryButton = new Button ("Enter company info");
    manualDataEntryButton.addStyleName (ValoTheme.BUTTON_BORDERLESS);
    manualDataEntryButton.addStyleName (" freedonia");
    addComponent (manualDataEntryButton, "manualDataEntryButton");

    manualDataEntryButton.addClickListener (event -> {
      view.setMainCompany (new MainCompany ());
      manualDataEntryButton.setEnabled (false);
      addMainCompanyForm ();
    });
  }

  public void addMainCompanyForm () {

    final MainCompanyForm mainCompanyForm = new MainCompanyForm (view.getMainCompany (), false, null);

    final BaseForm baseForm = new BaseForm (mainCompanyForm, "Enter company details");
    addComponent (baseForm, "mainCompanyForm");
    view.setMainCompanyForm (mainCompanyForm);

    final Button nextButton = new Button ("I confirm this information is correct");
    nextButton.addStyleName (ValoTheme.BUTTON_BORDERLESS);
    nextButton.addStyleName (" freedonia");
    addComponent (nextButton, "nextButton");
    nextButton.addClickListener (event -> {
      mainCompanyForm.save ();
      //view.setCurrentPage (new RegisterWithWEEENewDetailsPage (view));
      view.setCurrentPage (new SuccessPage (view));
    });
  }
}
