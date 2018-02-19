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
//import eu.toop.iface.mockup.client.HttpClientInvoker;

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
      // TODO use production keystore
      final SignatureHelper aSH = new SignatureHelper(
          FileHelper.getInputStream(new File("src/main/resources/demo-keystore.jks")), "password", null, "password");

      /*try (final NonBlockingByteArrayOutputStream archiveOutput = new NonBlockingByteArrayOutputStream()) {
        ToopMessageBuilder.createRequestMessage(new MSDataRequest("DE", "urn:abc:whatsoever-document-type-ID",
            organization.getCompanyName() + "/" + organization.getCompanyType()), archiveOutput, aSH);

        // Send to DC (see DCInputServlet in toop-mp-webapp)
        HttpClientInvoker.httpClientCallNoResponse("http://mp.elonia.toop:8090/dcinput", archiveOutput.toByteArray());

        // Successfully sent
      } catch (final IOException e1) {
        e1.printStackTrace();
      }*/
    });
    addComponents(toopButton);
  }

  public void setOrganizationBean(final Organization _organization) {
    organization = _organization;
    binder.readBean(organization);
  }
}