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

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

/**
 * @author steinar
 *         Date: 22.09.15
 *         Time: 11.51
 */
public class ExecutionOptionsTest {

    private CmdLineParser cmdLineParser;
    private CmdLineOptions cmdLineOptions;

    @BeforeMethod
    public void setUp() throws Exception {
        cmdLineOptions = new CmdLineOptions();
        cmdLineParser = new CmdLineParser(cmdLineOptions);
    }

    private ExecutionOptions computeExecutionOptions(String[] args) throws CmdLineException {
        cmdLineParser.parseArgument(args);

        // Calculates the effective execution options. I.e. any options not specified, will have a reasonable default
        ExecutionOptions executionOptions = new ExecutionOptions(cmdLineOptions);
        return executionOptions;
    }


    @Test
    public void withSbdhOnly() throws Exception {

        String args[] = {
                "-sbdh","sbdh.xml",
                "-ks","keystore.jks",
                "-kp", "secret"

        };

        ExecutionOptions executionOptions = computeExecutionOptions(args);
        assertEquals(ExecutionOptions.ExecMode.SBDH,executionOptions.getExecutionMode());
    }



    @Test
    public void manualSpecifications() throws CmdLineException {
        String args[] = {
                "-sbdh","sbdh.xml",
                "-bis", "bisdocument.xml",
                "-a", "file1",
                "-a", "file2",
                "-ks","keystore.jks",
                "-kp", "secret"

        };

        ExecutionOptions executionOptions = computeExecutionOptions(args);
        assertEquals(ExecutionOptions.ExecMode.MANUAL,executionOptions.getExecutionMode());
    }

    @Test
    public void unwrap() throws Exception {
        String args[] = {
                "-unwrap","message.xml",

        };

        ExecutionOptions executionOptions = computeExecutionOptions(args);
        assertEquals(ExecutionOptions.ExecMode.UNWRAP,executionOptions.getExecutionMode());
    }

    @Test
    public void wrap() throws Exception {
        String args[] = {
                "-wrap","message.xml",

        };

        ExecutionOptions executionOptions = computeExecutionOptions(args);
        assertEquals(ExecutionOptions.ExecMode.WRAP,executionOptions.getExecutionMode());
    }


}