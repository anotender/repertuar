package repertuar.model.helios;

import repertuar.model.Cinema;
import repertuar.service.api.ChainService;

import static repertuar.model.Chain.HELIOS;

public class HeliosCinema extends Cinema {

    public HeliosCinema(Integer id, String name, String url) {
        super(id, name, url);
    }

    @Override
    protected ChainService getChainService() {
        return HELIOS.getService();
    }

}
