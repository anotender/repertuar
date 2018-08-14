package repertuar.service.extractor;

import org.json.JSONObject;

public class JSONExtractorTest extends BaseExtractorTest<JSONObject> {

    @Override
    protected JSONObject createObjectFromString(String content) {
        return new JSONObject(content);
    }

}
