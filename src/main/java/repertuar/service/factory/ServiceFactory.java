package repertuar.service.factory;

import repertuar.model.Chain;
import repertuar.model.Cinema;
import repertuar.model.cinemaCity.CinemaCity;
import repertuar.model.cinemaCity.CinemaCityCinema;
import repertuar.model.multikino.Multikino;
import repertuar.model.multikino.MultikinoCinema;
import repertuar.service.api.ChainService;
import repertuar.service.impl.CinemaCityService;
import repertuar.service.impl.MultikinoService;

public class ServiceFactory {

    private MultikinoService multikinoService = new MultikinoService();
    private CinemaCityService cinemaCityService = new CinemaCityService();

    public ChainService getService(Chain c) {
        if (c instanceof Multikino) {
            return multikinoService;
        } else if (c instanceof CinemaCity) {
            return cinemaCityService;
        }
        return null;
    }

    public ChainService getService(Cinema c) {
        if (c instanceof MultikinoCinema) {
            return multikinoService;
        } else if (c instanceof CinemaCityCinema) {
            return cinemaCityService;
        }
        return null;
    }

}
