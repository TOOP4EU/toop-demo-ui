/**
 * Copyright (C) 2018 toop.eu
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
package bean;


import static org.junit.Assert.assertEquals;

import java.time.LocalDate;

import org.junit.Test;

import eu.toop.demoui.bean.Identity;

public class IdentityTest {
  @Test
  public void testBasic () {

    final String firstName = "Maximillian";
    final String familyName = "Stern";
    final String birthPlace = "Test birth place";
    final LocalDate birthDate = LocalDate.of (2018, 05, 10);
    final String identifier = "SV/GF/12345";
    final String nationality = "SV";

    final Identity identity = new Identity ();
    identity.setFirstName (firstName);
    identity.setFamilyName (familyName);
    identity.setBirthPlace (birthPlace);
    identity.setBirthDate (birthDate);
    identity.setIdentifier (identifier);
    identity.setNationality (nationality);

    assertEquals (firstName, identity.getFirstName ());
    assertEquals (familyName, identity.getFamilyName ());
    assertEquals (birthPlace, identity.getBirthPlace ());
    assertEquals (birthDate, identity.getBirthDate ());
    assertEquals (identifier, identity.getIdentifier ());
    assertEquals (nationality, identity.getNationality ());
  }
}
