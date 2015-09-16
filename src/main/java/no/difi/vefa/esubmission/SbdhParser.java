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

import org.unece.cefact.namespaces.standardbusinessdocumentheader.StandardBusinessDocumentHeader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;

/**
 * @author steinar
 *         Date: 13.08.15
 *         Time: 12.20
 */
public class SbdhParser {

    private final JAXBContext jaxbContext;

    public SbdhParser() {
        try {
            jaxbContext = JAXBContext.newInstance(StandardBusinessDocumentHeader.class);
        } catch (JAXBException e) {
            throw new IllegalStateException("Unable to create JAXBContext " + e.getMessage(), e);
        }
    }

    /** Parses the inputstream, which should contain a StandardBusinessDocument XML document */
    public StandardBusinessDocumentHeader parse(InputStream sbdhInputStream) {
        Unmarshaller unmarshaller = null;
        try {
            unmarshaller = jaxbContext.createUnmarshaller();
        } catch (JAXBException e1) {
            throw new IllegalStateException("Unable to create JAXB unmarshaller " + e1.getMessage(), e1);
        }

        JAXBElement element  = null;
        try {
            element = (JAXBElement) unmarshaller.unmarshal(sbdhInputStream);
        } catch (JAXBException e) {
            throw new IllegalStateException("Unable to parse input data into StandardBusinessDocumentHeader", e);

        }
        StandardBusinessDocumentHeader sbdh = (StandardBusinessDocumentHeader) element.getValue();
        return sbdh;

    }
}
