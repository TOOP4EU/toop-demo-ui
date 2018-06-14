package layouts;

import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
import com.vaadin.ui.TextField;
import eu.toop.demoui.bean.Identity;
import eu.toop.demoui.layouts.IdentityForm;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
    for (Iterator<Component> iterator = identityForm.iterator(); iterator.hasNext();) {
      Component comp = (Component) iterator.next();

      if (comp instanceof TextField) {
        TextField textField = (TextField)comp;
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
        DateField dateField = (DateField)comp;
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
