package eu.toop.demoui.fields;

import com.vaadin.ui.*;
import oasis.names.specification.ubl.schema.xsd.unqualifieddatatypes_21.IdentifierType;

public class IdentifierTypeField extends CustomField<IdentifierType> {
  private TextField schemeURI = new TextField ("Scheme URI");
  private TextField schemeVersionID = new TextField ("Scheme Version ID");
  private TextField schemeName = new TextField ("Scheme Name");
  private TextField schemeDataURI = new TextField ("Scheme Data URI");
  private TextField schemeAgencyName = new TextField ("Scheme Agency Name");
  private TextField schemeAgencyID = new TextField ("Scheme Agency ID");
  private TextField schemeID = new TextField ("Scheme ID");
  private TextField value = new TextField ("Value");

  private IdentifierType identifierType;

  @Override
  protected Component initContent() {
    FormLayout layout = new FormLayout ();
    setCaption ("Identifier Type");
    layout.addComponent(schemeID);
    layout.addComponent(value);
    layout.addComponent(schemeURI);
    layout.addComponent(schemeVersionID);
    layout.addComponent(schemeName);
    layout.addComponent(schemeDataURI);
    layout.addComponent(schemeAgencyName);
    layout.addComponent(schemeAgencyID);

    return layout;
  }

  @Override
  public IdentifierType getValue() {
    return identifierType;
  }

  @Override
  protected void doSetValue(IdentifierType identifierType) {
    this.identifierType = identifierType;

    if (identifierType.getSchemeID () != null) schemeID.setValue (identifierType.getSchemeID ());
    if (identifierType.getValue () != null) value.setValue (identifierType.getValue ());
    if (identifierType.getSchemeURI () != null) schemeURI.setValue (identifierType.getSchemeURI ());
    if (identifierType.getSchemeVersionID () != null) schemeVersionID.setValue (identifierType.getSchemeVersionID ());
    if (identifierType.getSchemeName () != null) schemeName.setValue (identifierType.getSchemeName ());
    if (identifierType.getSchemeDataURI () != null) schemeDataURI.setValue (identifierType.getSchemeDataURI ());
    if (identifierType.getSchemeAgencyName () != null) schemeAgencyName.setValue (identifierType.getSchemeAgencyName ());
    if (identifierType.getSchemeAgencyID () != null) schemeAgencyID.setValue (identifierType.getSchemeAgencyID ());
    if (identifierType.getValue () != null) value.setValue (identifierType.getValue ());
  }

  public boolean equals(Object obj) {
    return super.equals (obj);
  }
}
