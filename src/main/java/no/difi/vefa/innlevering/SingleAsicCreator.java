package no.difi.vefa.innlevering;

import java.io.File;
import java.util.List;

/**
 * @author steinar
 *         Date: 11.08.15
 *         Time: 14.03
 */
class SingleAsicCreator {

    private File bisFileName;
    private List<File> attachments;

    public void execute() {

    }

    public SingleAsicCreator bisFile(File bisFileName) {
        this.bisFileName = bisFileName;
        return this;
    }


    public SingleAsicCreator attachments(List<File> attachments) {
        this.attachments = attachments;
        return this;
    }

    public File create() {
        return null;
    }

    public File sign(File keyStoreFile, String keystorePassword, String privateKeyPassword) {
        return null;
    }
}
