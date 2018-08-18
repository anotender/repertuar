package repertuar.view.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import repertuar.model.Film;

@AllArgsConstructor
public class FilmChangeEvent {

    @Getter
    private final Film film;

}
