/**
 * Copyright (C) 2018 toop.eu
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.toop.demoui.layouts;

import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
import com.vaadin.ui.Button;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;

import eu.toop.demoui.bean.Identity;

public class IdentityForm extends FormLayout {

  private Binder<Identity> binder = new Binder<> ();

  private Identity identity;

  public IdentityForm (final Identity identity, final boolean readOnly, final Button.ClickListener onSubmit) {

    final TextField firstNameField = new TextField ("First name");
    final TextField familyNameField = new TextField ("Family Name");
    final TextField birthPlaceField = new TextField ("Birth place");
    final TextField identifierField = new TextField ("Identifier");
    final DateField birthDateField = new DateField ("Birth date");
    final TextField nationalityField = new TextField ("Nationality");

    binder.bind (firstNameField, Identity::getFirstName, Identity::setFirstName);
    binder.bind (familyNameField, Identity::getFamilyName, Identity::setFamilyName);
    binder.bind (birthPlaceField, Identity::getBirthPlace, Identity::setBirthPlace);
    binder.bind (identifierField, Identity::getIdentifier, Identity::setIdentifier);
    binder.bind (birthDateField, Identity::getBirthDate, Identity::setBirthDate);
    binder.bind (nationalityField, Identity::getNationality, Identity::setNationality);

    firstNameField.setReadOnly (readOnly);
    familyNameField.setReadOnly (readOnly);
    birthPlaceField.setReadOnly (readOnly);
    identifierField.setReadOnly (readOnly);
    birthDateField.setReadOnly (readOnly);
    nationalityField.setReadOnly (readOnly);

    addComponent (firstNameField);
    addComponent (familyNameField);
    addComponent (birthPlaceField);
    addComponent (identifierField);
    addComponent (birthDateField);
    addComponent (nationalityField);

    setIdentityBean (identity);
  }

  public void setIdentityBean (final Identity _identity) {

    identity = _identity;
    binder.readBean (identity);
  }

  public void save () {

    try {
      binder.writeBean (identity);
    } catch (ValidationException e) {
      e.printStackTrace ();
    }
  }
}