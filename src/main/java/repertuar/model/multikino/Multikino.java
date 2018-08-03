package repertuar.model.multikino;

import repertuar.model.Chain;
import repertuar.service.api.ChainService;
import repertuar.service.impl.MultikinoService;

public class Multikino extends Chain {

    public static final String BASE_URL = "https://multikino.pl";

    public Multikino(String name, String url) {
        super(name, url);
    }

    @Override
    public ChainService getService() {
        return MultikinoService.getInstance();
    }
}
