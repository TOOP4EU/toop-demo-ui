package eu.toop.demoui.view;

import com.vaadin.ui.Label;
import eu.toop.commons.codelist.EPredefinedDocumentTypeIdentifier;
import eu.toop.demoui.bean.TestRequest;
import eu.toop.demoui.layouts.TestPage;
import eu.toop.demoui.layouts.TestRequestForm;

public class TestView extends BaseView {

  public TestView () {

    setCurrentPage (new TestPage ());

    TestRequest testRequest = new TestRequest ();
    TestRequestForm testRequestForm = new TestRequestForm (testRequest, false, null);
    addComponent (testRequestForm);

    for (EPredefinedDocumentTypeIdentifier e: EPredefinedDocumentTypeIdentifier.values()) {
      addComponent (new Label (e.getID ()));
    }
  }
}
