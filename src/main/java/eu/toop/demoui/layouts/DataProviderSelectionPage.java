package eu.toop.demoui.layouts;

import com.helger.commons.error.level.EErrorLevel;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import eu.toop.iface.ToopInterfaceClient;
import eu.toop.iface.dpsearch.IDType;
import eu.toop.iface.dpsearch.MatchType;
import eu.toop.iface.dpsearch.ResultListType;
import eu.toop.kafkaclient.ToopKafkaClient;

import java.util.ArrayList;
import java.util.List;

public class DataProviderSelectionPage extends CustomLayout {

    private final ComboBox<IDType> dpSelector = new ComboBox<>("Select data provider");
    private final ProgressBar spinner = new ProgressBar ();
    private Label dpScheme = new Label("");
    private Label dpValue = new Label("");

    final Button proceedButton = new Button ("Proceed");

    public DataProviderSelectionPage(final String countryCode) {
        super ("DataProviderSelectionPage");

        spinner.setCaption ("Please wait while data providers are fetched...");
        spinner.setStyleName ("spinner");
        spinner.setIndeterminate (true);
        spinner.setVisible (true);
        addComponent (spinner, "spinner");

        ToopKafkaClient.send (EErrorLevel.INFO, () -> "[DC] Finding data providers...");

        final List<IDType> listOfPartipantIDs = new ArrayList<>();

        final ResultListType result = ToopInterfaceClient.searchDataProvider(countryCode, "");
        if (result != null) {
            for (MatchType matchType : result.getMatch()) {
                listOfPartipantIDs.add(matchType.getParticipantID());
            }
        }

        spinner.setVisible (false);

        dpSelector.setStyleName("dpSelector");
        dpSelector.setItems(listOfPartipantIDs);
        dpSelector.setItemCaptionGenerator(idType -> idType.getScheme() + "::" + idType.getValue());
        dpSelector.addValueChangeListener(event -> {
            if (event.getSource().isEmpty()) {
                dpScheme.setValue("");
                dpValue.setValue("");
                proceedButton.setEnabled(false);
            } else  {
                dpScheme.setValue(event.getValue().getScheme());
                dpValue.setValue(event.getValue().getValue());
                proceedButton.setEnabled(true);
            }
        });
        addComponent(dpSelector, "dpSelector");

        dpScheme.setStyleName("dpScheme");
        dpScheme.setCaption("Data Provider Scheme");
        dpValue.setStyleName("dpValue");
        dpValue.setCaption("Data Provider Value");

        addComponent(dpScheme, "dpScheme");
        addComponent(dpValue, "dpValue");

        proceedButton.setEnabled(false);
        proceedButton.addClickListener(event -> {
            onProceed(dpScheme.getValue(), dpValue.getValue());
        });
        proceedButton.addStyleName (ValoTheme.BUTTON_BORDERLESS);
        proceedButton.addStyleName ("ConsentAgreeButton");

        addComponent(proceedButton, "proceedButton");
    }

    protected void onProceed(String dpScheme, String dpValue) {
        //
    }

    public String getScheme() {
        return dpScheme.getValue();
    }

    public String getValue() {
        return dpValue.getValue();
    }
}
