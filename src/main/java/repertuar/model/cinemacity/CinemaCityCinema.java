package repertuar.model.cinemacity;

import repertuar.model.Cinema;
import repertuar.service.api.ChainService;

import static repertuar.model.Chain.CINEMA_CITY;

public class CinemaCityCinema extends Cinema {

    public CinemaCityCinema(Integer id, String name, String url) {
        super(id, name, url);
    }

    @Override
    protected ChainService getChainService() {
        return CINEMA_CITY.getService();
    }

}
