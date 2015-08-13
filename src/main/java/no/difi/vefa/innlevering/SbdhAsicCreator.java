/*
 * Copyright (c) 2015, Norwegian Agency for Public Management and eGovernment (Difi)
 *
 * Author according to Norwegian Copyright act paragraph no. 3: Steinar Overbeck Cook
 *
 * This file is part of vefa-innlevering.
 *
 * vefa-innlevering is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * vefa-innlevering is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with vefa-innlevering. See the files COPYING and COPYING.LESSER.
 * If not, see <http://www.gnu.org/licenses/>.
 */

package no.difi.vefa.innlevering;

import no.difi.asic.AsicWriter;
import no.difi.asic.AsicWriterFactory;
import no.difi.asic.MimeType;
import no.difi.asic.SignatureHelper;
import org.unece.cefact.namespaces.standardbusinessdocumentheader.ManifestItem;
import org.unece.cefact.namespaces.standardbusinessdocumentheader.StandardBusinessDocumentHeader;

import java.io.*;

/**
 * @author steinar
 *         Date: 13.08.15
 *         Time: 14.40
 */
class SbdhAsicCreator {


    private final SignatureHelper signatureHelper;

    public SbdhAsicCreator(SignatureHelper signatureHelper) {

        this.signatureHelper = signatureHelper;
    }

    public void createFrom(File sbdhFile, File asicFile) {

        AsicWriter asicWriter = null;

        try {
            asicWriter = AsicWriterFactory.newFactory().newContainer(asicFile);
        } catch (IOException e) {
            throw new IllegalStateException("Unable to create new container in " + asicFile + " " + e.getMessage(), e);
        }

        SbdhParser sbdhParser = new SbdhParser();
        FileInputStream sbdhInputStream = null;
        try {
            sbdhInputStream = new FileInputStream(sbdhFile);
        } catch (FileNotFoundException e) {
            throw new IllegalStateException("Unable to open file " + sbdhFile + " " + e.getMessage(), e);
        }

        // Parses the SBDH
        StandardBusinessDocumentHeader sbdh = sbdhParser.parse(sbdhInputStream);

        // Adds the SBDH

        // Adds the main UBL XML document
        if (sbdh.getManifest().getNumberOfItems().longValue() > Integer.MAX_VALUE) {
            throw new IllegalStateException("Too many Manifest items: " + sbdh.getManifest().getNumberOfItems().longValue() + " only " + Integer.MAX_VALUE + " allowed");
        }

        for (int itemCount = 0; itemCount < sbdh.getManifest().getNumberOfItems().intValue(); itemCount++) {

            ManifestItem manifestItem = sbdh.getManifest().getManifestItem().get(itemCount);

            String documentName = manifestItem.getUniformResourceIdentifier().replace("cid:", "");
            File documentFile = new File(sbdhFile.getParentFile().getAbsolutePath(), documentName);

            try {
                asicWriter.add(documentFile, documentName, MimeType.forString(manifestItem.getMimeTypeQualifierCode()));
            } catch (IOException e) {
                throw new IllegalStateException("Unable to add " + documentFile + " to asic archive with entry name " + documentName + ". " + e.getMessage(), e);
            }

            if (itemCount == 0) {
                asicWriter.setRootEntryName(documentName);
            }
        }

        // Adds the attachments

        try {
            asicWriter.sign(signatureHelper);
        } catch (IOException e) {
            throw new IllegalStateException("Unable to sign contents of " + sbdhFile + ". " + e.getMessage(), e);
        }

    }
}
