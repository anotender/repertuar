package repertuar.model.helios;

import repertuar.model.Chain;
import repertuar.service.api.ChainService;
import repertuar.service.impl.HeliosService;

public class Helios extends Chain {

    public static final String BASE_URL = "http://helios.pl";

    public Helios(String name, String url) {
        super(name, url);
    }

    @Override
    public ChainService getService() {
        return HeliosService.getInstance();
    }
}
