package eu.toop.demoui.view;

import com.vaadin.navigator.View;
import com.vaadin.ui.*;
import eu.toop.demoui.bean.Identity;
import eu.toop.demoui.bean.Organization;
import eu.toop.demoui.form.IdentityForm;
import eu.toop.demoui.form.OrganizationForm;

public class StartView extends VerticalLayout implements View {

    public Organization organization;
    public OrganizationForm organizationForm;

    public StartView() {
        VerticalLayout headerLayout = new VerticalLayout();
        VerticalLayout mainLayout = new VerticalLayout();
        VerticalLayout footerLayout = new VerticalLayout();

        addComponent(headerLayout);
        addComponent(mainLayout);
        addComponent(footerLayout);

        headerLayout.addComponent(new Label("About | Contact | Help"));

        mainLayout.addComponent(new Label("Welcome to Freedonia's"));
        mainLayout.addComponent(new Label("Online public services portal"));

        HorizontalLayout processChoiceLayout = new HorizontalLayout();
        mainLayout.addComponent(processChoiceLayout);

        Button registerABranchButton = new Button("Register a branch", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                UI.getCurrent().getNavigator().navigateTo(eIDModuleView.class.getName());
            }
        });
        registerABranchButton.setStyleName("myButton");

        processChoiceLayout.addComponent(new Button("License and permissions"));
        processChoiceLayout.addComponent(new Button("Company data mandates"));
        processChoiceLayout.addComponent(registerABranchButton);

        organization = new Organization();
        organizationForm = new OrganizationForm(organization, event -> {});

        addComponent(new Label("TOOP demo user interface"));
        addComponent(organizationForm);

        addComponent(new Button("Register your new company", clickEvent -> {
            addComponent(new Label("Your new company has been registered!"));
        }));
    }
}
