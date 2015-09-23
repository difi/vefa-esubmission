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
                "-sbdh","TEST",
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

    @Test
    public void wrapAsic() {
        String args[] = {
                "-sbdh","TEST",
                "-ks","TEST",
                "-o", "vefa-esubmission.asice"
        };

        Main.main(args);

        String wrapArgs[] = {

                "-wrap","vefa-esubmission.asice",
                "-o", "sbd.xml"
        };

        Main.main(wrapArgs);

        File sbd = new File("sbd.xml");

        assertTrue(sbd.canRead(), "Seems that sbd.xml was not created when wrapping generated asic file");

        // Finally unwrap it

        String[] unwrapArgs = {
                "-unwrap","sbd.xml",
                "-out","vefa-submission-regenerated.asice"
        };

        Main.main(unwrapArgs);

        File regeneratedAsic = new File("vefa-submission-regenerated.asice");
        assertTrue(regeneratedAsic.canRead());

    }

    @AfterMethod
    public void after() {
        File file = new File(CmdLineOptions.VEFA_INNLEVERING_ASICE);
        assertTrue(file.canRead(), "Seems that the file vefa-esubmission.asice was not created");
        // file.delete();
    }

}
