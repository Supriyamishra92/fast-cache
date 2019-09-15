package fastcache;

import java.util.LinkedHashMap;
import java.util.Map;

public class CacheLevelImpl extends LinkedHashMap<String, String> implements CacheLevel {

    private final int capacity;
    private final int readDelay;
    private final int writeDelay;

    public CacheLevelImpl(final int cap, final int readDelay, final int writeDelay) {
        capacity = cap;
        this.readDelay = readDelay;
        this.writeDelay = writeDelay;
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
        return new ReadResult(super.get(key), readDelay);
    }

    @Override
    public int set(final String key, final String value) {
        put(key, value);
        return writeDelay;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<String, String> eldest) {

       return size() > capacity;
    }
}
