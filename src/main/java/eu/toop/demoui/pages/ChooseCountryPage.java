package eu.toop.demoui.pages;

import java.util.HashMap;
import java.util.Map;

import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.themes.ValoTheme;

import eu.toop.demoui.view.HomeView;

public class ChooseCountryPage extends CustomLayout {
  public ChooseCountryPage (final HomeView view) {
    super ("ChooseCountryPage");

    setHeight ("100%");

    final Map<String, String> map = new HashMap<> ();
    map.put ("Elonia", "EL");
    final ComboBox<String> country = new ComboBox<> ("", map.keySet ());
    country.setWidth ("100%");
    country.setPlaceholder ("Select eID issuer country");
    addComponent (country, "countryDropdown");

    final Button nextButton = new Button ("Next");
    nextButton.addStyleName (ValoTheme.BUTTON_BORDERLESS);
    nextButton.addStyleName (" freedonia");
    addComponent (nextButton, "nextButton");
    nextButton.addClickListener (event -> view.setCurrentPage (new IdentityProviderPage (view)));
  }
}