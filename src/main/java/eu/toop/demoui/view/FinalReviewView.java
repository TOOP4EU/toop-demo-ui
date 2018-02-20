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
package eu.toop.demoui.view;

import com.vaadin.navigator.View;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

public class FinalReviewView extends VerticalLayout implements View {
  public FinalReviewView() {
    addComponent(new Label("Register a new branch in Freedonia"));
    addComponent(new Label("Review your information before submitting!"));

    addComponent(new Label("{{Show new branch details here}}"));
    addComponent(new Label("{{Show main company details here}}"));

    Button nextButton2 = new Button("Register my new company", clickEvent -> {
      UI.getCurrent().getNavigator().navigateTo(SuccessView.class.getName());
    });
    addComponent(nextButton2);
  }
}
