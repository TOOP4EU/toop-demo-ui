package eu.toop.demoui.layouts;

import com.vaadin.data.Binder;
import com.vaadin.ui.*;
import eu.toop.commons.codelist.EPredefinedDocumentTypeIdentifier;
import eu.toop.commons.codelist.EPredefinedProcessIdentifier;
import eu.toop.commons.concept.ConceptValue;
import eu.toop.demoui.bean.Identity;
import eu.toop.demoui.bean.TestRequest;
import eu.toop.demoui.fields.IdentifierTypeField;

import eu.toop.commons.jaxb.ToopXSDHelper;
import eu.toop.iface.ToopInterfaceClient;
import oasis.names.specification.ubl.schema.xsd.unqualifieddatatypes_21.IdentifierType;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;

public class TestRequestForm extends FormLayout {

  private Binder<TestRequest> binder = new Binder<> ();

  public TestRequestForm(final TestRequest testRequest, final boolean readOnly, final Button.ClickListener onSubmit) {

    setCaption ("Test Request Form");

    final Identity identity = new Identity();

    final IdentifierType identifierType = ToopXSDHelper.createIdentifier ("iso6523-actorid-upis", "9999:freedonia");

    final List<String> destCountryCodeList = new ArrayList<> ();
    destCountryCodeList.add ("GF");
    destCountryCodeList.add ("SV");

    final List<EPredefinedDocumentTypeIdentifier> toopDocumentTypeList = new ArrayList<> ();
    toopDocumentTypeList.add(EPredefinedDocumentTypeIdentifier.REQUEST_REGISTEREDORGANIZATION);
    toopDocumentTypeList.add(EPredefinedDocumentTypeIdentifier.RESPONSE_REGISTEREDORGANIZATION);

    final List<EPredefinedProcessIdentifier> toopProcessList = new ArrayList<> ();
    toopProcessList.add(EPredefinedProcessIdentifier.DATAREQUESTRESPONSE);
    toopProcessList.add(EPredefinedProcessIdentifier.DOCUMENTREQUESTRESPONSE);

    List<ConceptValue> conceptValueList = new ArrayList<> ();
    conceptValueList.add(new ConceptValue ("http://example.register.fre/freedonia-business-register", "FreedoniaAddress"));
    conceptValueList.add(new ConceptValue ("http://example.register.fre/freedonia-business-register", "FreedoniaSSNumber"));
    conceptValueList.add(new ConceptValue ("http://example.register.fre/freedonia-business-register", "FreedoniaBusinessCode"));
    conceptValueList.add(new ConceptValue ("http://example.register.fre/freedonia-business-register", "FreedoniaVATNumber"));
    conceptValueList.add(new ConceptValue ("http://example.register.fre/freedonia-business-register", "FreedoniaCompanyType"));
    conceptValueList.add(new ConceptValue ("http://example.register.fre/freedonia-business-register", "FreedoniaRegistrationDate"));
    conceptValueList.add(new ConceptValue ("http://example.register.fre/freedonia-business-register", "FreedoniaRegistrationNumber"));
    conceptValueList.add(new ConceptValue ("http://example.register.fre/freedonia-business-register", "FreedoniaCompanyName"));
    conceptValueList.add(new ConceptValue ("http://example.register.fre/freedonia-business-register", "FreedoniaCompanyNaceCode"));
    conceptValueList.add(new ConceptValue ("http://example.register.fre/freedonia-business-register", "FreedoniaActivityDeclaration"));
    conceptValueList.add(new ConceptValue ("http://example.register.fre/freedonia-business-register", "FreedoniaRegistrationAuthority"));

    final IdentityForm identityForm = new IdentityForm(identity, false, null);
    identityForm.setCaption ("Identity");
    addComponent (identityForm);

    final IdentifierTypeField identifierTypeField = new IdentifierTypeField ();
    identifierTypeField.setValue (identifierType);
    addComponent (identifierTypeField);

    final ComboBox<String> destCountryCodeComboBox = new ComboBox<>("Select Destination Country Code");
    destCountryCodeComboBox.setItems(destCountryCodeList);
    destCountryCodeComboBox.setSelectedItem(destCountryCodeList.get(0));
    destCountryCodeComboBox.setWidth ("100%");
    addComponent (destCountryCodeComboBox);

    final ComboBox<EPredefinedDocumentTypeIdentifier> toopDocumentTypeComboBox = new ComboBox<>("Select Toop Document Type");
    toopDocumentTypeComboBox.setItems(toopDocumentTypeList);
    toopDocumentTypeComboBox.setSelectedItem(toopDocumentTypeList.get(0));
    toopDocumentTypeComboBox.setWidth ("100%");
    addComponent (toopDocumentTypeComboBox);

    final ComboBox<EPredefinedProcessIdentifier> toopProcessComboBox = new ComboBox<>("Select Toop Process");
    toopProcessComboBox.setItems(toopProcessList);
    toopProcessComboBox.setSelectedItem(toopProcessList.get(0));
    toopProcessComboBox.setWidth ("100%");
    addComponent (toopProcessComboBox);

    final CheckBoxGroup<ConceptValue> conceptCheckBoxGroup = new CheckBoxGroup<>("Select Concepts");
    conceptCheckBoxGroup.setItems(conceptValueList);
    addComponent (conceptCheckBoxGroup);

    final Button sendToopRequest = new Button("Send Toop Request");
    sendToopRequest.addClickListener ((Button.ClickListener) clickEvent -> {

      final List<ConceptValue> finalConceptList = new ArrayList<> ();
      finalConceptList.addAll (conceptCheckBoxGroup.getSelectedItems ());

      /*try {
        ToopInterfaceClient.createRequestAndSendToToopConnector (ToopXSDHelper.createIdentifier (identifierTypeField.getValue ().getSchemeID (),
            identifierTypeField.getValue ().getValue ()),
            destCountryCodeComboBox.getSelectedItem ().get (),
            toopDocumentTypeComboBox.getSelectedItem ().get (),
            toopProcessComboBox.getSelectedItem ().get (),
            finalConceptList);
      } catch (IOException e) {
        e.printStackTrace ();
      }*/
    });
    addComponent (sendToopRequest);
  }
}
