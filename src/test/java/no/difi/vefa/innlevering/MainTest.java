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


import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;

import static org.testng.Assert.assertTrue;

/**
 * Unit test for simple Main.
 */
public class MainTest {

    @BeforeMethod
    public void setUp() {
        File file = new File(CmdLineOptions.VEFA_INNLEVERING_ASICE);
        if (file.exists()) {
            file.delete();
        }
    }


    @Test
    public void testCreateViaCommandLine() throws Exception {

        String args[] = {
                "-bis","TEST",
                "-ks", "TEST"
        };

        Main.main(args);

    }

    @Test
    public void createAsicFromSbdh() {
        String args[] = {
            "-sbdh","TEST",
            "-ks","TEST"
        };

        Main.main(args);

    }

    @AfterMethod
    public void after() {
        File file = new File(CmdLineOptions.VEFA_INNLEVERING_ASICE);
        assertTrue(file.canRead(), "Seems that the file vefa-innlevering.asice was not created");
        file.delete();
    }

}
