package eu.toop.demoui.pages;

import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import eu.toop.demoui.components.*;
import eu.toop.demoui.view.StartView;

public class Base extends VerticalLayout {

  private StartView _view;
  private Header _header;
  private Banner _banner;
  private Body _body;

  public Base (StartView view) {
    setSizeFull();
    setMargin (false);
    setView(view);
    setHeader(new Header ());
    setBanner (new Banner ());
  }

  public void setView(StartView view) {
    _view = view;
  }

  public StartView getView() {
    return _view;
  }

  public void setHeader (Header header) {
    replaceComponent (_header, header);
    _header = header;
  }

  public void setBanner (Banner banner) {
    replaceComponent (_banner, banner);
    _banner = banner;
  }

  public void setBody(Body body) {
    replaceComponent (_body, body);
    _body = body;
    setExpandRatio(body, 1F);
  }
}
