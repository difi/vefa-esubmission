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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author steinar
 *         Date: 11.08.15
 *         Time: 14.03
 */
class ManualAsicCreator {

    private static final Logger log = LoggerFactory.getLogger(ManualAsicCreator.class);

    private final ExecutionOptions executionOptions;
    private final SignatureHelper signatureHelper;

    public ManualAsicCreator(ExecutionOptions executionOptions, SignatureHelper signatureHelper) {

        this.executionOptions = executionOptions;
        this.signatureHelper = signatureHelper;
    }


    public void createAsicFile() {

        try {
            AsicWriter asicWriter = AsicWriterFactory.newFactory().newContainer(executionOptions.getArchiveFileName());

            // A BIS file name of "test" indicates to use the internal test XML document.
            if (executionOptions.isInternalTestBisFileBeingUsed()) {
                InputStream bisFileVerified = executionOptions.getBisFileInputStream();
                String entryName = executionOptions.getBisFileEntryName();

                asicWriter.add(bisFileVerified, entryName, MimeType.forString("application/xml")).setRootEntryName(entryName);
            } else {
                // Uses whatever was supplied on the command line.
                asicWriter.add(executionOptions.getBisFileName());
            }

            for (File attachmentfile : executionOptions.getAttachments()) {
                asicWriter.add(attachmentfile);
            }
            asicWriter.sign(signatureHelper);

        } catch (IOException e) {
            throw new IllegalStateException("Unable to create ASiC archive in " + executionOptions.getArchiveFileName() + ". " + e.getMessage(), e);
        }

        log.info("ASiC archive created in:" + executionOptions.getArchiveFileName());

    }
}
