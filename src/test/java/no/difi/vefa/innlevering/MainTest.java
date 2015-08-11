package no.difi.vefa.innlevering;


import org.testng.annotations.Test;

/**
 * Unit test for simple Main.
 */
public class MainTest {

    @Test
    public void testCreateViaCommandLine() throws Exception {

        String args[] = {
                "-bis","TEST",
                "-ks", "TEST"
        };

        Main.main(args);


    }
}
