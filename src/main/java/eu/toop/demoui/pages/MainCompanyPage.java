package eu.toop.demoui.pages;

import java.io.IOException;
import java.util.Arrays;

import org.jsoup.UncheckedIOException;

import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

import eu.toop.demoui.components.Body;
import eu.toop.demoui.components.FreedoniaHeader;
import eu.toop.demoui.form.IdentityForm;
import eu.toop.demoui.form.MainCompanyForm;
import eu.toop.demoui.view.StartView;
import eu.toop.iface.ToopInterfaceManager;

public class MainCompanyPage extends BasePage {

  private final MainCompanyForm mainCompanyForm;

  public MainCompanyPage (final StartView view) {
    super (view);

    setStyleName ("pageMainCompany");

    final VerticalLayout main = new VerticalLayout ();
    setHeader (new FreedoniaHeader ());
    setBody (new Body (main));

    main.setSizeUndefined ();

    {
      final Label label = new Label ("Register a new branch in Freedonia");
      main.addComponent (label);
    }

    {
      final Label label = new Label ("Your personal details");
      main.addComponent (label);
    }

    main.addComponent (new IdentityForm (view.getIdentity (), true, clickEvent -> {
    }));

    {
      final Label label = new Label ("Parent company main details");
      main.addComponent (label);
    }

    final Button toopButton = new Button ("Click to pre-fill with TOOP.", clickEvent -> {
      try {
        ToopInterfaceManager.requestConcepts (Arrays.asList ("companyName", "companyType"));
      } catch (final IOException ex) {
        throw new UncheckedIOException (ex);
      }
    });
    main.addComponent (toopButton);

    mainCompanyForm = new MainCompanyForm (view.getMainCompany (), false, null);
    main.addComponent (mainCompanyForm);

    final Button nextButton = new Button ("Confirm and proceed");
    main.addComponent (nextButton);
    nextButton.addClickListener (event -> {
      mainCompanyForm.save ();
      view.setCurrentPage (new NewCompanyPage (getView ()));
    });
  }
}
