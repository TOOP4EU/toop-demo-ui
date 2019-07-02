package eu.toop.demoui.certificate;

public class CertificateProcessContext {

    private CertificateProcessStrategy processStrategy;

    public CertificateProcessContext(CertificateProcessStrategy processStrategy) {
        this.processStrategy = processStrategy;
    }

    public void executeProcessStrategy() {
        processStrategy.processCertificate();
    }
}
