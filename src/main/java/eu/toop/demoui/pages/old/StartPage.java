package eu.toop.demoui.pages.old;

import com.vaadin.ui.*;
import eu.toop.demoui.components.*;
import eu.toop.demoui.view.StartView;

public class StartPage extends BasePage {

  public StartPage (StartView view) {
    super (view);
    setStyleName ("Start");

    VerticalLayout main = new VerticalLayout ();

    setHeader (new FreedoniaHeader ());
    setBanner (new WelcomeBanner ());
    setBody (new Body(main));

    HorizontalLayout processButtonGroup = new HorizontalLayout ();
    processButtonGroup.setSizeUndefined ();
    processButtonGroup.setStyleName ("processButtonGroup");
    main.addComponent (processButtonGroup);

    {
      Button processButton = new Button ("License and permissions");
      processButton.setStyleName ("processButton");
      processButtonGroup.addComponent (processButton);
    }
    {
      Button processButton = new Button ("Company data mandates");
      processButton.setStyleName ("processButton");
      processButtonGroup.addComponent (processButton);
    }
    {
      Button processButton = new Button ("Register a branch");
      processButton.setStyleName ("processButton");
      processButtonGroup.addComponent (processButton);

      processButton.addClickListener(new Button.ClickListener() {
        public void buttonClick(Button.ClickEvent event) {
          view.setCurrentPage (new eIDModulePage (getView ()));
        }
      });
    }

  }
}
