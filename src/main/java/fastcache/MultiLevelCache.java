package fastcache;

import java.util.ArrayList;
import java.util.List;

public class MultiLevelCache implements Cache {

    private final List<CacheLevel> levels;

    public MultiLevelCache(int[] capacities, int[] readDelay, int[] writeDelay) {

        this.levels = new ArrayList<>();

        for (int i = 0; i < capacities.length; i++) {
            this.levels.add(new CacheLevelImpl(capacities[i], readDelay[i], writeDelay[i]));
        }
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

        return totalWriteDelay;
    }
}
