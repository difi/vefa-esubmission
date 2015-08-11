package no.difi.vefa.innlevering;

import org.kohsuke.args4j.Option;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author steinar
 *         Date: 11.08.15
 *         Time: 11.29
 */
public class CmdLineOptions {

    public static final Logger log = LoggerFactory.getLogger(CmdLineOptions.class);
    public static final String VEFA_INNLEVERING_ASICE = "vefa-innlevering.asice";

    private File archiveFileName = new File(VEFA_INNLEVERING_ASICE);
    private List<File> attachments = new ArrayList<File>();

    @Option(name = "-b", aliases = {"-bis"}, usage="Name of main XML BIS file",metaVar = "<file>",required = true, forbids={"-d"})
    File bisFileName = null;

    @Option(name = "-ks", aliases = {"-keystore"}, usage = "File holding the JKS keystore")
    File keyStoreFile;

    @Option(name = "-kp", usage = "KeyStore password.", metaVar = "<KeyStorePassword>")
    String keystorePassword;

    @Option(name="-pp",usage = "Private key password if different from keystore password.", metaVar = "<privateKeyPassoword>")
    private String privateKeyPassword;

    @Option(name="-d",aliases = {"-dir"},usage = "Directory to scan for XML files", metaVar = "<dirname>",forbids="-bis")
    private File directory;

    @Option(name = "-h", aliases = {"-?", "-help"}, usage = "Print help.", help = true)
    Boolean help = false;

    @Option(name = "-o", aliases = {"-out"}, usage = "The name of the output file, defaults to " +VEFA_INNLEVERING_ASICE , metaVar = "<filename>")
    void setArchiveFileName(File fileName) {
        log.debug("Setting name of output file to " + fileName);
        archiveFileName = fileName;
    }

    @Option(name="-a",aliases = {"-attachment"}, usage="Name of attachment files. May be repeated.",metaVar = "<filename>")
    public void setAttachments(File attachment) {
        log.debug("Adding " + attachment + " to list of files to be attached");
        this.attachments.add(attachment);
    }


    public File getArchiveFileName() {
        return archiveFileName;
    }

    public List<File> getAttachments() {
        return attachments;
    }

    public File getBisFileName() {
        return bisFileName;
    }

    public String getKeystorePassword() {
        return keystorePassword;
    }


    public String getPrivateKeyPassword() {
        return privateKeyPassword;
    }


    public File getDirectory() {
        return directory;
    }

    public Boolean getHelp() {
        return help;
    }

    public File getKeyStoreFile() {
        return keyStoreFile;
    }
}

