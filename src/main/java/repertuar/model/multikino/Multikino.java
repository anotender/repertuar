package repertuar.model.multikino;

import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import repertuar.model.Chain;
import repertuar.model.Website;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by anotender on 07.12.15.
 */
public class Multikino extends Chain {

    public Multikino(String name, Website website) {
        super(name, website);
    }

    @Override
    public void loadCinemas() throws IOException {
        Website website = new Website("https://multikino.pl/pl/nasze-kina");

        HtmlPage page = website.loadPageWithJavaScriptDisabled();

        List<DomElement> elements = page.getElementsByTagName("a");

        for (DomElement e : elements) {
            if (e.hasAttribute("class") && e.getAttribute("class").equals("title")) {
                String url = "https://multikino.pl" + e.getAttribute("href");
                String city = e.getTextContent();
                cinemas.add(new MultikinoCinema(null, city, url));
            }
        }
    }
}
