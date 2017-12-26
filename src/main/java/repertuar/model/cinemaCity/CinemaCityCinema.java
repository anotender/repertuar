package repertuar.model.cinemaCity;

import repertuar.model.Cinema;
import repertuar.service.api.ChainService;
import repertuar.service.factory.ServiceFactory;

public class CinemaCityCinema extends Cinema {

    public CinemaCityCinema(Integer id, String name, String url) {
        super(id, name, url);
    }

    @Override
    public ChainService getService() {
        return ServiceFactory.getCinemaCityService();
    }
}
