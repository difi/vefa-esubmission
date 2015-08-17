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

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

/**
 * @author steinar
 *         Date: 13.08.15
 *         Time: 20.39
 */
public class Util {

    public static final String SAMPLE_SBDH_RESOURCE = "sbdh-peppol-sample-v1.3.xml";
    public static final String SAMPLE_UBL_DOCUMENT = "trdm090-submit-tender-sample.xml";
    public static final java.lang.String SAMPLE_ATTACHMENT = "sample-readme.txt";

    public static File createUniqueTempdir() {
        String tmpDirName = System.getProperty("java.io.tmpdir");

        File sbdhDir = null;

        Date now = new Date();
        String dirName = String.format("vefa-innlevering-%tFT%tH%tM%tS", now, now, now, now);
        sbdhDir = new File(tmpDirName, dirName);

        if (!sbdhDir.exists() && !sbdhDir.mkdir()) {
            throw new IllegalStateException("Unable to create directory " + sbdhDir);
        }
        return sbdhDir;
    }

    public static File extractResourceToTempDir(String resourceName) {
        File tempdir = Util.createUniqueTempdir();

        return extractResourceToTempDir(resourceName, tempdir);
    }

    public static File extractResourceToTempDir(String resourceName, File tempdir) {
        File resourceFile = new File(tempdir, resourceName);
        InputStream resourceAsStream = Util.class.getClassLoader().getResourceAsStream(resourceName);
        try {
            FileUtils.copyInputStreamToFile(resourceAsStream, resourceFile);
        } catch (IOException e) {
            throw new IllegalStateException("Unable to copy internal resource " + resourceName + " to temp dir " + tempdir);
        }

        return resourceFile;
    }

}
