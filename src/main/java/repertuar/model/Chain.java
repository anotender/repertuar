package repertuar.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import repertuar.service.api.ChainService;
import repertuar.service.impl.CinemaCityService;
import repertuar.service.impl.HeliosService;
import repertuar.service.impl.MultikinoService;

import java.io.IOException;
import java.util.List;

@AllArgsConstructor
public enum Chain {

    HELIOS("Helios", "http://helios.pl", HeliosService.getInstance()),
    MULTIKINO("Multikino", "https://multikino.pl", MultikinoService.getInstance()),
    CINEMA_CITY("Cinema City", "https://www.cinema-city.pl", CinemaCityService.getInstance());

    @Getter
    private final String name;

    @Getter
    private final String baseUrl;

    @Getter
    private final ChainService service;

    public List<Cinema> getCinemas() throws IOException {
        return service.getCinemas();
    }

    @Override
    public String toString() {
        return name;
    }
}
