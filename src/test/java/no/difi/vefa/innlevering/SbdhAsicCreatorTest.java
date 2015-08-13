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
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;
import org.unece.cefact.namespaces.standardbusinessdocumentheader.ManifestItem;
import org.unece.cefact.namespaces.standardbusinessdocumentheader.StandardBusinessDocumentHeader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;

import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

/**
 * @author steinar
 *         Date: 13.08.15
 *         Time: 14.40
 */
public class SbdhAsicCreatorTest {

    public static final Logger log = LoggerFactory.getLogger(SbdhAsicCreatorTest.class);
    @Test
    public void createFromSampleSbdh() throws Exception {

        String resources[] = {
            "sbdh-peppol-sample-v1.3.xml",
            "trdm090-submit-tender-sample.xml",
            "sample-readme.txt"
        };


        File sbdhDir = Util.createTempdir();
        for (String resource : resources) {
            InputStream sbdhStream = SbdhAsicCreatorTest.class.getClassLoader().getResourceAsStream(resource);
            File file = new File(sbdhDir, resource);
            FileUtils.copyInputStreamToFile(sbdhStream, file);
        }


        SignatureHelper signatureHelper = new SignatureHelper(KeyStoreUtil.sampleKeyStoreStream(), KeyStoreUtil.getKeyStorePassword(), KeyStoreUtil.getKeyStoreAlias(), KeyStoreUtil.getPrivateKeyPassord());

        SbdhAsicCreator sbdhAsicCreator = new SbdhAsicCreator(signatureHelper);

        File asicFile = File.createTempFile("vefa-innlevering-", ".asice");
        sbdhAsicCreator.createFrom(new File(sbdhDir,"sbdh-peppol-sample-v1.3.xml"), asicFile);

        log.debug("Created " + asicFile);

    }
}