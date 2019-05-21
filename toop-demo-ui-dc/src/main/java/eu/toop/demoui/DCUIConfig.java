/**
 * Copyright (C) 2018-2019 toop.eu
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
package eu.toop.demoui;

import java.util.List;
import java.util.ResourceBundle;

import com.helger.commons.string.StringHelper;

public class DCUIConfig {

  private static final ResourceBundle rb = ResourceBundle.getBundle("dcui");

  private DCUIConfig() {
  }

  public static String getEidModuleURL () {
    return rb.getString("toop.eidmodule.url");
  }

  public static String getConceptNamespace() {
    return rb.getString("toop.concept.namespace");
  }

  public static String getDestinationCountryCode () {
    return rb.getString ("destination.country.code");
  }

  public static String getSenderIdentifierScheme () {
    return rb.getString ("sender.identifier.scheme");
  }

  public static String getSenderIdentifierValue () {
    return rb.getString ("sender.identifier.value");
  }

  public static String getSenderCountryCode () {
    return rb.getString ("sender.country.code");
  }

  public static String getDumpRequestDirectory() {
    return rb.getString ("dump.request.directory");
  }

  public static String getDumpResponseDirectory() {
    return rb.getString ("dump.response.directory");
  }

  public static String getTrackerURL () {
    return rb.getString ("toop.tracker.url");
  }

  public static String getTrackerTopic () {
    return rb.getString ("toop.tracker.topic");
  }

  public static List<String> getCountryCodes () {
    return StringHelper.getExploded (',', rb.getString("country.codes"));
  }
}
