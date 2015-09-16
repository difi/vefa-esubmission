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

import no.difi.asic.SignatureHelper;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 *
 *
 */
public class Main {
    public static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {

        CmdLineOptions options = parseCmdLineOptions(args);
        ExecutionOptions executionOptions = new ExecutionOptions(options);

        // Establishes the Signature helper with a keystore
        SignatureHelper signatureHelper = new SignatureHelper(executionOptions.getKeyStoreStream(), executionOptions.getKeyStorePassword(), null, executionOptions.getPrivateKeyPassword());

        try {

            switch (executionOptions.getExecutionMode()) {
                case MANUAL:
                    ManualAsicCreator manualAsicCreator = new ManualAsicCreator(executionOptions, signatureHelper);
                    manualAsicCreator.createAsicFile();
                    break;

                case SBDH:
                    SbdhAsicCreator sbdhAsicCreator = new SbdhAsicCreator(signatureHelper);
                    File asicFile = sbdhAsicCreator.createAsicFile(executionOptions);
                    break;

                case SCAN:
                    throw new IllegalArgumentException("No support for directory scanning in this version");
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
