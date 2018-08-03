package repertuar.model.multikino;

import repertuar.model.Cinema;
import repertuar.service.api.ChainService;
import repertuar.service.impl.MultikinoService;

public class MultikinoCinema extends Cinema {

    public MultikinoCinema(Integer id, String name, String url) {
        super(id, name, url);
    }

    @Override
    public ChainService getService() {
        return MultikinoService.getInstance();
    }
}
