package repertuar.service.factory;

import repertuar.service.impl.CinemaCityService;
import repertuar.service.impl.HeliosService;
import repertuar.service.impl.MultikinoService;

public final class ServiceFactory {

    private static final MultikinoService MULTIKINO_SERVICE = new MultikinoService();
    private static final CinemaCityService CINEMA_CITY_SERVICE = new CinemaCityService();
    private static final HeliosService HELIOS_SERVICE = new HeliosService();

    private ServiceFactory() {
    }

    public static MultikinoService getMultikinoService() {
        return MULTIKINO_SERVICE;
    }

    public static CinemaCityService getCinemaCityService() {
        return CINEMA_CITY_SERVICE;
    }

    public static HeliosService getHeliosService() {
        return HELIOS_SERVICE;
    }
}
