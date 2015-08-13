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
import org.testng.annotations.Test;
import org.unece.cefact.namespaces.standardbusinessdocumentheader.ManifestItem;
import org.unece.cefact.namespaces.standardbusinessdocumentheader.StandardBusinessDocumentHeader;

import java.io.File;
import java.io.InputStream;
import java.net.URL;

/**
 * @author steinar
 *         Date: 13.08.15
 *         Time: 14.40
 */
public class SbdhAsicCreatorTest {

    @Test
    public void createFromSampleSbdh() throws Exception {

        InputStream sbdhStream = SbdhAsicCreatorTest.class.getClassLoader().getResourceAsStream("sbdh-peppol-sample-v1.3.xml");

        SignatureHelper signatureHelper = new SignatureHelper(KeyStoreUtil.sampleKeyStoreStream(), KeyStoreUtil.getKeyStorePassword(), KeyStoreUtil.getKeyStoreAlias(), KeyStoreUtil.getPrivateKeyPassord());

        AsicWriter asicWriter = AsicWriterFactory.newFactory().newContainer(new File(CmdLineOptions.VEFA_INNLEVERING_ASICE));

        SbdhParser sbdhParser = new SbdhParser();
        StandardBusinessDocumentHeader sbdh = sbdhParser.parse(sbdhStream);

        // Adds the SBDH

        // Adds the main UBL XML document
        if (sbdh.getManifest().getNumberOfItems().longValue() > Integer.MAX_VALUE) {
            throw new IllegalStateException("Too many Manifest items: " + sbdh.getManifest().getNumberOfItems().longValue() + " only " + Integer.MAX_VALUE + " allowed");
        }

        for (int itemCount = 0; itemCount < sbdh.getManifest().getNumberOfItems().intValue(); itemCount++) {

            ManifestItem manifestItem = sbdh.getManifest().getManifestItem().get(itemCount);

            String documentName = manifestItem.getUniformResourceIdentifier().replace("cid:", "");
            InputStream mainDocumentInputStream = SbdhAsicCreatorTest.class.getClassLoader().getResourceAsStream(documentName);
            asicWriter.add(mainDocumentInputStream, documentName, MimeType.forString(manifestItem.getMimeTypeQualifierCode()));

            if (itemCount == 0) {
                asicWriter.setRootEntryName(documentName);
            }
        }

        // Adds the attachments

        asicWriter.sign(signatureHelper);

    }
}