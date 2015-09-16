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
    public static final String VEFA_INNLEVERING_ASICE = "vefa-esubmission.asice";
    public static final String TEST_FLAG = "test";

    public CmdLineOptions() {
    }

    /** Default name of output file */
    private File archiveFileName = new File(VEFA_INNLEVERING_ASICE);

    /** Holds attachments listed with -a option */
    private List<File> attachments = new ArrayList<File>();

    /**
     * Parse the supplied SBDH and create ASiC based upon the contents of the SBDH
     */
    @Option(name="-sbdh", aliases = {"-auto"}, usage="Creates ASiC based upon contents of SBDH file.", metaVar = "<file>", forbids = {"-bis","-ubl","-scan","-a"})
    File sbdhFile = null;

    /**
     * Create ASiC manually using supplied XML BIS document to be inserted into the ASiC archive. The value "test" indicates to use whatever
     * XML document has been included in the distribution.
     */
    @Option(name = "-bis", aliases = {"-ubl"}, usage="Name of main XML BIS file",metaVar = "<file>",forbids={"-scan"})
    File bisFileName = null;

    /**
     * Directory to be scanned for SBDH files
     */
    @Option(name="-scan", aliases = {"-dir"},usage = "Directory to scan for XML files", metaVar = "<dirname>",forbids={"-bis","-ubl","-sbdh","-a"})
    private File scanDirectory;

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


    @Option(name = "-h", aliases = {"-?", "-help"}, usage = "Print help.", help = true)
    Boolean help = false;

    @Option(name = "-o", aliases = {"-out"}, usage = "The name of the output file, defaults to " +VEFA_INNLEVERING_ASICE , metaVar = "<filename>")
    void setArchiveFileName(File fileName) {
        log.debug("Setting name of output file to " + fileName);
        archiveFileName = fileName;
    }

    @Option(name="-a", usage="Name of attachment files. May be repeated.",metaVar = "<filename>")
    public void setAttachments(File attachment) {
        log.debug("Adding " + attachment + " to list of files to be attached");
        this.attachments.add(attachment);
    }

    public File getSbdhFile() {
        return sbdhFile;
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

    public File getScanDirectory() {
        return scanDirectory;
    }

    public Boolean getHelp() {
        return help;
    }

    public File getKeyStoreFile() {
        return keyStoreFile;
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CmdLineOptions{");
        sb.append("archiveFileName=").append(archiveFileName);
        sb.append(", attachments=").append(attachments);
        sb.append(", sbdhFile=").append(sbdhFile);
        sb.append(", bisFileName=").append(bisFileName);
        sb.append(", scanDirectory=").append(scanDirectory);
        sb.append(", keyStoreFile=").append(keyStoreFile);
        sb.append(", keystorePassword='").append(keystorePassword).append('\'');
        sb.append(", privateKeyPassword='").append(privateKeyPassword).append('\'');
        sb.append(", help=").append(help);
        sb.append('}');
        return sb.toString();
    }
}

