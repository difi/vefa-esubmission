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

import no.difi.vefa.sbdh.SbdhParser;
import no.difi.vefa.sbdh.SbdhParserFactory;
import org.unece.cefact.namespaces.standardbusinessdocumentheader.StandardBusinessDocumentHeader;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Extracts the SBDH from sbdh.xml in the ASiC archive.
 *
 * @author steinar
 *         Date: 22.09.15
 *         Time: 14.56
 */
class SbdhFromAsicExtractor {

    public static StandardBusinessDocumentHeader extractSbdhFromAsic(InputStream inputStream) {
        StandardBusinessDocumentHeader standardBusinessDocumentHeader = null;

        ZipInputStream zipInputStream = new ZipInputStream(inputStream);
        ZipEntry zipEntry;
        try {
            while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                if (zipEntry.getName().equalsIgnoreCase("sbdh.xml")) {

                    // Parses the sbdh into StandardBusinessDocumentHeader
                    SbdhParser sbdhParser = SbdhParserFactory.parserForSbdhOnly();
                    standardBusinessDocumentHeader = sbdhParser.parse(zipInputStream);

                    break;
                }
            }

        } catch (IOException e) {
            throw new IllegalStateException("Unable to read from input zip file, reason: " + e.getMessage(), e);
        }

        if (standardBusinessDocumentHeader == null) {
            throw new IllegalStateException("Unable to locate SBDH in the input stream");
        }

        return standardBusinessDocumentHeader;
    }
}
