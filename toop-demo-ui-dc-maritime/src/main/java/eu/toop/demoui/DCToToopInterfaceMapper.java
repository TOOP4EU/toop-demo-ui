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

import java.io.IOException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.GuardedBy;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.collection.impl.CommonsHashMap;
import com.helger.commons.collection.impl.ICommonsMap;
import com.helger.commons.concurrent.SimpleReadWriteLock;
import com.helger.commons.string.StringHelper;
import com.vaadin.ui.UI;

import eu.toop.commons.dataexchange.v140.TDETOOPRequestType;
import eu.toop.commons.error.ToopErrorException;
import eu.toop.iface.ToopInterfaceClient;

/**
 * This is an internal class that manages the state between the sending UI and
 * the async TOOP interface API.
 *
 * @author Philip Helger
 *
 */
public final class DCToToopInterfaceMapper {
  private static final SimpleReadWriteLock s_aRWLock = new SimpleReadWriteLock ();
  @GuardedBy ("s_aRWLock")
  private static final ICommonsMap<String, UI> MAP = new CommonsHashMap<> ();

  private DCToToopInterfaceMapper () {
  }

  public static void sendRequest (@Nonnull final TDETOOPRequestType aRequest,
                                  @Nonnull final UI aUI) throws IOException, ToopErrorException {
    ValueEnforcer.notNull (aRequest, "Request");
    ValueEnforcer.notNull (aUI, "UI");

    // Main sending
    ToopInterfaceClient.sendRequestToToopConnector (aRequest);

    // Remember only if no exception occurred
    s_aRWLock.writeLocked ( () -> MAP.put (aRequest.getDocumentUniversalUniqueIdentifier ().getValue (), aUI));
  }

  @Nullable
  public static UI getAndRemoveUI (@Nullable final String sRequestUUID) {
    if (StringHelper.hasNoText (sRequestUUID))
      return null;
    return s_aRWLock.writeLocked ( () -> MAP.remove (sRequestUUID));
  }
}
