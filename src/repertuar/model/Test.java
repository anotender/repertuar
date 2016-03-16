package repertuar.model;

import org.openqa.selenium.phantomjs.PhantomJSDriverService;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by anotender on 07.12.15.
 */
public class Test {

    public static void main(String[] args) throws InterruptedException {
        java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(java.util.logging.Level.OFF);
        Logger.getLogger(PhantomJSDriverService.class.getName()).setLevel(Level.OFF);

        PrintWriter out = null;

        try {
            out = new PrintWriter("content.html");

            Website w = new Website("http://www.cinema-city.pl/Janki");
            w.loadContent(true);

            for (String line : w.getContent()) {
                out.println(line);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }
}
