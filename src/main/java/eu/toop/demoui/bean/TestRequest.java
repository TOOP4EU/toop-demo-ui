package eu.toop.demoui.bean;

import java.util.List;

import com.vaadin.ui.CustomField;
import eu.toop.commons.concept.ConceptValue;
import oasis.names.specification.ubl.schema.xsd.unqualifieddatatypes_21.IdentifierType;

public class TestRequest {
  private CustomField<IdentifierType> IdentifierType;

  public CustomField<oasis.names.specification.ubl.schema.xsd.unqualifieddatatypes_21.IdentifierType> getIdentifierType () {

    return IdentifierType;
  }

  public void setIdentifierType (CustomField<oasis.names.specification.ubl.schema.xsd.unqualifieddatatypes_21.IdentifierType> identifierType) {

    IdentifierType = identifierType;
  }
}
