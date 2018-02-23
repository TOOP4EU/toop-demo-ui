package eu.toop.demoui.view;

import com.vaadin.navigator.View;
import com.vaadin.ui.*;
import eu.toop.demoui.pages.Start;

public class StartView extends VerticalLayout implements View {

  private AbstractLayout _page = null;

  public StartView() {
    setSizeFull();
    setMargin(false);

    setCurrentPage (new Start (this));
  }

  public void setCurrentPage(AbstractLayout page) {
    replaceComponent (_page, page);
    _page = page;
  }
}
