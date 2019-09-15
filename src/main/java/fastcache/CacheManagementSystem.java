package fastcache;

import java.io.IOException;
import java.util.Scanner;

public final class CacheManagementSystem {

    public static void main(final String[] args) throws IOException {

        final Scanner scanner = new Scanner(System.in);

        int level = scanner.nextInt();

        int[] capacities = new int[level];
        int[] readDelay  = new int[level];
        int[] writeDelay = new int[level];

        readParams(scanner, level, capacities);
        readParams(scanner, level, readDelay);
        readParams(scanner, level, writeDelay);

        final Cache cache = new MultiLevelCache(capacities, readDelay, writeDelay);

        while (!Thread.currentThread().isInterrupted()) {

            final String cmd = scanner.nextLine();
            if (cmd.startsWith("WRITE")) {
                // extract key, value
                final String[] writeCmd = cmd.split(" ", 3);
                // perform write
                final int writeTime = cache.set(writeCmd[1], writeCmd[2]);
                System.out.println("Write took " + writeTime + "ms");
            } else if (cmd.startsWith("READ")) {
                // extract key
                final String[] readCmd = cmd.split(" ", 2);
                // perform read
                final ReadResult readResult = cache.get(readCmd[1]);
                if (readResult.getValue() == null) {
                    System.err.println("Key not found!");
                } else {
                    System.out.println("Value=" + readResult.getValue() + ", found in " + readResult.getReadTimeDelay() + "ms!");
                }
            } else if (cmd.equalsIgnoreCase("quit")) {
                System.out.println("Bye!");
                break;
            } else {
                System.err.println("Unknown command, try again!");
            }
        }
    }

    private static void readParams(final Scanner scanner, final int level, final int[] param) {

        for (int i = 0; i < level; i++) {
            param[i] = scanner.nextInt();
        }
    }
}
