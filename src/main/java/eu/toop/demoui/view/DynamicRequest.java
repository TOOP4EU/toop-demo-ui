package eu.toop.demoui.view;

import com.vaadin.navigator.ViewChangeListener;
import eu.toop.demoui.layouts.DynamicRequestPage;


public class DynamicRequest extends BaseView {


    public DynamicRequest() {

    }

    @Override
    public void enter (ViewChangeListener.ViewChangeEvent event) {

        setCurrentPage (new DynamicRequestPage(this));

        System.out.println("Entered DynamicRequestPage");
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
