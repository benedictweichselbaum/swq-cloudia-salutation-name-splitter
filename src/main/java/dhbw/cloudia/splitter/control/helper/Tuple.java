package dhbw.cloudia.splitter.control.helper;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Tuple<T, U> {
    private T firstObject;
    private U secondObject;
}
