package eu.toop.demoui.view;

import com.vaadin.navigator.View;
import com.vaadin.ui.CustomLayout;

import eu.toop.demoui.bean.Identity;
import eu.toop.demoui.bean.MainCompany;
import eu.toop.demoui.bean.NewCompany;
import eu.toop.demoui.form.MainCompanyForm;
import eu.toop.demoui.pages.HomePage;

public class PhaseOne extends BaseView {

  public PhaseOne () {

    setCurrentPage (new HomePage (this));
  }
}
