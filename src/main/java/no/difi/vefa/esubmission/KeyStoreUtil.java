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

import java.io.InputStream;

/**
 * @author steinar
 *         Date: 13.08.15
 *         Time: 14.59
 */
public class KeyStoreUtil {


    public static final String VEFA_INNLEVERING_JKS = "vefa-esubmission.jks";
    private static String keyStorePassword = "changeit";
    private static String keyStoreAlias = null;
    private static String privateKeyPassord = "changeit";

    public static InputStream sampleKeyStoreStream() {
        InputStream resourceAsStream = KeyStoreUtil.class.getClassLoader().getResourceAsStream(VEFA_INNLEVERING_JKS);
        if (resourceAsStream == null) {
            throw new IllegalStateException("Unable to load internally provided keystore from " + VEFA_INNLEVERING_JKS);
        }

        return resourceAsStream;
    }

    public static String getKeyStorePassword() {
        return keyStorePassword;
    }

    public static String getKeyStoreAlias() {
        return keyStoreAlias;
    }

    public static String getPrivateKeyPassord() {
        return privateKeyPassord;
    }
}
