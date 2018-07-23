package repertuar.model.cinemacity;

import repertuar.model.Chain;
import repertuar.service.api.ChainService;
import repertuar.service.impl.CinemaCityService;

public class CinemaCity extends Chain {

    public static final String BASE_URL = "https://www.cinema-city.pl";

    public CinemaCity(String name, String url) {
        super(name, url);
    }

    @Override
    public ChainService getService() {
        return CinemaCityService.getInstance();
    }
}