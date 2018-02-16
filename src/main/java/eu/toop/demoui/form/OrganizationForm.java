package eu.toop.demoui.form;

import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
import com.vaadin.ui.*;
import eu.toop.demoui.bean.Organization;
import eu.toop.iface.mockup.MSDataRequest;
import eu.toop.iface.mockup.ToopMessageBundle;
import eu.toop.iface.mockup.ToopMessageBundleBuilder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class OrganizationForm extends FormLayout {
    private Binder<Organization> binder = new Binder<>();

    private Organization organization;

    public OrganizationForm(Organization organization, Button.ClickListener onSubmit) {

        TextField companyNameField = new TextField("Company name");
        TextField companyTypeField = new TextField("Company type");

        binder.bind(companyNameField, Organization::getCompanyName, Organization::setCompanyName);
        binder.bind(companyTypeField, Organization::getCompanyType, Organization::setCompanyType);

        addComponent(companyNameField);
        addComponent(companyTypeField);

        setOrganizationBean(organization);

        Button toopButton = new Button("Pre-fill my form using TOOP!");
        toopButton.addClickListener(e -> {
            ByteArrayOutputStream archiveOutput = new ByteArrayOutputStream();
            final File keystoreFile = new File("src/main/resources/demo-keystore.jks");
            final String keystorePassword = "password";
            final String keyPassword = "password";

            try {
                ToopMessageBundle bundle = new ToopMessageBundleBuilder()
                        .setMSDataRequest(new MSDataRequest("ABC123"))
                        .sign(archiveOutput, keystoreFile, keystorePassword, keyPassword);
                eu.toop.iface.mockup.client.SendToMPClient.httpClientCall(archiveOutput.toByteArray());
                archiveOutput.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
        addComponents(toopButton);
    }

    public void setOrganizationBean(Organization _organization) {
        organization = _organization;
        binder.readBean(organization);
    }
}