package eu.toop.demoui.view;

import com.vaadin.navigator.View;
import com.vaadin.ui.AbstractLayout;
import com.vaadin.ui.CustomLayout;

import eu.toop.demoui.bean.Identity;
import eu.toop.demoui.bean.MainCompany;
import eu.toop.demoui.bean.NewCompany;
import eu.toop.demoui.layouts.MainCompanyForm;

public class BaseView extends CustomLayout implements View {

  private AbstractLayout page;
  private Identity identity = new Identity ();
  private MainCompany mainCompany = new MainCompany ();
  private NewCompany newCompany = new NewCompany ();

  private MainCompanyForm mainCompanyForm = null;

  public BaseView () {

    super ("BaseView");
    setHeight ("100%");
  }

  public void setCurrentPage (AbstractLayout page) {

    if (this.page != null) {
      replaceComponent (this.page, page);
    } else {
      addComponent (page, "page");
    }
    this.page = page;
  }

  public AbstractLayout getCurrentPage () {

    return page;
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

  @Override
  public boolean equals(Object obj) {
    return super.equals (obj);
  }

  @Override
  public int hashCode() {
    return super.hashCode ();
  }
}
