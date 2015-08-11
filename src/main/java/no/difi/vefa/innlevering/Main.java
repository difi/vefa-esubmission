package no.difi.vefa.innlevering;

import no.difi.asic.AsicWriter;
import no.difi.asic.AsicWriterFactory;
import no.difi.asic.SignatureHelper;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Hello world!
 *
 */
public class Main
{
    public static final Logger log = LoggerFactory.getLogger(Main.class);
    public static final String TRDM090_SUBMIT_TENDER_SAMPLE_XML = "trdm090-submit-tender-sample.xml";

    public static void main( String[] args ) {

        CmdLineOptions options = new CmdLineOptions();
        CmdLineParser cmdLineParser = new CmdLineParser(options);
        try {
            cmdLineParser.parseArgument(args);

            if (options.getHelp()) {
                Usage.usage(System.out,cmdLineParser,"");
                System.exit(4);
            }

        } catch (CmdLineException e) {
            log.error(e.getMessage());
            Usage.usage(System.err, cmdLineParser, e.getMessage());
            System.exit(4);
        }


        SignatureHelper signatureHelper = configureSignatureHelper(options);

        // Creating a single ASiC archive or multiple?
        if (options.getBisFileName() != null) {

            File bisFileVerified = verifyBisFile(options.getBisFileName());
            try {
                AsicWriter asicWriter = AsicWriterFactory.newFactory().newContainer(options.getArchiveFileName());
                asicWriter.add(bisFileVerified).setRootEntryName(bisFileVerified.getName());

                for (File attachmentfile : options.getAttachments()) {
                    asicWriter.add(attachmentfile);
                }
                asicWriter.sign(signatureHelper);

            } catch (IOException e) {
                throw new IllegalStateException("Unable to create ASiC archive in " + options.getArchiveFileName() + ". " + e.getMessage(), e);
            }

            log.info("ASiC archive created in:" + options.getArchiveFileName());
        }
    }

    private static File verifyBisFile(File bisFileName) {
        File bisFile = null;
        if ("test".equals(bisFileName.getName().toLowerCase())) {
            URL internalSampleUrl = Main.class.getClassLoader().getResource(TRDM090_SUBMIT_TENDER_SAMPLE_XML);
            if (internalSampleUrl == null) {
                throw new IllegalStateException("Internal sample file " + TRDM090_SUBMIT_TENDER_SAMPLE_XML + " not found in classpath");
            }
            try {
                bisFile = new File(internalSampleUrl.toURI());
            } catch (URISyntaxException e) {
                throw new IllegalStateException("Unable to convert " + internalSampleUrl + " to File object." + e.getMessage(), e);
            }
        } else {
            bisFile = bisFileName;
        }

        if (!bisFile.canRead()) {
            throw new IllegalStateException("Unable to read file " + bisFile);
        }
        return bisFile;
    }


    private static SignatureHelper configureSignatureHelper(CmdLineOptions options)  {
        SignatureHelper signatureHelper = null;

        // Special handling of keystore, passwords etc.
        if ("test".equals(options.getKeyStoreFile().getName().toLowerCase())) {

            InputStream testKeyStoreStream = Main.class.getClassLoader().getResourceAsStream("vefa-innlevering.jks");
            if (testKeyStoreStream == null) {
                throw new IllegalStateException("Unable to locate the built-in test keystore");
            }
            signatureHelper = new SignatureHelper(testKeyStoreStream, "changeit", null,"changeit");
        } else {
            try {
                signatureHelper = new SignatureHelper(options.getKeyStoreFile(), options.getKeystorePassword(), options.getPrivateKeyPassword());
            } catch (IOException e) {
                throw new IllegalStateException("Unable to load keystore from " + options.getKeyStoreFile() + ". " + e.getMessage(), e);
            }
        }

        if (signatureHelper == null) {
            throw new IllegalStateException("Ooops, internal error. Unable to create SignatureHelper object");
        }
        return signatureHelper;
    }

}
