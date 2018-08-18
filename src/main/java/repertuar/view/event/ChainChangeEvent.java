package repertuar.view.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import repertuar.model.Chain;

@AllArgsConstructor
public class ChainChangeEvent {

    @Getter
    private final Chain chain;

}
