package eu.toop.demoui.view;

import com.vaadin.navigator.View;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import eu.toop.demoui.bean.Identity;
import eu.toop.demoui.form.IdentityForm;

public class eIDModuleView extends VerticalLayout implements View {

    public eIDModuleView() {
        addComponent(new Label("eID-Module Stub"));
        addComponent(new IdentityForm(new Identity(), event -> {
            getUI().getNavigator().navigateTo("");
        }));
    }
}
