package eu.toop.demoui.form;

import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
import com.vaadin.ui.*;
import eu.toop.demoui.bean.Identity;

public class IdentityForm extends FormLayout {
    public IdentityForm(Identity identity, Button.ClickListener onSubmit) {
        Binder<Identity> binder = new Binder<>();

        TextField firstNameField = new TextField("First name");
        binder.bind(firstNameField, Identity::getFirstName, Identity::setFirstName);
        addComponent(firstNameField);

        TextField familyNameField = new TextField("Family Name");
        binder.bind(familyNameField, Identity::getFamilyName, Identity::setFamilyName);
        addComponent(familyNameField);

        TextField birthPlaceField = new TextField("Birth place");
        binder.bind(birthPlaceField, Identity::getBirthPlace, Identity::setBirthPlace);
        addComponent(birthPlaceField);

        TextField identifierField = new TextField("Identifier");
        binder.bind(identifierField, Identity::getIdentifier, Identity::setIdentifier);
        addComponent(identifierField);

        DateField birthDateField = new DateField("Birth date");
        binder.bind(birthDateField, Identity::getBirthDate, Identity::setBirthDate);
        addComponent(birthDateField);

        TextField nationalityField = new TextField("Nationality");
        binder.bind(nationalityField, Identity::getNationality, Identity::setNationality);
        addComponent(nationalityField);

        binder.readBean(identity);

        Button submitButton = new Button("Use this identity", clickEvent -> {
            try {
                binder.writeBean(identity);
            } catch (ValidationException e) {
                Notification.show("Identity could not be saved, " +
                    "please check error messages for each field.");
            }
            onSubmit.buttonClick(clickEvent);
        });
        addComponent(submitButton);
    }
}
