package eu.toop.demoui.pages;

import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import eu.toop.demoui.components.Body;
import eu.toop.demoui.components.EloniaHeader;
import eu.toop.demoui.components.InfoIdP;
import eu.toop.demoui.components.InfoStart;
import eu.toop.demoui.view.StartView;

public class IdP extends Base {
  public IdP (StartView view) {
    super (view);

    setStyleName ("eIDModule");

    VerticalLayout main = new VerticalLayout ();
    setHeader (new EloniaHeader ());
    setBody (new Body (new InfoIdP (), main));

    main.setSizeUndefined ();

    TextField usernameField = new TextField ("Login");
    usernameField.setPlaceholder ("Username");
    main.addComponent (usernameField);

    TextField passwordField = new TextField ();
    passwordField.setPlaceholder ("Password");
    main.addComponent (passwordField);

    Button nextButton = new Button ("Use my identity");
    main.addComponent (nextButton);
  }
}
