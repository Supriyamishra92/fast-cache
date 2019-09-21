package fastcache;

import java.util.LinkedList;

public class RingBuffer extends LinkedList<Integer> {

    private final int maxCapacity;

    public RingBuffer(int maxCapacity) {

        this.maxCapacity = maxCapacity;
    }

    @Override
    public boolean add(final Integer observation) {

        final boolean result = super.add(observation);

        if (result && size() > maxCapacity) {
            removeLast();
        }

        return result;
    }

    public float average() {
        return this.stream().reduce(0, (prev, next) -> prev + next) / (size() * 1.0f);
    }
}
