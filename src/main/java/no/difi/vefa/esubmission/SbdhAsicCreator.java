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

import no.difi.asic.AsicWriter;
import no.difi.asic.AsicWriterFactory;
import no.difi.asic.MimeType;
import no.difi.asic.SignatureHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.unece.cefact.namespaces.standardbusinessdocumentheader.ManifestItem;
import org.unece.cefact.namespaces.standardbusinessdocumentheader.StandardBusinessDocumentHeader;

import java.io.*;

/**
 * Creates an ASiC archive based upon the contents of an XML file containing a SBDH.
 *
 * @author steinar
 *         Date: 13.08.15
 *         Time: 14.40
 */
class SbdhAsicCreator {

    Logger log = LoggerFactory.getLogger(SbdhAsicCreator.class);

    public static final String STANDARD_NAME_FOR_SBDH_XML = "sbdh.xml";

    private final SignatureHelper signatureHelper;
    private final SbdhParser sbdhParser;

    public SbdhAsicCreator(SignatureHelper signatureHelper) {

        this.signatureHelper = signatureHelper;
        sbdhParser = new SbdhParser();
    }


    public File createAsicFile(ExecutionOptions executionOptions) {
        if (executionOptions == null) {
            throw new IllegalArgumentException("mising required argument, executionOptions");
        }

        if (executionOptions.getSbdhFile() == null) {
            throw new IllegalArgumentException("No SBDH file provided");
        }

        File sbdhFile;
        File asicFile = executionOptions.getArchiveFileName();

        // Determines whether to use the internal SBDH or not.
        if (executionOptions.isInternalTestSbdhBeingUsed()) {
            // Running in test mode, so retrieve all resources and make
            // them available in the file system in order to reference them
            // from the SBDH

            // SBDH first
            sbdhFile = Util.extractResourceToTempDir(Util.SAMPLE_SBDH_RESOURCE);
            // Secondly the sample UBL document
            Util.extractResourceToTempDir(Util.SAMPLE_UBL_DOCUMENT, sbdhFile.getParentFile());
            // Thirdly, the sample attachment
            Util.extractResourceToTempDir(Util.SAMPLE_ATTACHMENT, sbdhFile.getParentFile());

        } else {
            sbdhFile = executionOptions.getSbdhFile();
            if (!sbdhFile.exists() || !sbdhFile.canRead()) {
                throw new IllegalArgumentException(sbdhFile + " non existent or can not be read");
            }
        }

        createFrom(sbdhFile, asicFile);

        log.info("Created " + asicFile.getAbsolutePath());
        return asicFile;
    }


    public void createFrom(File sbdhFile, File asicFile) {

        AsicWriter asicWriter = createAsicWriter(asicFile);

        // Opens, reads and parses the XML SBDH into Java objects
        StandardBusinessDocumentHeader sbdh = fetchStandardBusinessDocumentHeader(sbdhFile);

        // Adds the SBDH itself, always having the same name
        addSbdhItselfWithStandardName(sbdhFile, asicWriter);

        // Adds the documents referenced by the SBDH
        addDocumentsReferencedInSbdh(sbdhFile, asicWriter, sbdh);

        // Signs the ASiC archive
        sign(sbdhFile, asicWriter);
    }

    AsicWriter createAsicWriter(File asicFile) {
        AsicWriter asicWriter;
        try {
            asicWriter = AsicWriterFactory.newFactory().newContainer(asicFile);
        } catch (IOException e) {
            throw new IllegalStateException("Unable to create new container in " + asicFile + " " + e.getMessage(), e);
        }
        return asicWriter;
    }

    StandardBusinessDocumentHeader fetchStandardBusinessDocumentHeader(File sbdhFile) {
        FileInputStream sbdhInputStream;
        try {
            sbdhInputStream = new FileInputStream(sbdhFile);
        } catch (FileNotFoundException e) {
            throw new IllegalStateException("Unable to open file " + sbdhFile + " " + e.getMessage(), e);
        }

        // Parses the SBDH
        return sbdhParser.parse(sbdhInputStream);
    }

    void addSbdhItselfWithStandardName(File sbdhFile, AsicWriter asicWriter) {
        try {
            asicWriter.add(sbdhFile.toPath(), STANDARD_NAME_FOR_SBDH_XML, MimeType.forString("application/xml"));
        } catch (IOException e) {
            throw new IllegalStateException("Unable to add " + sbdhFile + " with name "+ STANDARD_NAME_FOR_SBDH_XML +" to archive" + e,e);
        }
    }

    void addDocumentsReferencedInSbdh(File sbdhFile, AsicWriter asicWriter, StandardBusinessDocumentHeader sbdh) {
        if (sbdh.getManifest().getNumberOfItems().longValue() > Integer.MAX_VALUE) {
            throw new IllegalStateException("Too many Manifest items: " + sbdh.getManifest().getNumberOfItems().longValue() + " only " + Integer.MAX_VALUE + " allowed");
        }

        for (int itemCount = 0; itemCount < sbdh.getManifest().getNumberOfItems().intValue(); itemCount++) {

            ManifestItem manifestItem = sbdh.getManifest().getManifestItem().get(itemCount);

            String documentName = manifestItem.getUniformResourceIdentifier().replace("cid:", "");

            // The current working directory is propably not the same as the directory of the SBDH. Thus
            // we must use absolute paths.
            File documentFile = new File(sbdhFile.getParentFile().getAbsolutePath(), documentName);
            if (!documentFile.canRead()) {
                throw new IllegalStateException("Unable to read " + documentFile);
            }

            try {
                log.debug("Adding " + documentFile);
                asicWriter.add(documentFile, documentName, MimeType.forString(manifestItem.getMimeTypeQualifierCode()));
            } catch (IOException e) {
                throw new IllegalStateException("Unable to add " + documentFile + " to asic archive with entry name " + documentName + ". " + e.getMessage(), e);
            }

            if (itemCount == 0) {
                asicWriter.setRootEntryName(documentName);
            }
        }
    }

    void sign(File sbdhFile, AsicWriter asicWriter) {
        try {
            asicWriter.sign(signatureHelper);
        } catch (IOException e) {
            throw new IllegalStateException("Unable to sign contents of " + sbdhFile + ". " + e.getMessage(), e);
        }
    }
}
