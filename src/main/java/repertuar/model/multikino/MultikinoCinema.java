package repertuar.model.multikino;

import repertuar.model.Cinema;
import repertuar.service.api.ChainService;

import static repertuar.model.Chain.MULTIKINO;

public class MultikinoCinema extends Cinema {

    public MultikinoCinema(Integer id, String name, String url) {
        super(id, name, url);
    }

    @Override
    protected ChainService getChainService() {
        return MULTIKINO.getService();
    }

}
