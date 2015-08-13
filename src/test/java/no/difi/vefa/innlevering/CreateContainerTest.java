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

import no.difi.asic.AsicWriterFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.File;
import java.net.URL;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

/**
 * @author steinar
 *         Date: 11.08.15
 *         Time: 09.30
 */
public class CreateContainerTest {

    public static final String TRDM090_SUBMIT_TENDER_SAMPLE_XML = "trdm090-submit-tender-sample.xml";
    public static final String VEFA_INNLEVERING_JKS = KeyStoreUtil.VEFA_INNLEVERING_JKS;

    public static final Logger log = LoggerFactory.getLogger(CreateContainerTest.class);

    @BeforeTest
    public void setUp() {

    }

    @Test
    public void createSimpleContainer() throws Exception {

        URL trdm090Resource = CreateContainerTest.class.getClassLoader().getResource(TRDM090_SUBMIT_TENDER_SAMPLE_XML);
        assertNotNull(trdm090Resource, "Unable to locate " + TRDM090_SUBMIT_TENDER_SAMPLE_XML + " in class path");
        File trdm090SampleFile = new File(trdm090Resource.toURI());

        URL keystoreUrl = CreateContainerTest.class.getClassLoader().getResource(VEFA_INNLEVERING_JKS);
        assertNotNull(keystoreUrl, "Unable to locate the keystore " + VEFA_INNLEVERING_JKS);
        File keyStoreFile = new File(keystoreUrl.toURI());
        assertTrue(keyStoreFile.canRead(), "Keystore " + VEFA_INNLEVERING_JKS + " not readable");

        File archiveOutputFile = new File(System.getProperty("java.io.tmpdir"), "vefa-sample.asice");

        AsicWriterFactory asicWriterFactory = AsicWriterFactory.newFactory();
        asicWriterFactory.newContainer(archiveOutputFile)
                .add(trdm090SampleFile)
                .setRootEntryName(trdm090SampleFile.getName())
        .sign(keyStoreFile, KeyStoreUtil.getKeyStorePassword(),KeyStoreUtil.getPrivateKeyPassord());

        log.debug("Created ASiC archive in " + archiveOutputFile);

    }
}
