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

import no.difi.vefa.sbdh.AsicExtractor;
import no.difi.vefa.sbdh.AsicExtractorFactory;
import no.difi.vefa.sbdh.SbdWrapper;
import org.unece.cefact.namespaces.standardbusinessdocumentheader.ManifestItem;
import org.unece.cefact.namespaces.standardbusinessdocumentheader.StandardBusinessDocumentHeader;

import java.io.*;
import java.math.BigInteger;

/**
 * Wraps and unwraps ASiC archives as base64 encoded payload within StandardBusinessDocumen instances.
 *
 * @author steinar
 *         Date: 22.09.15
 *         Time: 12.20
 */
public class AsicWrapper {


    /**
     * UnwrapsASiC archive contained as  base64 encoded payload within &gt;StandardBusinessDocument&lt;
     */
    public static void unwrapAsicFromSbd(File unwrapFile, File outputFile) {

        AsicExtractor asicExtractor = AsicExtractorFactory.defaultAsicExtractor();

        InputStream result = getInputStream(unwrapFile);

        OutputStream outputStream = getOutputStream(outputFile);

        asicExtractor.extractAsic(result, outputStream);

        try {
            outputStream.close();
        } catch (IOException e) {
            throw new IllegalStateException("Unable to close outputstream");
        }

    }

    /**
     * Wraps binary ASiC archive as base64 payload within SBD XML file.
     *
     * @param asicFile   ASiC archive to be encoded
     * @param sbdXmlFile file into which the results should be written.
     */
    public static void wrapAsicIntoSbd(File asicFile, File sbdXmlFile) {

        InputStream inputStream = getInputStream(asicFile);
        OutputStream outputStream = getOutputStream(sbdXmlFile);

        // Extracts the inner SBDH from the ASiC archive
        StandardBusinessDocumentHeader sbdh = SbdhFromAsicExtractor.extractSbdhFromAsic(inputStream);

        // Closes and reopens the inputstream
        inputStream = closeAndOpen(asicFile, inputStream);

        // Modifies the sbdh according to the rules for the outer
        StandardBusinessDocumentHeader outerSbdh = createOuterSbdh(sbdh);

        SbdWrapper sbdWrapper = new SbdWrapper();
        sbdWrapper.wrapInputStream(sbdh, inputStream, outputStream);

        try {
            inputStream.close();
            outputStream.close();
        } catch (IOException e) {
            throw new IllegalStateException("Unable to close input and output streams");
        }

    }

    /**
     * Modifies the inner SBDH to be used as the outer SBDH.
     *
     * @param sbdh the inner SBDH as extracted from the ASiC archive
     * @return
     */
    static StandardBusinessDocumentHeader createOuterSbdh(StandardBusinessDocumentHeader sbdh) {

        sbdh.getManifest().getManifestItem().clear();

        ManifestItem manifestItem = new ManifestItem();
        manifestItem.setMimeTypeQualifierCode("application/vnd.etsi.asic-e+zip");
        manifestItem.setUniformResourceIdentifier("#asic");
        manifestItem.setDescription("ASiC archive holding the business documents.");

        sbdh.getManifest().getManifestItem().add(manifestItem);

        sbdh.getManifest().setNumberOfItems(BigInteger.valueOf(1));
        return sbdh;
    }

    /**
     * Closes and re-opens the inpustream associated with the supplied ASiC file.
     *
     * @param asicFile    file object associated with the supplied InputStream
     * @param inputStream InputStream to be closed
     * @return the re-opened InputStream
     */
    private static InputStream closeAndOpen(File asicFile, InputStream inputStream) {
        // Reopen input file
        try {
            inputStream.close();
            inputStream = getInputStream(asicFile);
        } catch (IOException e) {
            throw new IllegalStateException("Unable to close input stream for " + asicFile + ", reason:", e);
        }
        return inputStream;
    }


    /**
     * Convenience method for opening a file for output
     */
    private static OutputStream getOutputStream(File outputFile) {
        OutputStream outputStream;
        try {
            outputStream = new FileOutputStream(outputFile);
        } catch (FileNotFoundException e) {
            throw new IllegalStateException("Unable to open outputfile " + outputFile + ", reason:" + e.getMessage());
        }
        return outputStream;
    }

    /**
     * Convenience method for opening a file for input
     */
    private static InputStream getInputStream(File unwrapFile) {
        InputStream result;
        try {
            BufferedInputStream sbdInputStream = new BufferedInputStream(new FileInputStream(unwrapFile));
            result = sbdInputStream;
        } catch (FileNotFoundException e) {
            throw new IllegalStateException("Unable to open " + unwrapFile + ", reason:" + e.getMessage(), e);
        }
        return result;
    }
}
