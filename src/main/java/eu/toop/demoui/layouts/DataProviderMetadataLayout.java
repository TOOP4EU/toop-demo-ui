package eu.toop.demoui.layouts;

import com.helger.pd.searchapi.v1.EntityType;
import com.helger.pd.searchapi.v1.IDType;
import com.helger.pd.searchapi.v1.MatchType;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.Label;

import java.util.List;

public class DataProviderMetadataLayout extends CustomLayout {

    final Label metadataLabel = new Label("", ContentMode.PREFORMATTED);

    public DataProviderMetadataLayout() {
        super("DataProviderMetadataLayout");
        setCaption("Metadata");
        setStyleName("dataProviderMetadataLayout");

        metadataLabel.setStyleName("metadataLabel");
        addComponent(metadataLabel, "metadataLabel");
    }

    public void setData(MatchType match) {
        if (match == null) {
            metadataLabel.setValue("");
            return;
        }

        final StringBuilder sbMetadata = new StringBuilder();

        IDType id = match.getParticipantID();
        sbMetadata.append("ParticipantID [scheme: " + id.getScheme() + ", value: " + id.getValue()).append("\n");
        sbMetadata.append("  Doc Types").append("\n");
        List<IDType> docTypes = match.getDocTypeID();
        for (IDType docType : docTypes) {
            sbMetadata.append("    DocType [scheme: " + docType.getScheme() + "]: " + docType.getValue()).append("\n");
        }
        sbMetadata.append("  Entities").append("\n");
        List<EntityType> entities = match.getEntity();
        for (EntityType entity : entities) {
            sbMetadata.append("    Entity: ").append("\n");
            sbMetadata.append("      Name(s): ").append("\n");
            entity.getName().forEach(nameType -> {
                sbMetadata.append("        " + nameType.getValue() + (nameType.getLanguage() != null ? " lang: [" + nameType.getLanguage() + "]" : "")).append("\n");
            });

            if (entity.getRegDate() != null)
                sbMetadata.append("      Registration Date: " + entity.getRegDate()).append("\n");

            sbMetadata.append("      Country: " + entity.getCountryCode()).append("\n");

            if (entity.getAdditionalInfo() != null)
                sbMetadata.append("      Add. Info: " + entity.getAdditionalInfo()).append("\n");
            if (entity.getGeoInfo() != null)
                sbMetadata.append("      Geo Info: " + entity.getGeoInfo()).append("\n");

            sbMetadata.append("      Identifiers:").append("\n");
            List<IDType> identifiers = entity.getIdentifier();
            for (IDType identifer : identifiers) {
                sbMetadata.append("        Identifier [scheme: " + identifer.getScheme() + "] " + identifer.getValue()).append("\n");
            }
        }

        metadataLabel.setValue(sbMetadata.toString());
    }
}
