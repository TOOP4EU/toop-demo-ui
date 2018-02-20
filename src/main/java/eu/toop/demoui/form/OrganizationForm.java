/**
 * Copyright (C) 2018 toop.eu
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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

import eu.toop.commons.doctype.EToopDocumentType;
import eu.toop.commons.doctype.EToopProcess;
import eu.toop.commons.exchange.message.ToopMessageBuilder;
import eu.toop.commons.exchange.mock.MSDataRequest;
import eu.toop.demoui.bean.Organization;
//import eu.toop.iface.mockup.client.HttpClientInvoker;

public class OrganizationForm extends FormLayout {
  private final Binder<Organization> binder = new Binder<> ();

  private Organization organization;

  public OrganizationForm (final Organization organization, final Button.ClickListener onSubmit) {

    final TextField companyNameField = new TextField ("Company name");
    final TextField companyTypeField = new TextField ("Company type");

    binder.bind (companyNameField, Organization::getCompanyName, Organization::setCompanyName);
    binder.bind (companyTypeField, Organization::getCompanyType, Organization::setCompanyType);

    addComponent (companyNameField);
    addComponent (companyTypeField);

    setOrganizationBean (organization);

    final Button toopButton = new Button ("Pre-fill my form using TOOP!");
    toopButton.addClickListener (e -> {
      // TODO use production keystore
      final SignatureHelper aSH = new SignatureHelper (FileHelper.getInputStream (new File ("src/main/resources/demo-keystore.jks")),
                                                       "password", null, "password");

      try (final NonBlockingByteArrayOutputStream archiveOutput = new NonBlockingByteArrayOutputStream ()) {
        ToopMessageBuilder.createRequestMessage (new MSDataRequest ("DE", EToopDocumentType.DOCTYPE3.getURIEncoded (),
                                                                    EToopProcess.PROC.getURIEncoded (),
                                                                    organization.getCompanyName () + "/" + organization.getCompanyType ()),
                                                 archiveOutput, aSH);

        // Send to DC (see DCInputServlet in toop-mp-webapp)
        // HttpClientInvoker.httpClientCallNoResponse("http://mp.elonia.toop:8090/dcinput",
        // archiveOutput.toByteArray());

        // Successfully sent
      } catch (final IOException e1) {
        e1.printStackTrace ();
      }
    });
    addComponents (toopButton);
  }

  public void setOrganizationBean (final Organization _organization) {
    organization = _organization;
    binder.readBean (organization);
  }
}