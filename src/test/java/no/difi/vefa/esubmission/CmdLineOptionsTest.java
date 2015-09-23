/*
 * Copyright (c) 2015, Norwegian Agency for Public Management and eGovernment (Difi)
 *
 * Author according to Norwegian Copyright act paragraph no.3: Steinar Overbeck Cook
 *
 * This file is part of vefa-esubmission.
 *
 * vefa-esubmission is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * vefa-esubmission is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with vefa-esubmission. See the files COPYING and COPYING.LESSER.
 * If not, see <http://www.gnu.org/licenses/>.
 *
 */

package no.difi.vefa.esubmission;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
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
    public void sbdhWithoutBis() {
        CmdLineOptions options = new CmdLineOptions();
        CmdLineParser cmdLineParser = new CmdLineParser(options);
        String args[] = {
                "-sbdh", "sbdh.xml",
                "-ks", "test",
                "-kp", "changeit",
                "-pp", "changeit"
        };

    }

    @Test
    public void singleBisDoc() {
        CmdLineOptions options = new CmdLineOptions();
        CmdLineParser cmdLineParser = new CmdLineParser(options);
        String args[] = {
                "-bis", "trdm090.xml",
                "-ks", "test",
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
        assertEquals(options.getOutputFile(), new File(CmdLineOptions.VEFA_INNLEVERING_ASICE));
    }

    @Test(expectedExceptions = CmdLineException.class)
    public void missingKeystoreParams() throws CmdLineException {
        CmdLineOptions options = new CmdLineOptions();
        CmdLineParser cmdLineParser = new CmdLineParser(options);
        String args[] = {
                "-sbdh", "sbdh.xml",
                "-kp", "changeit",
                "-pp", "changeit"
        };

        cmdLineParser.parseArgument(args);
        fail("Missing -ks option should fail");
    }


    @Test
    public void testToString() throws Exception {
        CmdLineOptions options = new CmdLineOptions();
        CmdLineParser cmdLineParser = new CmdLineParser(options);
        String args[] = {
                "-sbdh", "test-sbdh.xml",
                "-ks", "Test",
                "-out", "dirname2/sample-asic.asice"
        };

        cmdLineParser.parseArgument(args);

        System.out.println(options);
    }
}