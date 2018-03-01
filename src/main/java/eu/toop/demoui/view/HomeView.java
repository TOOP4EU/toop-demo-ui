package eu.toop.demoui.view;

import com.vaadin.navigator.View;
import com.vaadin.ui.*;
import eu.toop.demoui.pages.HomePage;

public class HomeView extends CustomLayout implements View {

  private CustomLayout _page;

  public HomeView () {
    super("HomeView");
    setHeight ("100%");

    setCurrentPage (new HomePage (this));
  }

  public void setCurrentPage(CustomLayout page) {
    if (_page != null) {
      replaceComponent (_page, page);
    } else {
      addComponent (page, "page");
    }
    _page = page;
  }
}
