package repertuar.service.extractor;

import org.apache.commons.io.IOUtils;

import java.io.IOException;

public abstract class BaseExtractorTest<T> {

    protected T getResource(String path) throws IOException {
        return createObjectFromString(IOUtils.toString(this.getClass().getResourceAsStream(path), "UTF-8"));
    }

    protected abstract T createObjectFromString(String content);

}
