package eu.toop.demoui.view;

import com.vaadin.navigator.View;
import com.vaadin.ui.*;

import java.util.ArrayList;
import java.util.List;

public class eIDModuleView extends VerticalLayout implements View {

  public eIDModuleView() {
    // List of planets
    List<String> choices = new ArrayList<>();
    choices.add("Elonia");

    ComboBox<String> countryComboBox = new ComboBox<>("Select eID Coutnry");
    countryComboBox.setItems(choices);
    addComponent(countryComboBox);

    // Use the name property for item captions
    countryComboBox.setItemCaptionGenerator(String::toString);

    Button nextButton = new Button("Go to IdentityProviderView", clickEvent -> {
      UI.getCurrent().getNavigator().navigateTo(IdentityProviderView.class.getName());
    });
    addComponent(nextButton);
  }
}
