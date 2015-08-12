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

    public static final String TRDM090_SUBMIT_TENDER_SAMPLE_XML = "trdm090-submit-tender-sample.xml";

    private CmdLineOptions options;

    public ExecutionOptions(CmdLineOptions options) {

        this.options = options;
    }


    public InputStream getKeyStoreStream() {
        InputStream keyStoreInputStream;

        // Special handling of keystore, passwords etc. if we are using the "test" keystore
        if (isTestKeystoreBeingUsed()) {

            keyStoreInputStream = Main.class.getClassLoader().getResourceAsStream("vefa-innlevering.jks");
            if (keyStoreInputStream == null) {
                throw new IllegalStateException("Unable to locate the built-in test keystore");
            }
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
            return "changeit";
        } else
            return options.getKeystorePassword();
    }

    public String getPrivateKeyPassword() {
        if (isTestKeystoreBeingUsed()) {
            return "changeit";
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

    public boolean isInternalTestBisFileBeingUsed() {
        return  (TEST_FLAG.equals(getBisFileName().getName().toLowerCase()));
    }
}
