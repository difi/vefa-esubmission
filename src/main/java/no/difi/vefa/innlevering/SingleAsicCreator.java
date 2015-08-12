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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @author steinar
 *         Date: 11.08.15
 *         Time: 14.03
 */
class SingleAsicCreator {

    private static final Logger log = LoggerFactory.getLogger(SingleAsicCreator.class);

    private final ExecutionOptions executionOptions;
    private final SignatureHelper signatureHelper;

    public SingleAsicCreator(ExecutionOptions executionOptions, SignatureHelper signatureHelper) {

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
