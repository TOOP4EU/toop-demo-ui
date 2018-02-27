package eu.toop.demoui.pages;

import com.helger.asic.SignatureHelper;
import com.helger.commons.collection.impl.CommonsArrayList;
import com.helger.commons.io.file.FileHelper;
import com.helger.commons.io.stream.NonBlockingByteArrayOutputStream;
import com.vaadin.ui.*;
import eu.toop.commons.doctype.EToopDocumentType;
import eu.toop.commons.doctype.EToopProcess;
import eu.toop.commons.exchange.RequestValue;
import eu.toop.commons.exchange.message.ToopMessageBuilder;
import eu.toop.commons.exchange.mock.MSDataRequest;
import eu.toop.demoui.components.Body;
import eu.toop.demoui.components.FreedoniaHeader;
import eu.toop.demoui.form.IdentityForm;
import eu.toop.demoui.form.MainCompanyForm;
import eu.toop.demoui.view.StartView;
import eu.toop.iface.mockup.client.HttpClientInvoker;

import java.io.File;
import java.io.IOException;

public class MainCompanyPage extends BasePage {

  private MainCompanyForm mainCompanyForm;

  public MainCompanyPage (StartView view) {
    super (view);

    setStyleName ("pageMainCompany");

    VerticalLayout main = new VerticalLayout ();
    setHeader (new FreedoniaHeader ());
    setBody (new Body (main));

    main.setSizeUndefined ();

    {
      Label label = new Label ("Register a new branch in Freedonia");
      main.addComponent (label);
    }

    {
      Label label = new Label ("Your personal details");
      main.addComponent (label);
    }

    main.addComponent (new IdentityForm (view.getIdentity (), true, clickEvent -> {
    }));

    {
      Label label = new Label ("Parent company main details");
      main.addComponent (label);
    }

    Button toopButton = new Button ("Click to pre-fill with TOOP.", clickEvent -> {
      File keystoreFile = new File ("src/main/resources/demo-keystore.jks");

      final SignatureHelper aSH = new SignatureHelper (FileHelper.getInputStream (keystoreFile),
        "password",
        null,
        "password");

      try (final NonBlockingByteArrayOutputStream archiveOutput = new NonBlockingByteArrayOutputStream ()) {
        CommonsArrayList<RequestValue> commonsArrayList = new CommonsArrayList<> ();
        commonsArrayList.add(new RequestValue ("companyName", null));
        commonsArrayList.add(new RequestValue ("companyType", null));

        MSDataRequest msDataRequest = new MSDataRequest ("toop::sender", "DE",
          EToopDocumentType.DOCTYPE3.getURIEncoded (),
          EToopProcess.PROC.getURIEncoded (),
          commonsArrayList);

        ToopMessageBuilder.createRequestMessage (msDataRequest, archiveOutput, aSH);

        // Send to DC (see DCInputServlet in toop-mp-webapp)
        String destinationUrl = "http://mp.elonia.toop:8083/dcinput";
        HttpClientInvoker.httpClientCallNoResponse (destinationUrl, archiveOutput.toByteArray ());
      } catch (IOException e) {
        e.printStackTrace ();
      }
    });
    main.addComponent (toopButton);

    mainCompanyForm = new MainCompanyForm (view.getMainCompany (), false, null);
    main.addComponent (mainCompanyForm);

    Button nextButton = new Button ("Confirm and proceed");
    main.addComponent (nextButton);
    nextButton.addClickListener (new Button.ClickListener () {
      public void buttonClick (Button.ClickEvent event) {
        mainCompanyForm.save ();
        view.setCurrentPage (new NewCompanyPage (getView ()));
      }
    });
  }
}
