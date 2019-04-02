package eu.toop.demoui.layouts;

import com.vaadin.ui.Button;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;
import eu.toop.demoui.view.BaseView;

public class DataProviderSelectionWindow extends Window {

    public DataProviderSelectionWindow (final BaseView view, final String countryCode) {
        final Window subWindow = new Window ("Sub-window");
        final VerticalLayout subContent = new VerticalLayout ();
        subWindow.setContent (subContent);

        subWindow.setWidth ("1100px");

        subWindow.setModal (true);
        subWindow.setCaption (null);
        subWindow.setResizable (false);
        subWindow.setClosable (false);

        // Put some components in it
        final DataProviderSelectionPage dataProviderSelectionPage = new DataProviderSelectionPage (countryCode) {
            @Override
            protected void onProceed(String dpScheme, String dpValue) {
                onSave (dpScheme, dpValue);
                subWindow.close ();
            }
        };
        subContent.addComponent (dataProviderSelectionPage);

        final Button cancelButton = new Button ("Cancel", event -> {
            subWindow.close ();
        });
        cancelButton.addStyleName (ValoTheme.BUTTON_BORDERLESS);
        cancelButton.addStyleName ("ConsentCancelButton");

        subContent.addComponent (cancelButton);

        // Center it in the browser window
        subWindow.center ();

        // Open it in the UI
        view.getUI ().addWindow (subWindow);
    }

    protected void onSave (String participantScheme, String participantValue) {
        //
    }

    protected void onCancel () {
        //
    }
}
