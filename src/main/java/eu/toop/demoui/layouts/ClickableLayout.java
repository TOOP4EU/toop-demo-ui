package eu.toop.demoui.layouts;

import com.vaadin.event.LayoutEvents;
import com.vaadin.ui.VerticalLayout;

public class ClickableLayout extends VerticalLayout {
  public ClickableLayout (final LayoutEvents.LayoutClickListener clickListener) {

    setMargin (false);
    setStyleName ("clickableLayout");
    addLayoutClickListener (clickListener);
  }
}
