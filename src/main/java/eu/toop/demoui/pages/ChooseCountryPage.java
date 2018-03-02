package eu.toop.demoui.pages;

import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.themes.ValoTheme;
import eu.toop.demoui.view.HomeView;

import java.util.*;

public class ChooseCountryPage extends CustomLayout {
  public ChooseCountryPage (HomeView view) {
    super ("ChooseCountryPage");

    setHeight ("100%");

    final Map<String, String> map = new HashMap<> ();
    map.put("Elonia", "EL");
    final ComboBox country = new ComboBox<>("", map.keySet());
    country.setWidth ("100%");
    country.setPlaceholder ("Select eID issuer country");
    addComponent(country, "countryDropdown");

    Button nextButton = new Button ("Next");
    nextButton.addStyleName (ValoTheme.BUTTON_BORDERLESS);
    nextButton.addStyleName (" freedonia");
    addComponent (nextButton, "nextButton");
    nextButton.addClickListener(new Button.ClickListener() {
      public void buttonClick(Button.ClickEvent event) {
        view.setCurrentPage (new IdentityProviderPage (view));
      }
    });
  }
}