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

import java.io.IOException;
import java.util.Comparator;
import java.util.Map;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.helger.commons.mime.CMimeType;
import com.helger.commons.string.StringHelper;

import eu.toop.demoui.datasets.DPDataset;
import eu.toop.demoui.datasets.DPUIDatasets;

/**
 * Servlet for listing all data sets
 *
 * @author Philip Helger
 */
@WebServlet ("/list-datasets")
public class ListDataSetsServlet extends HttpServlet
{
  @Override
  protected void doGet (final HttpServletRequest req, final HttpServletResponse resp) throws IOException
  {
    final StringBuilder aSB = new StringBuilder ();
    aSB.append ("<html><head><title>DataSet</title></head><body><h1>Known Datasets</h1>");
    aSB.append ("<div>A total of ")
       .append (DPUIDatasets.INSTANCE.getDatasets ().size ())
       .append (" dataset(s) is/are contained.</div>");
    aSB.append ("<ul>");
    for (final DPDataset aDS : DPUIDatasets.INSTANCE.getDatasets ())
    {
      aSB.append ("<li>");
      if (StringHelper.hasText (aDS.getNaturalPersonIdentifier ()))
        aSB.append ("<div>Natural Person ID: <code>")
           .append (aDS.getNaturalPersonIdentifier ())
           .append ("</code></div>");
      if (StringHelper.hasText (aDS.getLegalPersonIdentifier ()))
        aSB.append ("<div>Legal Person ID: <code>").append (aDS.getLegalPersonIdentifier ()).append ("</code></div>");
      if (!aDS.getConcepts ().isEmpty ())
      {
        aSB.append ("Concepts: <ul>");
        for (final Map.Entry <String, String> aEntry : aDS.getConcepts ()
                                                          .getSortedByKey (Comparator.naturalOrder ())
                                                          .entrySet ())
          aSB.append ("<li>").append (aEntry.getKey ()).append ('=').append (aEntry.getValue ()).append ("</li>");
        aSB.append ("</ul>");
      }
      aSB.append ("</li>");
    }
    aSB.append ("</ul></body></html>");

    resp.setContentType (CMimeType.TEXT_HTML.getAsString ());
    resp.getWriter ().println (aSB.toString ());
  }
}
