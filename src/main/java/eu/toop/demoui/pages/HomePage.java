package eu.toop.demoui.pages;

import com.vaadin.event.LayoutEvents;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.Page;
import com.vaadin.server.Resource;
import com.vaadin.ui.CustomLayout;

import eu.toop.demoui.components.ClickableLayout;
import eu.toop.demoui.components.ServiceChoicePanel;
import eu.toop.demoui.view.BaseView;
import eu.toop.demoui.view.PhaseOne;

import java.io.IOException;
import java.net.*;

public class HomePage extends CustomLayout {

  public HomePage (BaseView view) {

    super ("HomePage");

    setHeight ("100%");

    ClickableLayout serviceChoicePanel = new ClickableLayout (new LayoutEvents.LayoutClickListener () {
      @Override
      public void layoutClick (LayoutEvents.LayoutClickEvent layoutClickEvent) {

        view.setCurrentPage (new BusinessServicesPage (view));
      }
    });
    serviceChoicePanel.addComponent (new ServiceChoicePanel ("BUSINESS SERVICES", ""));
    addComponent (serviceChoicePanel, "serviceChoicePanel");
  }
}
