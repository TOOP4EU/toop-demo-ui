package eu.toop.demoui.view;

import com.vaadin.navigator.View;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import eu.toop.demoui.bean.Identity;
import eu.toop.demoui.bean.Organization;
import eu.toop.demoui.form.IdentityForm;
import eu.toop.demoui.form.OrganizationForm;

public class StartView extends VerticalLayout implements View {

    public Identity identity;
    public Organization organization;

    public IdentityForm identityForm;
    public OrganizationForm organizationForm;

    public StartView() {
        identity = new Identity();
        organization = new Organization();

        identityForm = new IdentityForm(identity, event -> {});
        organizationForm = new OrganizationForm(organization, event -> {});

        addComponent(new Label("TOOP demo user interface"));
        addComponent(identityForm);
        addComponent(organizationForm);

        addComponent(new Button("Register your new company", clickEvent -> {
            addComponent(new Label("Your new company has been registered!"));
        }));
    }
}
