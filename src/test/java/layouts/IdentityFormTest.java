package layouts;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;

import org.junit.Test;

import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
import com.vaadin.ui.TextField;

import eu.toop.demoui.bean.Identity;
import eu.toop.demoui.layouts.IdentityForm;

public class IdentityFormTest {
  @Test
  public void testBasic () {

    final String firstName = "Maximillian";
    final String familyName = "Stern";
    final String birthPlace = "Test birth place";
    final LocalDate birthDate = LocalDate.of (2018, 05, 10);
    final String identifier = "SV/GF/12345";
    final String nationality = "SV";

    final String newFirstName = "Alice";
    final String newFamilyName = "Briansson";
    final String newBirthPlace = "Another birth place";
    final LocalDate newBirthDate = LocalDate.of (2017, 04, 11);
    final String newIdentifier = "SV/GF/67890";
    final String newNationality = "GF";

    final Identity identity = new Identity ();
    identity.setFirstName (firstName);
    identity.setFamilyName (familyName);
    identity.setBirthPlace (birthPlace);
    identity.setBirthDate (birthDate);
    identity.setIdentifier (identifier);
    identity.setNationality (nationality);

    final IdentityForm identityForm = new IdentityForm (identity, false);
    for (final Component comp : identityForm) {
      if (comp instanceof TextField) {
        final TextField textField = (TextField)comp;
        switch (textField.getCaption ()) {
          case "First name":
            textField.setValue (newFirstName);
            break;
          case "Family name":
            textField.setValue (newFamilyName);
            break;
          case "Birth place":
            textField.setValue (newBirthPlace);
            break;
          case "Identifier":
            textField.setValue (newIdentifier);
            break;
          case "Nationality":
            textField.setValue (newNationality);
            break;
        }
      }
      if (comp instanceof DateField) {
        final DateField dateField = (DateField)comp;
        switch (dateField.getCaption ()) {
          case "Birth date":
            dateField.setValue (newBirthDate);
            break;
        }
      }
    }
    identityForm.save();

    assertEquals (newFirstName, identity.getFirstName ());
    assertEquals (newFamilyName, identity.getFamilyName ());
    assertEquals (newBirthPlace, identity.getBirthPlace ());
    assertEquals (newBirthDate, identity.getBirthDate ());
    assertEquals (newIdentifier, identity.getIdentifier ());
    assertEquals (newNationality, identity.getNationality ());
  }
}
