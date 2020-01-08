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
package eu.toop.demoui.view;

import com.vaadin.navigator.ViewChangeListener;

import eu.toop.demoui.layouts.MaritimePage;

public class Maritime extends BaseView {
    public Maritime () {

    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        setCurrentPage (new MaritimePage(this));

        System.out.println("Entered MaritimePage");
    }

    @Override
    public boolean equals(Object obj) { return super.equals(obj); }

    @Override
    public int hashCode() { return super.hashCode(); }
}
