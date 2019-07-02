package eu.toop.demoui.certificate;

import com.helger.commons.collection.impl.ICommonsList;
import eu.toop.commons.codelist.EPredefinedDocumentTypeIdentifier;
import eu.toop.commons.dataexchange.v140.TDETOOPRequestType;
import eu.toop.commons.dataexchange.v140.TDETOOPResponseType;
import eu.toop.commons.exchange.AsicWriteEntry;

import javax.annotation.Nonnull;

public class CertificateProcessStrategyBuilder {

    private String sLogPrefix;
    private TDETOOPRequestType aRequest;
    private TDETOOPResponseType aResponse;
    private ICommonsList<AsicWriteEntry> documentEntries;

    public CertificateProcessStrategyBuilder setLogPrefix(@Nonnull final String sLogPrefix) {
        this.sLogPrefix = sLogPrefix;
        return CertificateProcessStrategyBuilder.this;
    }

    public CertificateProcessStrategyBuilder setRequest(@Nonnull final TDETOOPRequestType aRequest) {
        this.aRequest = aRequest;
        return CertificateProcessStrategyBuilder.this;
    }

    public CertificateProcessStrategyBuilder setResponse(@Nonnull final TDETOOPResponseType aResponse) {
        this.aResponse = aResponse;
        return CertificateProcessStrategyBuilder.this;
    }

    public CertificateProcessStrategyBuilder setDocumentEntries(@Nonnull final ICommonsList<AsicWriteEntry> documentEntries) {
        this.documentEntries = documentEntries;
        return CertificateProcessStrategyBuilder.this;
    }

    public CertificateProcessStrategy build(EPredefinedDocumentTypeIdentifier documentTypeIdentifier) {

        switch (documentTypeIdentifier) {
            case URN_EU_TOOP_NS_DATAEXCHANGE_1P40_REQUEST_URN_EU_TOOP_REQUEST_CREWCERTIFICATE_LIST_1_40:
            case URN_EU_TOOP_NS_DATAEXCHANGE_1P40_REQUEST_URN_EU_TOOP_REQUEST_SHIPCERTIFICATE_LIST_1_40:
                return new MaritimeCertificateProcessStrategy(sLogPrefix, aResponse);

            default:
                return new NonMaritimeCertificateProcessStrategy();
        }

    }

}
