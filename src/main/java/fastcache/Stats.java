package fastcache;

public class Stats {

    private final float avgReadTime;

    private final float avgWriteTime;

    private final float availableCapacity;

    public Stats(final float avgReadTime, final float avgWriteTime, final float availableCapacity) {

        this.avgReadTime = avgReadTime;
        this.avgWriteTime = avgWriteTime;
        this.availableCapacity = availableCapacity;
    }

    public float getAvgReadTime() {
        return avgReadTime;
    }

    public float getAvgWriteTime() {
        return avgWriteTime;
    }

    public float getAvailableCapacity() {
        return availableCapacity;
    }

    @Override
    public String toString() {
        return String.format("avgReadTime: %2f, avgWriteTime: %2f, availableCapacity: %2f", avgReadTime, avgWriteTime, availableCapacity);
    }
}
