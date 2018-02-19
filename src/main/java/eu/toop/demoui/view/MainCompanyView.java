package eu.toop.demoui.view;

import com.vaadin.navigator.View;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import eu.toop.demoui.bean.Organization;
import eu.toop.demoui.form.OrganizationForm;

public class MainCompanyView extends VerticalLayout implements View {

  private Organization organization;
  private OrganizationForm organizationForm;

  public MainCompanyView() {
    organization = new Organization();
    organizationForm = new OrganizationForm(organization, event -> {});
    addComponent(organizationForm);

    Button nextButton2 = new Button("Go to NewCompanyView", clickEvent -> {
      UI.getCurrent().getNavigator().navigateTo(NewCompanyView.class.getName());
    });
    addComponent(nextButton2);
  }

  public Organization getOrganization() {
    return organization;
  }

  public OrganizationForm getOrganizationForm() {
    return organizationForm;
  }
}
