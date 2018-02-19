package eu.toop.demoui.form;

import java.io.File;
import java.io.IOException;

import com.helger.asic.SignatureHelper;
import com.helger.commons.io.file.FileHelper;
import com.helger.commons.io.stream.NonBlockingByteArrayOutputStream;
import com.vaadin.data.Binder;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;

import eu.toop.commons.exchange.message.ToopMessageBuilder;
import eu.toop.commons.exchange.mock.MSDataRequest;
import eu.toop.demoui.bean.Organization;

public class OrganizationForm extends FormLayout {
  private final Binder<Organization> binder = new Binder<>();

  private Organization organization;

  public OrganizationForm(final Organization organization, final Button.ClickListener onSubmit) {

    final TextField companyNameField = new TextField("Company name");
    final TextField companyTypeField = new TextField("Company type");

    binder.bind(companyNameField, Organization::getCompanyName, Organization::setCompanyName);
    binder.bind(companyTypeField, Organization::getCompanyType, Organization::setCompanyType);

    addComponent(companyNameField);
    addComponent(companyTypeField);

    setOrganizationBean(organization);

    final Button toopButton = new Button("Pre-fill my form using TOOP!");
    toopButton.addClickListener(e -> {
      final SignatureHelper aSH = new SignatureHelper(
          FileHelper.getInputStream(new File("src/main/resources/demo-keystore.jks")), "password", null, "password");

      try (final NonBlockingByteArrayOutputStream archiveOutput = new NonBlockingByteArrayOutputStream()) {
        // TODO should be the request I assume
        ToopMessageBuilder.createResponseMessage(new MSDataRequest("ABC123"), null, null, null, archiveOutput, aSH);
        eu.toop.iface.mockup.client.SendToMPClient.httpClientCall(archiveOutput.toByteArray());
      } catch (final IOException e1) {
        e1.printStackTrace();
      }
    });
    addComponents(toopButton);
  }

  public void setOrganizationBean(final Organization _organization) {
    organization = _organization;
    binder.readBean(organization);
  }
}