package eu.toop.demoui.view;

import com.vaadin.navigator.View;
import com.vaadin.ui.*;

public class StartView extends VerticalLayout implements View {

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

        Button licenseAndPermissionsButton = new Button("License and permissions");
        Button companyDataMandatesButton = new Button("Company data mandates");
        Button registerABranchButton = new Button("Register a branch", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                UI.getCurrent().getNavigator().navigateTo(eIDModuleView.class.getName());
            }
        });

        licenseAndPermissionsButton.setEnabled(false);
        companyDataMandatesButton.setEnabled(false);

        processChoiceLayout.addComponent(licenseAndPermissionsButton);
        processChoiceLayout.addComponent(companyDataMandatesButton);
        processChoiceLayout.addComponent(registerABranchButton);
    }
}
