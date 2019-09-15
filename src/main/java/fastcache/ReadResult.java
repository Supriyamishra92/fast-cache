package fastcache;

public class ReadResult {

    private final String value;
    private final int readTimeDelay;

    public ReadResult(String value, int readTimeDelay) {
        this.value = value;
        this.readTimeDelay = readTimeDelay;
    }

    public String getValue() {
        return value;
    }

    public int getReadTimeDelay() {
        return readTimeDelay;
    }
}
