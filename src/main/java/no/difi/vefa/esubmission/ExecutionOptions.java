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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import static no.difi.vefa.esubmission.CmdLineOptions.TEST_FLAG;

/**
 * Holds the effective execution options.
 *
 * Created by soc on 12.08.2015.
 */
public class ExecutionOptions {



    public enum ExecMode { SBDH, MANUAL, SCAN, WRAP, UNWRAP;}

    public static final String TRDM090_SUBMIT_TENDER_SAMPLE_XML = "trdm090-submit-tender-sample.xml";

    private CmdLineOptions cmdLineOptions;

    public ExecutionOptions(CmdLineOptions cmdLineOptions) {

        this.cmdLineOptions = cmdLineOptions;
    }

    public InputStream getKeyStoreStream() {
        InputStream keyStoreInputStream;

        // Special handling of keystore, passwords etc. if we are using the "test" keystore
        if (isTestKeystoreBeingUsed()) {

            keyStoreInputStream = KeyStoreUtil.sampleKeyStoreStream();
        } else {
            try {
                keyStoreInputStream = new FileInputStream(cmdLineOptions.getKeyStoreFile());
            } catch (FileNotFoundException e) {
                throw new IllegalStateException("Unable to open keystore " + cmdLineOptions.getKeyStoreFile() + ". " + e.getMessage(), e);
            }

        }
        return keyStoreInputStream;
    }


    private boolean isTestKeystoreBeingUsed() {
        return TEST_FLAG.equals(cmdLineOptions.getKeyStoreFile().getName().toLowerCase());
    }

    public String getKeyStorePassword() {
        if (isTestKeystoreBeingUsed()) {
            return KeyStoreUtil.getKeyStorePassword();
        } else
            return cmdLineOptions.getKeystorePassword();
    }

    public String getPrivateKeyPassword() {
        if (isTestKeystoreBeingUsed()) {
            return KeyStoreUtil.getPrivateKeyPassord();
        } else
            return cmdLineOptions.getPrivateKeyPassword();
    }

    public File getOutputFile() {
        return cmdLineOptions.getOutputFile();
    }

    public List<File> getAttachments() {
        return cmdLineOptions.getAttachments();
    }

    public File getBisFileName() {
        return cmdLineOptions.getBisFileName();
    }

    /**
     * Provides an input stream to the main BIS document
     */
    public InputStream getBisFileInputStream() {
        InputStream bisFileAsInputStream = null;

        if (TEST_FLAG.equals(cmdLineOptions.getBisFileName().getName().toLowerCase())) {
            bisFileAsInputStream = Main.class.getClassLoader().getResourceAsStream(TRDM090_SUBMIT_TENDER_SAMPLE_XML);
            if (bisFileAsInputStream == null) {
                throw new IllegalStateException("Internal sample file " + TRDM090_SUBMIT_TENDER_SAMPLE_XML + " not found in classpath");
            }
        } else {
            try {
                bisFileAsInputStream = new FileInputStream(cmdLineOptions.getBisFileName());
            } catch (FileNotFoundException e) {
                throw new IllegalStateException("File " + cmdLineOptions.getBisFileName() + " not found.", e);
            }
        }

        return bisFileAsInputStream;
    }

    public String getBisFileEntryName() {
        if ("test".equals(cmdLineOptions.getBisFileName().getName().toLowerCase())) {
            return TRDM090_SUBMIT_TENDER_SAMPLE_XML;
        } else
            return cmdLineOptions.getBisFileName().getName();
    }

    /** Indicates if the user has specified some form of the string "test" as the BIS file */
    public boolean isInternalTestBisFileBeingUsed() {
        return  (TEST_FLAG.equals(getBisFileName().getName().toLowerCase()));
    }

    public File getSbdhFile() {
        return cmdLineOptions.getSbdhFile();
    }

    /** Indicates whether the user specified <code>-sbdh test</code> on the command line */
    public boolean isInternalTestSbdhBeingUsed() {
        return (TEST_FLAG.equals(getSbdhFile().getName().toLowerCase()));
    }

    /** Provides the name of the file holding the base64 encoded payload to be unwrapped into an ASiC archive */
    public File sbdFileToUnwrap() {
        return cmdLineOptions.getUnwrapFile();
    }

    /** Provides the name of the file holding the ASiC archive to be wrapped as base64 encoded payload in an SBD */
    public File asicFileToWrap() {
        return cmdLineOptions.asicFileToWrap();
    }

    public ExecMode getExecutionMode() {

        // Only -sbdh <filename> provided, no -bis specified
        if (cmdLineOptions.getSbdhFile() != null && cmdLineOptions.getBisFileName() == null) {
            return ExecMode.SBDH;
        }
        // -sbdh <filename> and -bis <filename> specified
        else if (cmdLineOptions.getSbdhFile() != null && cmdLineOptions.getBisFileName() != null) {
                return ExecMode.MANUAL;
        }
        // -scan
        else if (cmdLineOptions.getScanDirectory() != null) {
            return ExecMode.SCAN;
        }
        // -wrap
        else if (cmdLineOptions.asicFileToWrap() != null) {
            return ExecMode.WRAP;
        }
        // -unwrap
        else if (cmdLineOptions.getUnwrapFile() != null) {
            return ExecMode.UNWRAP;
        }
        throw new IllegalStateException("Unable to determine mode of operations: " + cmdLineOptions);
    }
}
