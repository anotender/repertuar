package repertuar.model.helios;

import repertuar.model.Cinema;
import repertuar.service.api.ChainService;
import repertuar.service.impl.HeliosService;

public class HeliosCinema extends Cinema {

    public HeliosCinema(Integer id, String name, String url) {
        super(id, name, url);
    }

    @Override
    public ChainService getService() {
        return HeliosService.getInstance();
    }
}
