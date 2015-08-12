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

import no.difi.asic.AsicWriter;
import no.difi.asic.AsicWriterFactory;
import no.difi.asic.MimeType;
import no.difi.asic.SignatureHelper;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 *
 *
 */
public class Main {
    public static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {

        CmdLineOptions options = parseCmdLineOptions(args);
        ExecutionOptions executionOptions = new ExecutionOptions(options);

        SignatureHelper signatureHelper = new SignatureHelper(executionOptions.getKeyStoreStream(), executionOptions.getKeyStorePassword(), null, executionOptions.getPrivateKeyPassword());

        try {

            // Creating a single ASiC archive or multiple?
            if (options.getBisFileName() != null && options.getOutputDirectory() == null) {
                SingleAsicCreator singleAsicCreator = new SingleAsicCreator(executionOptions, signatureHelper);
                singleAsicCreator.createAsicFile();
            } else {
                if (options.getBisFileName() == null) {
                    throw new IllegalArgumentException("No support for directory scanning in this version");
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private static CmdLineOptions parseCmdLineOptions(String[] args) {
        CmdLineOptions options = new CmdLineOptions();
        CmdLineParser cmdLineParser = new CmdLineParser(options);
        try {
            cmdLineParser.parseArgument(args);

            if (options.getHelp()) {
                Usage.usage(System.out, cmdLineParser, "");
                System.exit(4);
            }

        } catch (CmdLineException e) {
            log.error(e.getMessage());
            Usage.usage(System.err, cmdLineParser, e.getMessage());
            System.exit(4);
        }


        return options;
    }

}
