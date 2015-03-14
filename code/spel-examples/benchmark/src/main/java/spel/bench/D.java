package spel.bench;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Thomas Darimont
 */
public class D {
    AtomicInteger state = new AtomicInteger();

    public int someMethod() {
        return state.incrementAndGet();
    }
}
