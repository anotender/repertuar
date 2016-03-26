package repertuar.model.helios;

import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import repertuar.model.Chain;
import repertuar.model.Website;

import java.io.IOException;
import java.util.List;

public class Helios extends Chain {

    public Helios(String name, Website website) {
        super(name, website);
    }

    @Override
    public void loadCinemas() throws IOException {
        HtmlPage page = website.loadPageWithJavaScriptDisabled();
        List<DomElement> divElements = page.getElementsByTagName("div");

        for (DomElement divElement : divElements) {
            if (divElement.hasAttribute("class") && divElement.getAttribute("class").equals("list")) {
                for (HtmlElement htmlElement : divElement.getElementsByTagName("a")) {
                    String url = "http://helios.pl" + htmlElement.getAttribute("href");
                    String city = htmlElement.getElementsByTagName("strong").get(0).getTextContent();
                    String name = htmlElement.getElementsByTagName("span").get(0).getTextContent();
                    cinemas.add(new HeliosCinema(name, city, url));
                }
                break;
            }
        }
    }
}
