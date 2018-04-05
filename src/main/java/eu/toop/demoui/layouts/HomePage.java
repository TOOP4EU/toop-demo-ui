package eu.toop.demoui.layouts;

import com.vaadin.ui.CustomLayout;

import eu.toop.demoui.view.BaseView;

public class HomePage extends CustomLayout {

  public HomePage (BaseView view) {

    super ("HomePage");

    setHeight ("100%");

    final ClickableLayout serviceChoicePanel = new ClickableLayout ( (event) -> {

        view.setCurrentPage (new BusinessServicesPage (view));
    });
    serviceChoicePanel.addComponent (new ServiceChoicePanel ("BUSINESS SERVICES"));
    addComponent (serviceChoicePanel, "serviceChoicePanel");
  }
}
