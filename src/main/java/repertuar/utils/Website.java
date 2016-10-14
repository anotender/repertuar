package repertuar.utils;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.awt.*;
import java.io.IOException;
import java.net.URL;

public class Website {
    private static final WebClient WEB_CLIENT = new WebClient(BrowserVersion.CHROME);

    static {
        WEB_CLIENT.getOptions().setCssEnabled(false);
        WEB_CLIENT.getOptions().setGeolocationEnabled(false);
    }

    private final String url;
    private HtmlPage loadedPage;

    public static boolean isInternetConnectionAvailible() {
        try {
            new URL("http://www.google.com").openConnection();
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    public Website(String url) {
        this.url = url;
    }

    public HtmlPage loadPageWithJavaScriptEnabled() throws IOException {
        WEB_CLIENT.getOptions().setJavaScriptEnabled(true);
        loadedPage = WEB_CLIENT.getPage(url);
        WEB_CLIENT.waitForBackgroundJavaScript(30 * 1000); /* will wait JavaScript to execute up to 30s */
        return loadedPage;
    }

    public HtmlPage loadPageWithJavaScriptDisabled() throws IOException {
        WEB_CLIENT.getOptions().setJavaScriptEnabled(false);
        loadedPage = WEB_CLIENT.getPage(url);
        return loadedPage;
    }

    public void open() throws Exception {
        Desktop.getDesktop().browse(new URL(url).toURI());
    }

    @Override
    public String toString() {
        return url;
    }

}
