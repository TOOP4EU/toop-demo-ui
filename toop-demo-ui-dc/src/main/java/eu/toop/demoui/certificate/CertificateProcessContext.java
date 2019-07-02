package eu.toop.demoui.certificate;

import eu.toop.commons.codelist.EPredefinedDocumentTypeIdentifier;

public class CertificateProcessContext {

    private CertificateProcessStrategy processStrategy;

    public CertificateProcessContext(CertificateProcessStrategy processStrategy) {
        this.processStrategy = processStrategy;
    }

    public void executeProcessStrategy() {
        processStrategy.processCertificate();
    }
}
