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
package eu.toop.demoui;

import java.util.ResourceBundle;

import com.helger.commons.string.StringParser;

public class DPUIConfig
{
  private static final ResourceBundle rb = ResourceBundle.getBundle ("dcui");

  private DPUIConfig ()
  {}

  public static String getResponderIdentifierScheme ()
  {
    return rb.getString ("responder.identifier.scheme");
  }

  public static String getResponderIdentifierValue ()
  {
    return rb.getString ("responder.identifier.value");
  }

  public static String getProviderCountryCode ()
  {
    return rb.getString ("provider.country.code");
  }

  public static String getDumpRequestDirectory ()
  {
    return rb.getString ("dump.request.directory");
  }

  public static String getDumpResponseDirectory ()
  {
    return rb.getString ("dump.response.directory");
  }

  public static boolean isTrackerEnabled ()
  {
    return StringParser.parseBool (rb.getString ("toop.tracker.enabled"), true);
  }

  public static String getTrackerURL ()
  {
    return rb.getString ("toop.tracker.url");
  }

  public static String getTrackerTopic ()
  {
    return rb.getString ("toop.tracker.topic");
  }
}
