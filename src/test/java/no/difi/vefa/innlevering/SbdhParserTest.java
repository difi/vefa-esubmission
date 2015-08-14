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

import org.testng.annotations.Test;
import org.unece.cefact.namespaces.standardbusinessdocumentheader.ManifestItem;
import org.unece.cefact.namespaces.standardbusinessdocumentheader.StandardBusinessDocumentHeader;

import java.io.InputStream;
import java.math.BigInteger;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

/**
 * @author steinar
 *         Date: 13.08.15
 *         Time: 12.22
 */
public class SbdhParserTest {


    public static final String SBDH_SAMPLE_RESOURCE = Util.SAMPLE_SBDH_RESOURCE;

    @Test
    public void parseSampleSbdh() throws Exception {

        InputStream sbdhInputStream = SbdhParserTest.class.getClassLoader().getResourceAsStream(SBDH_SAMPLE_RESOURCE);
        assertNotNull(sbdhInputStream, "Unable to locate " + SBDH_SAMPLE_RESOURCE + " in class path");
        SbdhParser sbdhParser = new SbdhParser();
        StandardBusinessDocumentHeader sbdh = sbdhParser.parse(sbdhInputStream);

        assertEquals(sbdh.getManifest().getNumberOfItems(), BigInteger.valueOf(2));
        int i= 0;
        for (ManifestItem manifestItem : sbdh.getManifest().getManifestItem()) {
            switch (i++) {
                case 0:
                    assertEquals(manifestItem.getUniformResourceIdentifier(), "cid:trdm090-submit-tender-sample.xml");
                    break;
                case 1:
                    assertEquals(manifestItem.getUniformResourceIdentifier(), "cid:sample-readme.txt");
                    break;
            }
        }
    }


    @Test(expectedExceptions = IllegalStateException.class)
    public void parseInvalidXml() throws Exception {
        InputStream is = SbdhParserTest.class.getClassLoader().getResourceAsStream("sample-readme.txt");
        assertNotNull(is, "unable to locate sample-readme.txt in class path");
        SbdhParser sbdhParser = new SbdhParser();
        StandardBusinessDocumentHeader sbdh = sbdhParser.parse(is);
    }
}