package eu.toop.demoui.layouts;

import com.vaadin.ui.Button;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;
import eu.toop.demoui.view.BaseView;

public class DataProviderSelectionWindow extends Window {

    public DataProviderSelectionWindow (final BaseView view) {
        final Window subWindow = new Window ("Sub-window");
        final VerticalLayout subContent = new VerticalLayout ();
        subWindow.setContent (subContent);

        subWindow.setWidth ("800px");

        subWindow.setModal (true);
        subWindow.setCaption (null);
        subWindow.setResizable (false);
        subWindow.setClosable (false);


        // Put some components in it
        final DataProviderSelectionPage dataProviderSelectionPage = new DataProviderSelectionPage ();
        subContent.addComponent (dataProviderSelectionPage);

        final Button proceedButton = new Button ("Proceed", event -> {
            onProceed ();
            subWindow.close ();
        });
        proceedButton.addStyleName (ValoTheme.BUTTON_BORDERLESS);
        proceedButton.addStyleName ("ConsentAgreeButton");

        final Button cancelButton = new Button ("Cancel", event -> {
            onCancel ();
            subWindow.close ();
        });
        cancelButton.addStyleName (ValoTheme.BUTTON_BORDERLESS);
        cancelButton.addStyleName ("ConsentCancelButton");

        subContent.addComponent (proceedButton);
        subContent.addComponent (cancelButton);

        // Center it in the browser window
        subWindow.center ();

        // Open it in the UI
        view.getUI ().addWindow (subWindow);
    }

    protected void onProceed () {
        // The user may override this method to execute their own code when the user
        // click on the 'consent'-button.
    }

    protected void onCancel () {
        // The user may override this method to execute their own code when the user
        // click on the 'self-provide'-button.
    }
}
