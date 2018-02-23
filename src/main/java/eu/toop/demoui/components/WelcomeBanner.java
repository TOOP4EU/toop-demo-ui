package eu.toop.demoui.components;

import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class WelcomeBanner extends Banner {
  public WelcomeBanner () {
    setStyleName ("welcomeBanner");

    Label welcomeBannerTitle = new Label ("Welcome to Freedonia's");
    welcomeBannerTitle.setStyleName ("welcomeBannerTitle");
    addComponent (welcomeBannerTitle);

    Label welcomeBannerSubTitle = new Label ("Online public services portal");
    welcomeBannerSubTitle.setStyleName ("welcomeBannerSubTitle");
    addComponent (welcomeBannerSubTitle);
  }
}
