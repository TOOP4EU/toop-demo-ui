package eu.toop.demoui.view;

import com.vaadin.navigator.View;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

public class FinalReviewView extends VerticalLayout implements View {
  public FinalReviewView() {
    addComponent(new Label("Register a new branch in Freedonia"));
    addComponent(new Label("Review your information before submitting!"));

    addComponent(new Label("{{Show new branch details here}}"));
    addComponent(new Label("{{Show main company details here}}"));

    Button nextButton2 = new Button("Register my new company", clickEvent -> {
      UI.getCurrent().getNavigator().navigateTo(SuccessView.class.getName());
    });
    addComponent(nextButton2);
  }
}
