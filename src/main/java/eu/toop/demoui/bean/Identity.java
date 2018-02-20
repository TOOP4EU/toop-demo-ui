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
package eu.toop.demoui.bean;

import java.time.LocalDate;

public class Identity implements java.io.Serializable {
  private String firstName;
  private String familyName;
  private String birthPlace;
  private LocalDate birthDate;
  private String identifier;
  private String nationality;

  public Identity() {
    // Preset mockup data
    firstName = "John";
    familyName = "Doe";
    birthPlace = "Fridili";
    birthDate = LocalDate.parse("1986-02-01");
    identifier = "EL/EL/12345";
    nationality = "EL";
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(final String firstName) {
    this.firstName = firstName;
  }

  public String getFamilyName() {
    return familyName;
  }

  public void setFamilyName(final String familyName) {
    this.familyName = familyName;
  }

  public String getBirthPlace() {
    return birthPlace;
  }

  public void setBirthPlace(final String birthPlace) {
    this.birthPlace = birthPlace;
  }

  public LocalDate getBirthDate() {
    return birthDate;
  }

  public void setBirthDate(final LocalDate birthDate) {
    this.birthDate = birthDate;
  }

  public String getIdentifier() {
    return identifier;
  }

  public void setIdentifier(final String identifier) {
    this.identifier = identifier;
  }

  public String getNationality() {
    return nationality;
  }

  public void setNationality(final String nationality) {
    this.nationality = nationality;
  }
}
