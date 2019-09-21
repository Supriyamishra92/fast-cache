package fastcache;

import java.util.ArrayList;
import java.util.List;

public class MultiLevelCache implements Cache {

    private final List<CacheLevel> levels;
    private final RingBuffer readTimes;
    private final RingBuffer writeTimes;

    public MultiLevelCache(int[] capacities, int[] readDelay, int[] writeDelay) {

        this.levels = new ArrayList<>();
        for (int i = 0; i < capacities.length; i++) {
            this.levels.add(new CacheLevelImpl(capacities[i], readDelay[i], writeDelay[i]));
        }
        this.readTimes = new RingBuffer(10);
        this.writeTimes = new RingBuffer(10);
    }

    @Override
    public ReadResult get(final String key) {

        int totalTimeDelay = 0;
        String value = null;
        for (int i = 0; i < levels.size(); i++) {
            final ReadResult readResult = levels.get(i).get(key);
            totalTimeDelay += readResult.getReadTimeDelay();
            value = readResult.getValue();

            if (value != null) {
                // back propagate
                for (int j = i - 1; j >= 0; j--) {
                    totalTimeDelay += levels.get(j).set(key, value);
                }
                break;
            }
        }
        readTimes.add(totalTimeDelay);
        return new ReadResult(value, totalTimeDelay);
    }

    @Override
    public int set(final String key, final String value) {

        int totalWriteDelay = 0;
        for (final CacheLevel level : levels) {
            final ReadResult result = level.get(key);
            totalWriteDelay += result.getReadTimeDelay();

            if (result.getValue() == null) {
                totalWriteDelay += level.set(key, value);
            } else {
                break;
            }
        }

        writeTimes.add(totalWriteDelay);

        return totalWriteDelay;
    }
    @Override
    public Stats stats() {

        final Float totalAvailable = levels.stream().map(Cache::stats).map(Stats::getAvailableCapacity).reduce(0f, (prev, next) -> prev + next);

        return new Stats(readTimes.average(), writeTimes.average(), totalAvailable / levels.size());
    }



}
