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
    public static final String VEFA_INNLEVERING_JKS = "vefa-innlevering.jks";

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
        .sign(keyStoreFile, "changeit","changeit");

        log.debug("Created ASiC archive in " + archiveOutputFile);

    }
}
