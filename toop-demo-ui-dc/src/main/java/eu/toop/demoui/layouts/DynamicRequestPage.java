/**
 * Copyright (C) 2018-2019 toop.eu
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import com.helger.commons.error.level.EErrorLevel;
import com.helger.commons.string.StringHelper;
import com.helger.commons.timing.StopWatch;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;

import eu.toop.commons.codelist.EPredefinedDocumentTypeIdentifier;
import eu.toop.commons.codelist.EPredefinedProcessIdentifier;
import eu.toop.commons.concept.ConceptValue;
import eu.toop.commons.dataexchange.v140.TDEAddressWithLOAType;
import eu.toop.commons.dataexchange.v140.TDEDataRequestSubjectType;
import eu.toop.commons.dataexchange.v140.TDEDocumentRequestType;
import eu.toop.commons.dataexchange.v140.TDEErrorType;
import eu.toop.commons.dataexchange.v140.TDELegalPersonType;
import eu.toop.commons.dataexchange.v140.TDENaturalPersonType;
import eu.toop.commons.dataexchange.v140.TDERoutingInformationType;
import eu.toop.commons.dataexchange.v140.TDETOOPRequestType;
import eu.toop.commons.error.ToopErrorException;
import eu.toop.commons.exchange.ToopMessageBuilder140;
import eu.toop.commons.jaxb.ToopXSDHelper140;
import eu.toop.commons.usecase.EToopEntityType;
import eu.toop.demoui.DCToToopInterfaceMapper;
import eu.toop.demoui.DCUIConfig;
import eu.toop.demoui.endpoints.DemoUIToopInterfaceHelper;
import eu.toop.demoui.view.BaseView;
import eu.toop.iface.ToopInterfaceConfig;
import eu.toop.kafkaclient.ToopKafkaClient;
import oasis.names.specification.ubl.schema.xsd.unqualifieddatatypes_21.TextType;

public class DynamicRequestPage extends CustomLayout {
  private static enum ERequestType {
    DATA,
    DOCUMENT
  }

  private final BaseView view;
  private final ProgressBar spinner = new ProgressBar ();

  private final ComboBox<String> countryCodeField = new ComboBox<> ();
  private final ComboBox<EPredefinedDocumentTypeIdentifier> documentTypeField = new ComboBox<> ();
  private final TextField naturalPersonIdentifierField = new TextField ();
  private final TextField naturalPersonFirstNameField = new TextField ();
  private final TextField naturalPersonFamilyNameField = new TextField ();
  private final TextField legalPersonUniqueIdentifierField = new TextField ();
  private final TextField legalPersonCompanyNameField = new TextField ();
  private final TextField documentIdentifierField = new TextField();
  private final TextField dataProviderScheme = new TextField ();
  private final TextField dataProviderName = new TextField ();
  private Label errorLabel = null;
  private Label conceptErrorsLabel = null;
  private final Button dataProvidersFindButton = new Button ("Find Data Providers");
  private final Button dataProvidersManualButton = new Button ("Manually enter Data Provider");
  private final Button sendButton = new Button ("Send Data Element Request");
  private final Button sendDocumentRequestButton = new Button ("Send Document Request");

  private Label requestIdLabel = null;
  private boolean responseReceived = false;
  private final StopWatch sw = StopWatch.createdStopped ();
  private final Timer timeoutTimer = new Timer ();

  private final static List<ConceptValue> conceptList = new ArrayList<> ();

  static {
    final String sNS = DCUIConfig.getConceptNamespace ();
    conceptList.add (new ConceptValue (sNS, "FreedoniaStreetAddress"));
    conceptList.add (new ConceptValue (sNS, "FreedoniaSSNumber"));
    conceptList.add (new ConceptValue (sNS, "FreedoniaCompanyCode"));
    conceptList.add (new ConceptValue (sNS, "FreedoniaVATNumber"));
    conceptList.add (new ConceptValue (sNS, "FreedoniaCompanyType"));
    conceptList.add (new ConceptValue (sNS, "FreedoniaRegistrationDate"));
    conceptList.add (new ConceptValue (sNS, "FreedoniaRegistrationNumber"));
    conceptList.add (new ConceptValue (sNS, "FreedoniaCompanyName"));
    conceptList.add (new ConceptValue (sNS, "FreedoniaNaceCode"));
    conceptList.add (new ConceptValue (sNS, "FreedoniaActivityDescription"));
    conceptList.add (new ConceptValue (sNS, "FreedoniaRegistrationAuthority"));
    conceptList.add (new ConceptValue (sNS, "FreedoniaLegalStatus"));
    conceptList.add (new ConceptValue (sNS, "FreedoniaLegalStatusEffectiveDate"));
  }

  public DynamicRequestPage (final BaseView view) {

    super ("DynamicRequestPage");
    this.view = view;

    setHeight ("100%");

    resetError ();

    spinner.setCaption ("Please wait while your request for data is processed...");
    spinner.setStyleName ("spinner");
    spinner.setIndeterminate (true);
    spinner.setVisible (false);
    addComponent (spinner, "spinner");

    dataProviderScheme.setVisible (false);
    dataProviderName.setVisible (false);

    dataProviderScheme.setPlaceholder ("Data Provider Scheme");
    // dataProviderScheme.setReadOnly(true);
    dataProviderName.setPlaceholder ("Data Provider Name");
    // dataProviderName.setReadOnly(true);

    countryCodeField.setStyleName ("countryCodeDropdown");
    countryCodeField.setItems (DCUIConfig.getCountryCodes ());
    documentTypeField.setStyleName ("documentTypeDropdown");
    // add all Request document types only
    final List<EPredefinedDocumentTypeIdentifier> aDocTypes = new ArrayList<> ();
    for (final EPredefinedDocumentTypeIdentifier e : EPredefinedDocumentTypeIdentifier.values ())
      if (e.getID ().contains ("::Request##"))
        aDocTypes.add (e);

    documentTypeField.setItems (aDocTypes);
    // Don't allow new items
    documentTypeField.setNewItemHandler (null);

    addComponent (countryCodeField, "countryCodeField");
    addComponent (documentTypeField, "documentTypeField");
    addComponent (naturalPersonIdentifierField, "naturalPersonIdentifierField");
    addComponent (naturalPersonFirstNameField, "naturalPersonFirstNameField");
    addComponent (naturalPersonFamilyNameField, "naturalPersonFamilyNameField");
    addComponent (legalPersonUniqueIdentifierField, "legalPersonUniqueIdentifierField");
    addComponent (legalPersonCompanyNameField, "legalPersonCompanyNameField");
    addComponent(documentIdentifierField, "documentIdentifierField");
    addComponent (dataProviderScheme, "dataProviderScheme");
    addComponent (dataProviderName, "dataProviderName");

    dataProvidersFindButton.addStyleName (ValoTheme.BUTTON_BORDERLESS);
    dataProvidersFindButton.addClickListener ( (evt) -> {

      new DataProviderSelectionWindow (view, countryCodeField.getValue ()) {
        @Override
        public void onSave (final String participantScheme, final String participantValue) {
          dataProviderScheme.setValue (participantScheme);
          dataProviderName.setValue (participantValue);

          dataProvidersFindButton.setVisible (false);
          dataProvidersManualButton.setVisible (false);
          dataProviderScheme.setVisible (true);
          dataProviderName.setVisible (true);
        }

        @Override
        public void onCancel () {
          dataProvidersFindButton.setVisible (true);
          dataProvidersManualButton.setVisible (true);
          dataProviderScheme.setVisible (false);
          dataProviderName.setVisible (false);
        }
      };
    });

    dataProvidersManualButton.addStyleName (ValoTheme.BUTTON_BORDERLESS);
    dataProvidersManualButton.addClickListener ( (evt) -> {
      ToopKafkaClient.send (EErrorLevel.INFO, () -> "[DC] Manual entry of data provider...");
      dataProvidersFindButton.setVisible (false);
      dataProvidersManualButton.setVisible (false);
      dataProviderScheme.setVisible (true);
      dataProviderName.setVisible (true);
      dataProviderScheme.setReadOnly (false);
      dataProviderName.setReadOnly (false);
    });

    sendButton.addStyleName (ValoTheme.BUTTON_BORDERLESS);
    sendButton.addStyleName ("ConsentAgreeButton");
    sendButton.addClickListener (new SendRequest (ERequestType.DATA));

    sendDocumentRequestButton.addStyleName (ValoTheme.BUTTON_BORDERLESS);
    sendDocumentRequestButton.addClickListener (new SendRequest (ERequestType.DOCUMENT));

    addComponent (dataProvidersFindButton, "dataProvidersFindButton");
    addComponent (dataProvidersManualButton, "dataProvidersManualButton");
    addComponent (sendButton, "sendButton");
    addComponent (sendDocumentRequestButton, "sendDocumentRequestButton");
  }

  class SendRequest implements Button.ClickListener {

    private final ERequestType type;

    public SendRequest (final ERequestType type) {
      this.type = type;
    }

    @Override
    public void buttonClick (final Button.ClickEvent clickEvent) {

      responseReceived = false;
      resetError ();
      removeMainCompanyForm ();
      removeKeyValueForm ();
      sw.restart ();

      try {
        final String identifierPrefix = countryCodeField.getValue () + "/" + DCUIConfig.getSenderCountryCode () + "/";

        if (type == ERequestType.DATA) {
          ToopKafkaClient.send (EErrorLevel.INFO,
                                () -> "[DC] Requesting concepts: "
                                      + StringHelper.getImplodedMapped (", ", conceptList,
                                                                        x -> x.getNamespace () + "#" + x.getValue ()));
        } else {
          ToopKafkaClient.send (EErrorLevel.INFO, () -> "[DC] Requesting document.");
        }

        final TDEDataRequestSubjectType aDS = new TDEDataRequestSubjectType ();
        if (!naturalPersonIdentifierField.isEmpty ()) {
          boolean bCanAddNaturalPerson = true;
          final TDENaturalPersonType aNP = new TDENaturalPersonType ();

          // Mandatory field - checked in Schematrin
          {
            aDS.setDataRequestSubjectTypeCode (ToopXSDHelper140.createCode (EToopEntityType.NATURAL_PERSON.getID ()));
            final String naturalPersonIdentifier = naturalPersonIdentifierField.getValue ();
            if (StringHelper.hasText (naturalPersonIdentifier))
              aNP.setPersonIdentifier (ToopXSDHelper140.createIdentifierWithLOA (identifierPrefix
                                                                                 + naturalPersonIdentifier));
            else
              bCanAddNaturalPerson = false;
          }

          // Mandatory field
          String naturalPersonFirstName = "";
          if (!naturalPersonFirstNameField.isEmpty ()) {
            naturalPersonFirstName = naturalPersonFirstNameField.getValue ();
          }
          aNP.setFirstName (ToopXSDHelper140.createTextWithLOA (naturalPersonFirstName));

          // Mandatory field
          String naturalPersonFamilyName = "";
          if (!naturalPersonFamilyNameField.isEmpty ()) {
            naturalPersonFamilyName = naturalPersonFamilyNameField.getValue ();
          }
          aNP.setFamilyName (ToopXSDHelper140.createTextWithLOA (naturalPersonFamilyName));

          // Mandatory field
          aNP.setBirthDate (ToopXSDHelper140.createDateWithLOANow ());

          final TDEAddressWithLOAType aAddress = new TDEAddressWithLOAType ();
          aAddress.setCountryCode (ToopXSDHelper140.createCodeWithLOA (countryCodeField.getValue ()));
          aNP.setNaturalPersonLegalAddress (aAddress);

          if (bCanAddNaturalPerson) {
            aDS.setNaturalPerson (aNP);
          }
        }

        if (!legalPersonUniqueIdentifierField.isEmpty ()) {
          boolean bCanAddLegalPerson = true;
          final TDELegalPersonType aLP = new TDELegalPersonType ();

          // Mandatory field
          final String id = legalPersonUniqueIdentifierField.getValue ();
          if (StringHelper.hasText (id))
            aLP.setLegalPersonUniqueIdentifier (ToopXSDHelper140.createIdentifierWithLOA (identifierPrefix + id));
          else
            bCanAddLegalPerson = false;

          // Mandatory field
          String legalName = "";
          if (!legalPersonCompanyNameField.isEmpty ()) {
            legalName = legalPersonCompanyNameField.getValue ();
          }
          aLP.setLegalName (ToopXSDHelper140.createTextWithLOA (legalName));

          final TDEAddressWithLOAType aAddress = new TDEAddressWithLOAType ();
          aAddress.setCountryCode (ToopXSDHelper140.createCodeWithLOA (countryCodeField.getValue ()));
          aLP.setLegalPersonLegalAddress (aAddress);

          if (bCanAddLegalPerson) {
            aDS.setDataRequestSubjectTypeCode (ToopXSDHelper140.createCode (EToopEntityType.LEGAL_ENTITY.getID ()));
            aDS.setLegalPerson (aLP);
          }
        }

        final String srcCountryCode = DCUIConfig.getSenderCountryCode ();
        final String dstCountryCode = countryCodeField.getValue ();
        final EPredefinedDocumentTypeIdentifier eDocumentTypeID = documentTypeField.getValue ();
        final EPredefinedProcessIdentifier eProcessID;
        if (eDocumentTypeID == EPredefinedDocumentTypeIdentifier.REQUEST_SHIPCERTIFICATE
            || eDocumentTypeID == EPredefinedDocumentTypeIdentifier.REQUEST_SHIPCERTIFICATE_LIST
            || eDocumentTypeID == EPredefinedDocumentTypeIdentifier.REQUEST_CREWCERTIFICATE
            || eDocumentTypeID == EPredefinedDocumentTypeIdentifier.REQUEST_CREWCERTIFICATE_LIST)
          eProcessID = EPredefinedProcessIdentifier.TWOPHASEDREQUESTRESPONSE;
        else if (type == ERequestType.DATA)
          eProcessID = EPredefinedProcessIdentifier.DATAREQUESTRESPONSE;
        else
          eProcessID = EPredefinedProcessIdentifier.DOCUMENTREQUESTRESPONSE;
        final TDETOOPRequestType aRequest = ToopMessageBuilder140.createMockRequest (aDS, srcCountryCode,
                                                                                     dstCountryCode,
                                                                                     ToopXSDHelper140.createIdentifier (DCUIConfig.getSenderIdentifierScheme (),
                                                                                                                        DCUIConfig.getSenderIdentifierValue ()),
                                                                                     eDocumentTypeID, eProcessID,
                                                                                     type == ERequestType.DATA ? conceptList
                                                                                                               : null);

        final UUID uuid = UUID.randomUUID ();
        requestIdLabel = new Label (uuid.toString ());
        addComponent (requestIdLabel, "requestId");
        aRequest.setDocumentUniversalUniqueIdentifier (ToopXSDHelper140.createIdentifier ("UUID", null,
                                                                                          uuid.toString ()));
        aRequest.setSpecificationIdentifier (ToopXSDHelper140.createIdentifier (EPredefinedDocumentTypeIdentifier.DOC_TYPE_SCHEME,
                                                                                eDocumentTypeID.getID ()
                                                                                               .substring (0,
                                                                                                           eDocumentTypeID.getID ()
                                                                                                                          .indexOf ("##"))));

        if (type == ERequestType.DOCUMENT) {
          final TDEDocumentRequestType documentRequestType = new TDEDocumentRequestType ();
          documentRequestType.setDocumentURI (ToopXSDHelper140.createIdentifier ("https://koolitus.emde.ee/cc/b0/67/123456"));
          documentRequestType.setDocumentRequestIdentifier (ToopXSDHelper140.createIdentifier ("demo-agency",
                                                                                               "toop-doctypeid-qns",
                                                                                               UUID.randomUUID ()
                                                                                                   .toString ()));
          documentRequestType.setDocumentRequestTypeCode (ToopXSDHelper140.createCode ("ETR"));
          aRequest.addDocumentRequest (documentRequestType);
        }

        if (!dataProviderScheme.isEmpty () && !dataProviderName.isEmpty ()) {
          final TDERoutingInformationType routingInformation = aRequest.getRoutingInformation ();
          routingInformation.setDataProviderElectronicAddressIdentifier (ToopXSDHelper140.createIdentifier (dataProviderScheme.getValue (),
                                                                                                            dataProviderName.getValue ()));
          aRequest.setRoutingInformation (routingInformation);

          ToopKafkaClient.send (EErrorLevel.INFO,
                                () -> "[DC] Set routing information to specific data provider: ["
                                      + dataProviderScheme.getValue () + ", " + dataProviderName.getValue () + "]");
        }

        DemoUIToopInterfaceHelper.dumpRequest (aRequest);

        ToopKafkaClient.send (EErrorLevel.INFO,
                              () -> "[DC] Sending request to TC: " + ToopInterfaceConfig.getToopConnectorDCUrl ());

        DCToToopInterfaceMapper.sendRequest (aRequest, getUI ());

        spinner.setVisible (true);
        // setEnabled (false);

        // Fake response
        timeoutTimer.schedule (new TimerTask () {
          @Override
          public void run () {
            if (!responseReceived)
              setErrorTimeout ();
          }
        }, 60000);
      } catch (final IOException | ToopErrorException ex) {
        // Convert from checked to unchecked
        throw new RuntimeException (ex);
      }
    }
  }

  private void _onResponseReceived () {
    responseReceived = true;
    timeoutTimer.cancel ();
    spinner.setVisible (false);
    sw.stop ();
    addComponent (new Label ("Last request took " + sw.getMillis () + " milliseconds"), "durationText");
  }

  public void setError (final List<TDEErrorType> errors) {
    _onResponseReceived ();

    final StringBuilder sb = new StringBuilder ();

    for (final TDEErrorType tdeErrorType : errors) {
      sb.append (" Error: ").append ("\n");

      if (tdeErrorType.getOrigin () != null) {
        sb.append (" - Origin: ").append (tdeErrorType.getOrigin ().getValue ()).append ("\n");
      } else {
        sb.append (" - Origin: ").append ("N/A").append ("\n");
      }

      if (tdeErrorType.getCategory () != null) {
        sb.append (" - Category: ").append (tdeErrorType.getCategory ().getValue ()).append ("\n");
      } else {
        sb.append (" - Category: ").append ("N/A").append ("\n");
      }

      if (tdeErrorType.getErrorCode () != null) {
        sb.append (" - Error Code: ").append (tdeErrorType.getErrorCode ().getValue ()).append ("\n");
      } else {
        sb.append (" - Error Code: ").append ("N/A").append ("\n");
      }

      if (tdeErrorType.getSeverity () != null) {
        sb.append (" - Severity: ").append (tdeErrorType.getSeverity ().getValue ()).append ("\n");
      } else {
        sb.append (" - Severity: ").append ("N/A").append ("\n");
      }

      for (final TextType errorText : tdeErrorType.getErrorText ()) {

        if (errorText != null) {
          sb.append (" - Error Text: ").append (errorText.getValue ()).append ("\n");
        } else {
          sb.append (" - Error Text: ").append ("N/A").append ("\n");
        }
      }
    }

    errorLabel = new Label (sb.toString ());
    errorLabel.setContentMode (ContentMode.PREFORMATTED);
    addComponent (errorLabel, "errorLabel");
  }

  public void setErrorTimeout () {

    _onResponseReceived ();

    final StringBuilder sb = new StringBuilder ();
    sb.append (" Error: ").append ("\n");
    sb.append (" - ").append ("Timeout").append ("\n");

    errorLabel = new Label (sb.toString ());
    errorLabel.setContentMode (ContentMode.PREFORMATTED);
    addComponent (errorLabel, "errorLabel");
  }

  public void setConceptErrors (final String conceptErrors) {

    _onResponseReceived ();

    conceptErrorsLabel = new Label (conceptErrors);
    conceptErrorsLabel.setContentMode (ContentMode.PREFORMATTED);
    addComponent (conceptErrorsLabel, "conceptErrorsLabel");
  }

  public void resetError () {
    removeComponent ("errorLabel");
  }

  public void addMainCompanyForm () {

    _onResponseReceived ();

    final MainCompanyForm mainCompanyForm = new MainCompanyForm (view.getToopDataBean (), false);

    final BaseForm baseForm = new BaseForm (mainCompanyForm, "Company details");
    addComponent (baseForm, "mainCompanyForm");
    view.setMainCompanyForm (mainCompanyForm);
  }

  public void addKeyValueForm () {

    _onResponseReceived ();

    final KeyValueForm keyValueForm = new KeyValueForm (view.getToopDataBean (), false);

    final BaseForm baseForm = new BaseForm (keyValueForm, "Key value details");
    addComponent (baseForm, "keyValueForm");
    view.setKeyValueForm (keyValueForm);
  }

  public void removeMainCompanyForm () {
    removeComponent ("mainCompanyForm");
  }

  public void removeKeyValueForm () {
    removeComponent ("keyValueForm");
  }

  public String getRequestId () {
    if (requestIdLabel != null) {
      return requestIdLabel.getValue ();
    }
    return null;
  }
}
