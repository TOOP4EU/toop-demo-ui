/**
 * Copyright (C) 2018-2020 toop.eu
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
package eu.toop.demoui.layouts;

import java.util.ArrayList;
import java.util.List;

import com.helger.commons.error.level.EErrorLevel;
import com.helger.pd.searchapi.v1.MatchType;
import com.helger.pd.searchapi.v1.ResultListType;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.themes.ValoTheme;

import eu.toop.iface.ToopInterfaceClient;
import eu.toop.kafkaclient.ToopKafkaClient;

public class DataProviderSelectionPage extends CustomLayout {

  private final ComboBox<MatchType> dpSelector = new ComboBox<> ("Select data provider");
  private final ProgressBar spinner = new ProgressBar ();
  String dpScheme = "";
  String dpValue = "";
  private final DataProviderMetadataLayout dpMetadataLayout = new DataProviderMetadataLayout ();

  final Button proceedButton = new Button ("Proceed");

  public DataProviderSelectionPage (final String countryCode) {
    super ("DataProviderSelectionPage");

    spinner.setCaption ("Please wait while data providers are fetched...");
    spinner.setStyleName ("spinner");
    spinner.setIndeterminate (true);
    spinner.setVisible (true);
    addComponent (spinner, "spinner");

    ToopKafkaClient.send (EErrorLevel.INFO, () -> "[DC] Finding data providers...");

    final List<MatchType> listOfMatches = new ArrayList<> ();

    final ResultListType result = ToopInterfaceClient.searchDataProvider (countryCode, null);
    if (result != null)
      listOfMatches.addAll (result.getMatch ());

    spinner.setVisible (false);

    dpSelector.setStyleName ("dpSelector");
    dpSelector.setItems (listOfMatches);
    dpSelector.setItemCaptionGenerator (matchType -> matchType.getParticipantID ().getScheme () + "::"
                                                     + matchType.getParticipantID ().getValue ());
    dpSelector.addValueChangeListener (event -> {
      if (event.getSource ().isEmpty ()) {
        dpScheme = "";
        dpValue = "";
        proceedButton.setEnabled (false);
        dpMetadataLayout.setData (null);
      } else {
        dpScheme = event.getValue ().getParticipantID ().getScheme ();
        dpValue = event.getValue ().getParticipantID ().getValue ();
        dpMetadataLayout.setData (event.getValue ());
        proceedButton.setEnabled (true);
      }
    });
    addComponent (dpSelector, "dpSelector");
    addComponent (dpMetadataLayout, "dpMetadataLayout");

    proceedButton.setEnabled (false);
    proceedButton.addClickListener (event -> {
      onProceed (dpScheme, dpValue);
    });
    proceedButton.addStyleName (ValoTheme.BUTTON_BORDERLESS);
    proceedButton.addStyleName ("ConsentAgreeButton");

    addComponent (proceedButton, "proceedButton");
  }

  protected void onProceed (final String dpScheme, final String dpValue) {
    //
  }

  public String getScheme () {
    return dpScheme;
  }

  public String getValue () {
    return dpValue;
  }
}
