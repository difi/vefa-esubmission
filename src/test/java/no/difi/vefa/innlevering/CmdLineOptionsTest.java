/*
 *  Copyright (c) 2015,  Norwegian Agency for Public Management and eGovernment (Difi)
 *
 *  Author according to Norwegian Copyright act §3: Steinar Overbeck Cook
 *
 *  All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without modification,
 *  are permitted provided that the following conditions are met:
 *
 *  · Redistributions of source code must retain the above copyright notice, this
 *  list of conditions and the following disclaimer.
 *
 *  · Redistributions in binary form must reproduce the above copyright notice, this
 *  list of conditions and the following disclaimer in the documentation and/or
 *  other materials provided with the distribution.
 *
 *  · Neither the name of the <ORGANIZATION> nor the names of its contributors may
 *  be used to endorse or promote products derived from this software without
 *  specific prior written permission.
 *
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 *  ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 *  WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 *  ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 *  (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 *  LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 *  ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *  (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 *  SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package no.difi.vefa.innlevering;

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