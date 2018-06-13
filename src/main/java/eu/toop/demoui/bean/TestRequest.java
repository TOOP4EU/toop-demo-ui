package eu.toop.demoui.bean;

import com.vaadin.ui.CustomField;
import oasis.names.specification.ubl.schema.xsd.unqualifieddatatypes_21.IdentifierType;

public class TestRequest {
  private CustomField<IdentifierType> identifierType;

  public CustomField<oasis.names.specification.ubl.schema.xsd.unqualifieddatatypes_21.IdentifierType> getIdentifierType () {

    return identifierType;
  }

  public void setIdentifierType (CustomField<oasis.names.specification.ubl.schema.xsd.unqualifieddatatypes_21.IdentifierType> identifierType) {

    this.identifierType = identifierType;
  }
}
