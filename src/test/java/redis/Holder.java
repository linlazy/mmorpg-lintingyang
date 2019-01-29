package redis;

import lombok.Data;

/**
 * @author linlazy
 */
@Data
public class Holder<T> {

    private T value;
}
