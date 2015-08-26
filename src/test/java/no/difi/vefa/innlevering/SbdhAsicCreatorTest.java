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

import no.difi.asic.SignatureHelper;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

/**
 * @author steinar
 *         Date: 13.08.15
 *         Time: 14.40
 */
public class SbdhAsicCreatorTest {

    public static final Logger log = LoggerFactory.getLogger(SbdhAsicCreatorTest.class);

    /**
     * Extracts the resources embedded in the project into an external directory, after which the SBDH is parsed and
     * an ASiC archive is created based upon the contents of the SBDH.
     *
     * @throws Exception
     */
    @Test
    public void createFromSampleSbdh() throws Exception {

        String resources[] = {
                Util.SAMPLE_SBDH_RESOURCE,  // The SBDH, which contains the references to the next two files.
                "trdm090-submit-tender-sample.xml",
                "sample-readme.txt"
        };


        // Creates a temporary directory in which we will place the SBDH together with the referenced files.
        File sbdhDir = Util.createUniqueTempdir();

        // Iterates over each resource and copies them to the temporary directory
        for (String resource : resources) {
            InputStream sbdhStream = SbdhAsicCreatorTest.class.getClassLoader().getResourceAsStream(resource);
            File file = new File(sbdhDir, resource);
            FileUtils.copyInputStreamToFile(sbdhStream, file);
        }

        // Establish the SignatureHelper to be used by the SbdhAsicCreator
        SignatureHelper signatureHelper = new SignatureHelper(KeyStoreUtil.sampleKeyStoreStream(), KeyStoreUtil.getKeyStorePassword(), KeyStoreUtil.getKeyStoreAlias(), KeyStoreUtil.getPrivateKeyPassord());

        SbdhAsicCreator sbdhAsicCreator = new SbdhAsicCreator(signatureHelper);

        // Temporary file in which the ASiC archive will be created
        File asicFile = File.createTempFile("vefa-esubmission-", ".asice");

        // Compute the full path name of the SBDH to be parsed
        File sbdhFile = new File(sbdhDir, Util.SAMPLE_SBDH_RESOURCE);

        // Creates the ASiC archive
        sbdhAsicCreator.createFrom(sbdhFile, asicFile);

        assertTrue(sbdhFile.canRead(), sbdhFile + " not readable");
        assertTrue(asicFile.exists() && asicFile.canRead(), asicFile + " not readable or does not exist");

        // Verifies the contents of the ASiC archive
        ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(asicFile));

        ZipEntry zipEntry = null;
        List<String> entries = new ArrayList<>();

        while ((zipEntry = zipInputStream.getNextEntry()) != null) {
            entries.add(zipEntry.getName());
        }

        for (String resourceName : resources) {
            // The SBDH file is always entered into the archive with the same name
            if (resourceName.equals(Util.SAMPLE_SBDH_RESOURCE)) {
                assertTrue(entries.contains(SbdhAsicCreator.STANDARD_NAME_FOR_SBDH_XML));
            } else {
                assertTrue(entries.contains(resourceName), resourceName + " not found in generated ASiC archive");
            }
        }

        log.debug("Created " + asicFile);
    }
}