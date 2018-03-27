package eu.toop.demoui.view;

import com.vaadin.navigator.View;
import com.vaadin.ui.CustomLayout;

import eu.toop.demoui.bean.Identity;
import eu.toop.demoui.bean.MainCompany;
import eu.toop.demoui.bean.NewCompany;
import eu.toop.demoui.form.MainCompanyForm;
import eu.toop.demoui.pages.HomePage;

public class HomeView extends CustomLayout implements View {

  private CustomLayout _page;
  private Identity identity = new Identity ();
  private MainCompany mainCompany = new MainCompany ();
  private NewCompany newCompany = new NewCompany ();

  private MainCompanyForm mainCompanyForm = null;

  public HomeView () {
    super("HomeView");
    setHeight ("100%");

    setCurrentPage (new HomePage (this));

  }

  public void setCurrentPage(CustomLayout page) {
    if (_page != null) {
      replaceComponent (_page, page);
    } else {
      addComponent (page, "page");
    }
    _page = page;
  }

  public CustomLayout getCurrentPage () {
    return _page;
  }

  public Identity getIdentity () {
    return identity;
  }

  public void setIdentity (Identity identity) {
    this.identity = identity;
  }

  public MainCompany getMainCompany () {
    return mainCompany;
  }

  public void setMainCompany (MainCompany mainCompany) {
    this.mainCompany = mainCompany;
  }

  public NewCompany getNewCompany () {
    return newCompany;
  }

  public void setNewCompany (NewCompany newCompany) {
    this.newCompany = newCompany;
  }

  public MainCompanyForm getMainCompanyForm () {
    return mainCompanyForm;
  }

  public void setMainCompanyForm (MainCompanyForm mainCompanyForm) {
    this.mainCompanyForm = mainCompanyForm;
  }
}
