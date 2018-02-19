package eu.toop.demoui.view;

import com.vaadin.navigator.View;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

public class MainCompanyRequestDisclaimerView extends VerticalLayout implements View {
  public MainCompanyRequestDisclaimerView() {
    Button nextButton = new Button("Go to MainCompanyView", clickEvent -> {
      UI.getCurrent().getNavigator().navigateTo(MainCompanyView.class.getName());
    });
    addComponent(nextButton);
  }
}
