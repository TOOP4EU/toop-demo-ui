package eu.toop.demoui.layouts;

import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import eu.toop.demoui.bean.TestRequest;
import eu.toop.demoui.view.BaseView;

public class TestPage extends VerticalLayout {
  public TestPage (BaseView view) {

    setStyleName ("TestPage");

    TestRequest testRequest = new TestRequest ();

    addComponent (new TestRequestForm (testRequest, false, null));
  }
}
