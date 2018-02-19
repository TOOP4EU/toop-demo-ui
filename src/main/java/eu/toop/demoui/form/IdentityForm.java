package eu.toop.demoui.form;

import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
import com.vaadin.ui.*;
import eu.toop.demoui.bean.Identity;

public class IdentityForm extends FormLayout {

    private Binder<Identity> binder = new Binder<>();

    private Identity identity;

    public IdentityForm(Identity identity, Button.ClickListener onSubmit) {

        TextField firstNameField = new TextField("First name");
        TextField familyNameField = new TextField("Family Name");
        TextField birthPlaceField = new TextField("Birth place");
        TextField identifierField = new TextField("Identifier");
        DateField birthDateField = new DateField("Birth date");
        TextField nationalityField = new TextField("Nationality");

        binder.bind(firstNameField, Identity::getFirstName, Identity::setFirstName);
        binder.bind(familyNameField, Identity::getFamilyName, Identity::setFamilyName);
        binder.bind(birthPlaceField, Identity::getBirthPlace, Identity::setBirthPlace);
        binder.bind(identifierField, Identity::getIdentifier, Identity::setIdentifier);
        binder.bind(birthDateField, Identity::getBirthDate, Identity::setBirthDate);
        binder.bind(nationalityField, Identity::getNationality, Identity::setNationality);

        addComponent(firstNameField);
        addComponent(familyNameField);
        addComponent(birthPlaceField);
        addComponent(identifierField);
        addComponent(birthDateField);
        addComponent(nationalityField);

        setIdentityBean(identity);

        /*Button submitButton = new Button("Use this identity", clickEvent -> {
            try {
                binder.writeBean(identity);
            } catch (ValidationException e) {
                Notification.show("Identity could not be saved, " +
                    "please check error messages for each field.");
            }

            firstNameField.setReadOnly(true);
            familyNameField.setReadOnly(true);
            birthPlaceField.setReadOnly(true);
            identifierField.setReadOnly(true);
            birthDateField.setReadOnly(true);
            nationalityField.setReadOnly(true);

            if (onSubmit != null) {
                onSubmit.buttonClick(clickEvent);
            }
        });
        addComponent(submitButton);*/
    }

    public void setIdentityBean(Identity _identity) {
        identity = _identity;
        binder.readBean(identity);
    }
}
