package repertuar.service.extractor;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class HTMLExtractorTest extends BaseExtractorTest<Document> {

    @Override
    protected Document createObjectFromString(String content) {
        return Jsoup.parse(content);
    }

}
