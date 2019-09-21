package fastcache;

import java.util.List;

public interface CacheLevel extends Cache {

    int capacity();

    int readTimeDelay();

    int writeTimeDelay();

}
