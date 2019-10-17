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
package eu.toop.demoui.view;

import com.vaadin.navigator.View;
import com.vaadin.ui.AbstractLayout;
import com.vaadin.ui.CustomLayout;

import eu.toop.demoui.bean.DocumentDataBean;

public class BaseView extends CustomLayout implements View {

  private AbstractLayout page;
  private DocumentDataBean documentDataBean = new DocumentDataBean();


  public BaseView () {

    super ("BaseView");
    setHeight ("100%");
  }

  public void setCurrentPage (AbstractLayout page) {

    if (this.page != null) {
      replaceComponent (this.page, page);
    } else {
      addComponent (page, "page");
    }
    this.page = page;
  }

  public AbstractLayout getCurrentPage () {

    return page;
  }

  public DocumentDataBean getDocumentDataBean() {
    return documentDataBean;
  }

  public void setDocumentDataBean(DocumentDataBean documentDataBean) {
    this.documentDataBean = documentDataBean;
  }


  @Override
  public boolean equals(Object obj) {
    return super.equals (obj);
  }

  @Override
  public int hashCode() {
    return super.hashCode ();
  }
}
