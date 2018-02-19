package eu.toop.demoui.view;

import com.vaadin.navigator.View;
import com.vaadin.ui.*;

public class NewCompanyView extends VerticalLayout implements View {
  public NewCompanyView() {
    addComponent(new Label("Register a new branch in Freedonia"));
    addComponent(new Label("Add the new branch details to register with Freedonia"));

    TextField companyTradeNameField = new TextField();
    TextField undertakingIdentification = new TextField();
    TextField companyType = new TextField();
    TextField legalForm = new TextField();

    companyTradeNameField.setCaption("Company trade field name:");
    undertakingIdentification.setCaption("Undertaking identification:");
    companyType.setCaption("Company type:");
    legalForm.setCaption("Legal form:");

    addComponent(companyTradeNameField);
    addComponent(undertakingIdentification);
    addComponent(companyType);
    addComponent(legalForm);

    Button nextButton2 = new Button("Register your new branch in Freedonia!", clickEvent -> {
      UI.getCurrent().getNavigator().navigateTo(FinalReviewView.class.getName());
    });
    addComponent(nextButton2);
  }
}
