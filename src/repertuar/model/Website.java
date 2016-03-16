package repertuar.model;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * Created by mateu on 24.09.2015.
 */
public class Website {
    private static final WebDriver DRIVER;

    static {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setJavascriptEnabled(true);
        caps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, "C:\\Users\\mateu\\Dysk Google\\Programy\\repertuar\\src\\lib\\phantomjs.exe");
        DRIVER = new PhantomJSDriver(caps);
    }


    private final String website;
    private final LinkedList<String> content;

    public static boolean isInternetConnectionAvailible() {
        try {
            new URL("http://www.google.com").openConnection();
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    public Website(String website) {
        this.website = website;
        this.content = new LinkedList<>();
    }

    public void loadContent(boolean enableJavaScript) {
        if (enableJavaScript) {
            DRIVER.get(website);

            JavascriptExecutor jse = (JavascriptExecutor) DRIVER;

            String pageLoadStatus;
            do {
                pageLoadStatus = (String) jse.executeScript("return document.readyState");
            } while (!pageLoadStatus.equals("complete"));

            Collections.addAll(content, DRIVER.getPageSource().split("\n"));
        } else {
            Scanner in = null;
            try {
                URL url = new URL(website);
                URLConnection connection = url.openConnection();

                in = new Scanner(connection.getInputStream());

                while (in.hasNextLine()) {
                    content.add(in.nextLine());
                }

                in.close();
            } catch (IOException e) {
            } finally {
                if (in != null) {
                    in.close();
                }
            }
        }
    }

    public LinkedList<String> getContent() {
        return content;
    }

    public void open() throws Exception {
        Desktop.getDesktop().browse(new URL(website).toURI());
    }

    @Override
    public String toString() {
        return website;
    }

}
