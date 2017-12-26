package repertuar.model.helios;

import repertuar.model.Chain;
import repertuar.service.api.ChainService;
import repertuar.service.factory.ServiceFactory;

public class Helios extends Chain {

    public Helios(String name, String url) {
        super(name, url);
    }

    @Override
    public ChainService getService() {
        return ServiceFactory.getHeliosService();
    }
}
