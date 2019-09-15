package fastcache;

public interface CacheLevel extends Cache {

    int capacity();

    int readTimeDelay();

    int writeTimeDelay();
}
