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

import org.kohsuke.args4j.Option;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Holds the options for executing the program. Three stages:
 * <ol>
 *     <li>Reasonable defaults are set.</li>
 *     <li>Command line is parsed under control of the @Option annotations.</li>
 *     <li>The properties are inspected again and the effective set of options is set.</li>
 * </ol>
 *
 *
 * @author steinar
 *         Date: 11.08.15
 *         Time: 11.29
 */
public class CmdLineOptions {

    public static final Logger log = LoggerFactory.getLogger(CmdLineOptions.class);
    public static final String VEFA_INNLEVERING_ASICE = "vefa-innlevering.asice";
    public static final String TEST_FLAG = "test";

    public CmdLineOptions() {
    }

    /** Default name of output file */
    private File archiveFileName = new File(VEFA_INNLEVERING_ASICE);

    /** Holds attachments listed with -a option */
    private List<File> attachments = new ArrayList<File>();

    /**
     * The main XML BIS document to be inserted into the ASiC archive. The value "test" indicates to use whatever
     * XML document has been included in the distribution.
     */
    @Option(name = "-b", aliases = {"-bis"}, usage="Name of main XML BIS file",metaVar = "<file>",required = true, forbids={"-d"})
    File bisFileName = null;

    /**
     * The keystore holding the private and public key together with any certificates.
     * Value of "test" indicates the use of the supplied test keystore.
     */
    @Option(name = "-ks", aliases = {"-keystore"}, usage = "File holding the JKS keystore", required = true)
    File keyStoreFile = new File(TEST_FLAG);

    @Option(name = "-kp", usage = "KeyStore password.", metaVar = "<KeyStorePassword>")
    String keystorePassword;

    @Option(name="-pp",usage = "Private key password if different from keystore password.", metaVar = "<privateKeyPassoword>")
    private String privateKeyPassword;

    @Option(name="-d",aliases = {"-dir"},usage = "Directory to scan for XML files", metaVar = "<dirname>",forbids="-bis")
    private File outputDirectory;

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

    public File getOutputDirectory() {
        return outputDirectory;
    }

    public Boolean getHelp() {
        return help;
    }

    public File getKeyStoreFile() {
        return keyStoreFile;
    }


}

