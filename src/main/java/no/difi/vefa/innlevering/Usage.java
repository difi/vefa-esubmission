package no.difi.vefa.innlevering;

import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.ExampleMode;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

/**
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

        writer.println("java -jar vefa-innlevering.jar [options...]");
        writer.flush();
        cmdLineParser.printUsage(outputStream);
        writer.println();
        writer.println(" Example: java -jar vefa-innlevering.jar -bis trdm090.xml" + cmdLineParser.printExample(ExampleMode.ALL));
        writer.flush();
    }
}
