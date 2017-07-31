package repertuar.service.factory;

import repertuar.model.Chain;
import repertuar.model.Cinema;
import repertuar.model.cinemaCity.CinemaCity;
import repertuar.model.cinemaCity.CinemaCityCinema;
import repertuar.model.helios.Helios;
import repertuar.model.helios.HeliosCinema;
import repertuar.model.multikino.Multikino;
import repertuar.model.multikino.MultikinoCinema;
import repertuar.service.api.ChainService;
import repertuar.service.impl.CinemaCityService;
import repertuar.service.impl.HeliosService;
import repertuar.service.impl.MultikinoService;

import java.util.Optional;

public class ServiceFactory {

    private static final MultikinoService MULTIKINO_SERVICE = new MultikinoService();
    private static final CinemaCityService CINEMA_CITY_SERVICE = new CinemaCityService();
    private static final HeliosService HELIOS_SERVICE = new HeliosService();

    public static Optional<ChainService> getService(Chain c) {
        if (c instanceof Multikino) {
            return Optional.of(MULTIKINO_SERVICE);
        } else if (c instanceof CinemaCity) {
            return Optional.of(CINEMA_CITY_SERVICE);
        } else if (c instanceof Helios) {
            return Optional.of(HELIOS_SERVICE);
        }
        return Optional.empty();
    }

    public static Optional<ChainService> getService(Cinema c) {
        if (c instanceof MultikinoCinema) {
            return Optional.of(MULTIKINO_SERVICE);
        } else if (c instanceof CinemaCityCinema) {
            return Optional.of(CINEMA_CITY_SERVICE);
        } else if (c instanceof HeliosCinema) {
            return Optional.of(HELIOS_SERVICE);
        }
        return Optional.empty();
    }

}
