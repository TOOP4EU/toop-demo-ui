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
package eu.toop.demoui.fields;

import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;

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

  @Override
  public boolean equals(Object obj) {
    return super.equals (obj);
  }

  @Override
  public int hashCode() {
    return super.hashCode ();
  }
}
