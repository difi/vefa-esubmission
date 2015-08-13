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

import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 * @author steinar
 *         Date: 13.08.15
 *         Time: 20.39
 */
public class Util {

    public static File createTempdir() {
        String tmpDirName = System.getProperty("java.io.tmpdir");

        File sbdhDir = null;

        Date now = new Date();
        String dirName = String.format("vefa-innlevering-%tFT%tH%tM%tS", now,now,now,now);
        sbdhDir = new File(tmpDirName, dirName);

        if (!sbdhDir.mkdir()) {
            throw new IllegalStateException("Unable to create directory " + sbdhDir);
        }
        return sbdhDir;
    }
}
