package dhbw.cloudia.splitter.control.helper;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Helper class. Tuple holding two objects. Generified.
 * @param <T> first object type
 * @param <U> second object type
 */
@Data
@AllArgsConstructor
public class Tuple<T, U> {
    private T firstObject;
    private U secondObject;
}
