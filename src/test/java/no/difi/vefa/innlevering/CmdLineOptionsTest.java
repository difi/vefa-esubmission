package no.difi.vefa.innlevering;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.ExampleMode;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;

import static org.testng.Assert.*;

/**
 * @author steinar
 *         Date: 11.08.15
 *         Time: 12.18
 */
public class CmdLineOptionsTest {

    @Test
    public void sampleCommandLine() throws Exception {

        CmdLineOptions options = new CmdLineOptions();
        CmdLineParser cmdLineParser = new CmdLineParser(options);

        String args[] = {
                "-o", "outputfile",
                "-bis", "bisdocument.xml",
                "-a", "file1",
                "-a", "file2",
                "-unknownoption"

        };

        try {
            cmdLineParser.parseArgument(args);
        } catch (CmdLineException e) {
            Usage.usage(System.err, cmdLineParser, e.getMessage());
            return;
        }
        fail("Should have failed");
    }

    @Test
    public void singleBisDoc() {
        CmdLineOptions options = new CmdLineOptions();
        CmdLineParser cmdLineParser = new CmdLineParser(options);
        String args[] = {
                "-bis", "trdm090.xml",
                "-ks","test",
                "-kp", "changeit",
                "-pp", "changeit"
        };

        try {
            cmdLineParser.parseArgument(args);
        } catch (CmdLineException e) {
            Usage.usage(System.err, cmdLineParser, e.getMessage());
            fail("Valid option set has been rejected!");
        }

        assertEquals(options.bisFileName, new File("trdm090.xml"));
        assertEquals(options.getArchiveFileName(), new File(CmdLineOptions.VEFA_INNLEVERING_ASICE));
    }
}