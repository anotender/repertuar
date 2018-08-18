package repertuar.view.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import repertuar.model.Cinema;

@AllArgsConstructor
public class CinemaChangeEvent {

    @Getter
    private final Cinema cinema;

}
