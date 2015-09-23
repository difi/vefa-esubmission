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

import no.difi.vefa.sbdh.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;
import org.unece.cefact.namespaces.standardbusinessdocumentheader.StandardBusinessDocumentHeader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static org.testng.Assert.*;

/**
 * @author steinar
 *         Date: 22.09.15
 *         Time: 12.24
 */
public class AsicWrapperTest {

    public static final Logger log = LoggerFactory.getLogger(AsicWrapperTest.class);

    @Test(dataProvider = "sampleSbdFile", dataProviderClass = no.difi.vefa.esubmission.SampleDataProvider.class)
    public void testUnwrapAsicFromSbd(File sbdFile) throws Exception {

        // Place the unwrapped result here
        File asicFile = File.createTempFile("vefa-esubmission", ".asic");

        // Performs the actual unwrapping
        AsicWrapper.unwrapAsicFromSbd(sbdFile, asicFile);

        assertTrue(asicFile.canRead(), "Unable to read extracted ASiC file");

        boolean asicManifestSeen = verifyAsicFile(asicFile);

        assertTrue(asicManifestSeen, "No META-INF/asicmanifest* found in ASiC archive");
    }


    @Test(dataProvider = "sampleAsicFile", dataProviderClass = SampleDataProvider.class)
    public void wrapSampleAsicIntoSbd(File sampleAsicFile) throws Exception {

        File sbdXmlFile = File.createTempFile("vefa-sbd-sample", ".xml");

        // Performs the wrapping of data
        AsicWrapper.wrapAsicIntoSbd(sampleAsicFile, sbdXmlFile);

        // Size of vefa-sbd-sample.xml should be greater than zero
        assertTrue(sbdXmlFile.canRead() && sbdXmlFile.length() > 0, "Seems no data was written to output file " + sbdXmlFile);

        // Extracts and parses the SBDH from the generated SBD
        SbdhParser sbdhParser = SbdhParserFactory.sbdhParserAndExtractor();
        StandardBusinessDocumentHeader sbdh = sbdhParser.parse(new FileInputStream(sbdXmlFile));
        assertNotNull(sbdh, "No SBDH found!");


        /**
         *  Extracts and decodes the basic64 payload into a binary ASiC archive
         */
        File decodedAsicFile = File.createTempFile("vefa-asic-sample", ".asice");

        // Performs the unwrap operation
        AsicWrapper.unwrapAsicFromSbd(sbdXmlFile, decodedAsicFile);

        // Verifies that we can read the entire zip file
        boolean asicOk = verifyAsicFile(decodedAsicFile);
        assertTrue(asicOk, "ASiC file seems to be valid");

        log.debug("Wrapped and unwrapped \nASiC file: " + sampleAsicFile + "\nsbd.xml: " + sbdXmlFile + "\nregenerated: " + decodedAsicFile);
    }

    private boolean verifyAsicFile(File asicFile) throws IOException {

        log.debug("Size of ASiC file is " + asicFile.length());

        FileInputStream asicFileInput = new FileInputStream(asicFile);
        ZipInputStream zipInputStream = new ZipInputStream(asicFileInput);

        boolean asicManifestSeen = false;
        ZipEntry zipEntry;
        while ((zipEntry = zipInputStream.getNextEntry()) != null) {
            log.debug("Found zip entry: " + zipEntry.getName());
            if (zipEntry.getName().startsWith("META-INF/asicmanifest")) {
                asicManifestSeen = true;
            }
        }

        return asicManifestSeen;
    }

}