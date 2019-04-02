package eu.toop.demoui.layouts;

import com.helger.commons.error.level.EErrorLevel;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import eu.toop.iface.ToopInterfaceClient;
import eu.toop.iface.dpsearch.MatchType;
import eu.toop.iface.dpsearch.ResultListType;
import eu.toop.kafkaclient.ToopKafkaClient;

import java.util.ArrayList;
import java.util.List;

public class DataProviderSelectionPage extends CustomLayout {

    private final ComboBox<MatchType> dpSelector = new ComboBox<>("Select data provider");
    private final ProgressBar spinner = new ProgressBar ();
    String dpScheme = "";
    String dpValue = "";
    private final DataProviderMetadataLayout dpMetadataLayout = new DataProviderMetadataLayout();

    final Button proceedButton = new Button ("Proceed");

    public DataProviderSelectionPage(final String countryCode) {
        super ("DataProviderSelectionPage");

        spinner.setCaption ("Please wait while data providers are fetched...");
        spinner.setStyleName ("spinner");
        spinner.setIndeterminate (true);
        spinner.setVisible (true);
        addComponent (spinner, "spinner");

        ToopKafkaClient.send (EErrorLevel.INFO, () -> "[DC] Finding data providers...");

        final List<MatchType> listOfMatches = new ArrayList<>();

        final ResultListType result = ToopInterfaceClient.searchDataProvider(countryCode, "");
        if (result != null) {
            listOfMatches.addAll(result.getMatch());
        }

        spinner.setVisible (false);

        dpSelector.setStyleName("dpSelector");
        dpSelector.setItems(listOfMatches);
        dpSelector.setItemCaptionGenerator(matchType -> matchType.getParticipantID().getScheme() + "::" + matchType.getParticipantID().getValue());
        dpSelector.addValueChangeListener(event -> {
            if (event.getSource().isEmpty()) {
                dpScheme = "";
                dpValue = "";
                proceedButton.setEnabled(false);
                dpMetadataLayout.setData(null);
            } else  {
                dpScheme = event.getValue().getParticipantID().getScheme();
                dpValue = event.getValue().getParticipantID().getValue();
                dpMetadataLayout.setData(event.getValue());
                proceedButton.setEnabled(true);
            }
        });
        addComponent(dpSelector, "dpSelector");
        addComponent(dpMetadataLayout, "dpMetadataLayout");

        proceedButton.setEnabled(false);
        proceedButton.addClickListener(event -> {
            onProceed(dpScheme, dpValue);
        });
        proceedButton.addStyleName (ValoTheme.BUTTON_BORDERLESS);
        proceedButton.addStyleName ("ConsentAgreeButton");

        addComponent(proceedButton, "proceedButton");
    }

    protected void onProceed(String dpScheme, String dpValue) {
        //
    }

    public String getScheme() {
        return dpScheme;
    }

    public String getValue() {
        return dpValue;
    }
}
