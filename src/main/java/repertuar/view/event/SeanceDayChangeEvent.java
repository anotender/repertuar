package repertuar.view.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import repertuar.model.SeanceDay;

@AllArgsConstructor
public class SeanceDayChangeEvent {

    @Getter
    private final SeanceDay seanceDay;

}
