package repertuar.model.cinemacity;

import repertuar.model.Cinema;
import repertuar.service.api.ChainService;
import repertuar.service.impl.CinemaCityService;

public class CinemaCityCinema extends Cinema {

    public CinemaCityCinema(Integer id, String name, String url) {
        super(id, name, url);
    }

    @Override
    public ChainService getService() {
        return CinemaCityService.getInstance();
    }
}
