package fastcache;

public interface Cache {

    ReadResult get(String key);

    int set(String key, String value);

}
