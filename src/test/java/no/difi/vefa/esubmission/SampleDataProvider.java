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

import org.testng.annotations.DataProvider;

import java.io.File;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;

import static org.testng.Assert.assertNotNull;

/**
 * @author steinar
 *         Date: 22.09.15
 *         Time: 12.25
 */
public class SampleDataProvider {

    public static final String SAMPLE_SBD_XML = "sample-sbd.xml";
    public static final String SAMPLE_ASIC = "sample-asic.asice";

    @DataProvider(name = "sampleSbd")
    public static Object[][] sampleSbd()  {

        InputStream resourceAsStream = SampleDataProvider.class.getClassLoader().getResourceAsStream(SAMPLE_SBD_XML);
        assertNotNull(resourceAsStream, SAMPLE_SBD_XML + " not found in class path");

        return new Object[][]{{resourceAsStream}};
    }

    @DataProvider(name = "sampleSbdFile")
    public static Object[][] sampleSbdFile()  {

        URL url = SampleDataProvider.class.getClassLoader().getResource(SAMPLE_SBD_XML);
        assertNotNull(url, SAMPLE_SBD_XML + " not found in class path");

        File file = null;
        try {
            file = new File(url.toURI());
        } catch (URISyntaxException e) {
            throw new IllegalStateException("Unable to obtain File object from URL, reason:" + e.getMessage(), e);
        }

        return new Object[][]{{file}};
    }




    @DataProvider(name = "sampleAsicFile")
    public static Object[][] sampleAsic() {

        URL url = SampleDataProvider.class.getClassLoader().getResource(SAMPLE_ASIC);
        assertNotNull(url, SAMPLE_ASIC + " not found in class path");

        File result;
        try {
            result = new File(url.toURI());
            return new Object[][]{{result}};
        } catch (URISyntaxException e) {
            throw new IllegalStateException("Unable to convert URL for " + url + " into URI, reason:" + e.getMessage(), e);
        }

    }
}

