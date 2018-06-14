package eu.toop.demoui.view;

import eu.toop.demoui.layouts.HomePage;

public class PhaseOne extends BaseView {

  public PhaseOne () {

    setCurrentPage (new HomePage (this));
  }

  @Override
  public boolean equals(Object obj) {
    return super.equals (obj);
  }
}
