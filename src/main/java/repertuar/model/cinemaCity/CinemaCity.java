package repertuar.model.cinemaCity;

import repertuar.model.Chain;
import repertuar.service.api.ChainService;
import repertuar.service.factory.ServiceFactory;

public class CinemaCity extends Chain {

    public CinemaCity(String name, String url) {
        super(name, url);
    }

    @Override
    public ChainService getService() {
        return ServiceFactory.getCinemaCityService();
    }
}
