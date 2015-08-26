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

package no.difi.vefa.innlevering;

import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.ExampleMode;
import org.kohsuke.args4j.OptionHandlerFilter;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

/**
 * Provides the famous "usage" message for this component.
 *
 * @author steinar
 *         Date: 11.08.15
 *         Time: 13.06
 */
class Usage {

    static public void usage(OutputStream outputStream, CmdLineParser cmdLineParser, String message) {

        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
        PrintWriter writer = new PrintWriter(outputStreamWriter);

        writer.println();
        writer.println(message);
        writer.println();

        writer.println("java -jar vefa-esubmission.jar [options...]");
        writer.flush();
        cmdLineParser.printUsage(outputStream);
        writer.println();
        writer.println(" Example: java -jar vefa-esubmission.jar -bis trdm090.xml" + cmdLineParser.printExample(OptionHandlerFilter.ALL));
        writer.flush();
    }
}
