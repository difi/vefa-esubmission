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

package no.difi.vefa.innlevering;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import static no.difi.vefa.innlevering.CmdLineOptions.TEST_FLAG;

/**
 * Created by soc on 12.08.2015.
 */
public class ExecutionOptions {



    public enum ExecMode { SBDH, MANUAL, SCAN;}
    public static final String TRDM090_SUBMIT_TENDER_SAMPLE_XML = "trdm090-submit-tender-sample.xml";

    private CmdLineOptions options;

    public ExecutionOptions(CmdLineOptions options) {

        this.options = options;
    }

    public InputStream getKeyStoreStream() {
        InputStream keyStoreInputStream;

        // Special handling of keystore, passwords etc. if we are using the "test" keystore
        if (isTestKeystoreBeingUsed()) {

            keyStoreInputStream = KeyStoreUtil.sampleKeyStoreStream();
        } else {
            try {
                keyStoreInputStream = new FileInputStream(options.getKeyStoreFile());
            } catch (FileNotFoundException e) {
                throw new IllegalStateException("Unable to open keystore " + options.getKeyStoreFile() + ". " + e.getMessage(), e);
            }

        }
        return keyStoreInputStream;
    }


    private boolean isTestKeystoreBeingUsed() {
        return TEST_FLAG.equals(options.getKeyStoreFile().getName().toLowerCase());
    }

    public String getKeyStorePassword() {
        if (isTestKeystoreBeingUsed()) {
            return KeyStoreUtil.getKeyStorePassword();
        } else
            return options.getKeystorePassword();
    }

    public String getPrivateKeyPassword() {
        if (isTestKeystoreBeingUsed()) {
            return KeyStoreUtil.getPrivateKeyPassord();
        } else
            return options.getPrivateKeyPassword();
    }

    public File getArchiveFileName() {
        return options.getArchiveFileName();
    }

    public List<File> getAttachments() {
        return options.getAttachments();
    }

    public File getBisFileName() {
        return options.getBisFileName();
    }

    /**
     * Provides an input stream to the main BIS document
     */
    public InputStream getBisFileInputStream() {
        InputStream bisFileAsInputStream = null;

        if (TEST_FLAG.equals(options.getBisFileName().getName().toLowerCase())) {
            bisFileAsInputStream = Main.class.getClassLoader().getResourceAsStream(TRDM090_SUBMIT_TENDER_SAMPLE_XML);
            if (bisFileAsInputStream == null) {
                throw new IllegalStateException("Internal sample file " + TRDM090_SUBMIT_TENDER_SAMPLE_XML + " not found in classpath");
            }
        } else {
            try {
                bisFileAsInputStream = new FileInputStream(options.getBisFileName());
            } catch (FileNotFoundException e) {
                throw new IllegalStateException("File " + options.getBisFileName() + " not found.", e);
            }
        }

        return bisFileAsInputStream;
    }

    public String getBisFileEntryName() {
        if ("test".equals(options.getBisFileName().getName().toLowerCase())) {
            return TRDM090_SUBMIT_TENDER_SAMPLE_XML;
        } else
            return options.getBisFileName().getName();
    }

    /** Indicates if the user has specified some form of the string "test" as the BIS file */
    public boolean isInternalTestBisFileBeingUsed() {
        return  (TEST_FLAG.equals(getBisFileName().getName().toLowerCase()));
    }

    public File getSbdhFile() {
        return options.getSbdhFile();
    }

    /** Indicates whether the user specified <code>-sbdh test</code> on the command line */
    public boolean isInternalTestSbdhBeingUsed() {
        return (TEST_FLAG.equals(getSbdhFile().getName().toLowerCase()));
    }


    public ExecMode getExecutionMode() {
        if (options.getSbdhFile() != null) {
            return ExecMode.SBDH;
        } else
        if (options.getScanDirectory() != null) {
            return ExecMode.SCAN;
        } else if (options.getBisFileName() != null) {
            return ExecMode.MANUAL;
        }
        throw new IllegalStateException("Unable to determine mode of operations: " + options);
    }
}
