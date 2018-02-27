package eu.toop.demoui.pages;

import com.vaadin.ui.*;
import eu.toop.demoui.components.*;
import eu.toop.demoui.view.StartView;

public class eIDModulePage extends BasePage {
  public eIDModulePage (StartView view) {
    super (view);

    setStyleName ("eIDModule");

    VerticalLayout main = new VerticalLayout ();
    setHeader (new FreedoniaHeader ());
    setBody (new Body (new InfoStart (), main));

    main.setSizeUndefined ();

    TextField countryField = new TextField ("Login");
    countryField.setPlaceholder ("Begin to type your country of origin");
    main.addComponent (countryField);

    Button nextButton = new Button ("Next");
    main.addComponent (nextButton);

    nextButton.addClickListener(new Button.ClickListener() {
      public void buttonClick(Button.ClickEvent event) {
        view.setCurrentPage (new IdentityProviderPage (getView ()));
      }
    });
  }
}
