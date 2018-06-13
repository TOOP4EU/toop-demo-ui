package eu.toop.demoui.layouts;

import com.vaadin.ui.VerticalLayout;
import eu.toop.demoui.bean.TestRequest;

public class TestPage extends VerticalLayout {
  public TestPage () {

    setStyleName ("TestPage");

    TestRequest testRequest = new TestRequest ();

    addComponent (new TestRequestForm (testRequest, false, null));
  }
}
