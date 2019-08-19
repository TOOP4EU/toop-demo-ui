package eu.toop.demoui.layouts;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.ui.Grid;
import com.vaadin.ui.GridLayout;

import eu.toop.demoui.bean.DocumentDataBean;

public class DocumentCertificateList extends GridLayout {

    private static final Logger logger = LoggerFactory.getLogger (DocumentCertificateList.class);


//    private final Binder<ToopDataBean> binder = new Binder<> ();
//    private ToopDataBean toopDataBean;

    public DocumentCertificateList (final List<DocumentDataBean> docResponseList) {
        GridLayout gridLayout = new GridLayout();
        gridLayout.setColumns(docResponseList.size());

        Grid<DocumentDataBean> grid = new Grid<>();
        grid.setItems(docResponseList);
        grid.addColumn(DocumentDataBean::getDocumentName).setCaption("Document Name");
        grid.addColumn(DocumentDataBean::getDocumentDescription).setCaption("Document Description");
        grid.addColumn(DocumentDataBean::getDocumentIdentifier).setCaption("Document Identifier");
        grid.addColumn(DocumentDataBean::getDocumentIssuePlace).setCaption("Issue Place");
        grid.addColumn(DocumentDataBean::getDocumentIssueDate).setCaption("Issue Date");
        grid.addColumn(DocumentDataBean::getDocumentMIMEType).setCaption("MIME Type code");
        grid.addColumn(DocumentDataBean::getDocumentURI).setCaption("URI");

        gridLayout.addComponent(grid);

//        GridLayout grid = new GridLayout(1, 2);
//        grid.setColumns(2);
//        Label docNameLabel = new Label("Document Name",Label.CENTER);
//        grid.addLayoutComponent("Document Name", docNameLabel);
//
//        Label docIssuerLabel = new Label("Document Issuer",Label.CENTER);
//        grid.addLayoutComponent("Document Issuer", docIssuerLabel);
//        grid.setItems(toopDataBean.getKeyValList());
//        Grid<AbstractMap.SimpleEntry<String, String>> grid = new Grid<>();
//        grid.setWidth(100f, Sizeable.Unit.PERCENTAGE);
//        grid.setItems(toopDataBean.getKeyValList());
//


    }

//    public void setToopDataBean(final ToopDataBean toopDataBean) {
//
//        this.toopDataBean = toopDataBean;
//        binder.readBean (this.toopDataBean);
//    }
//
//    public void save () {
//
//        try {
//            binder.writeBean (toopDataBean);
//        } catch (final ValidationException e) {
//            logger.error ("Failed to write to 'toopDataBean' bean");
//        }
//    }

    @Override
    public boolean equals(Object obj) {
        return super.equals (obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode ();
    }
}
