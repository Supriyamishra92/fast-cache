package fastcache;

import java.util.LinkedHashMap;
import java.util.Map;

public class CacheLevelImpl extends LinkedHashMap<String, String> implements CacheLevel {

    private final int capacity;
    private final int readDelay;
    private final int writeDelay;

    private final RingBuffer readTimes;
    private final RingBuffer writeTimes;

    public CacheLevelImpl(final int cap, final int readDelay, final int writeDelay) {
        capacity = cap;
        this.readDelay = readDelay;
        this.writeDelay = writeDelay;
        this.readTimes = new RingBuffer(10);
        this.writeTimes = new RingBuffer(10);
    }

    @Override
    public int capacity() {
        return capacity;
    }

    @Override
    public int readTimeDelay() {
        return readDelay;
    }

    @Override
    public int writeTimeDelay() {
        return writeDelay;
    }

    @Override
    public ReadResult get(final String key) {

        readTimes.add(readDelay);
        return new ReadResult(super.get(key), readDelay);
    }

    @Override
    public int set(final String key, final String value) {
        put(key, value);
        writeTimes.add(readDelay);
        return writeDelay;
    }

    @Override
    public Stats stats() {

        return new Stats(readTimes.average(), writeTimes.average(), 1f - (size() / (capacity * 1.0f)));
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<String, String> eldest) {

       return size() > capacity;
    }
}
