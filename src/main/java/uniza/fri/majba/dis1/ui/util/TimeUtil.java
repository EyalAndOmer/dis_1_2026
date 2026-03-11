package uniza.fri.majba.dis1.ui.util;

public class TimeUtil {
    private TimeUtil() {
        // Utility class
    }

    public static String formatElapsedDuration(long startTimeMillis) {
        long elapsed = System.currentTimeMillis() - startTimeMillis;
        long seconds = elapsed / 1000;
        if (seconds < 60) {
            return String.format("%.1fs", elapsed / 1000.0);
        }
        long minutes = seconds / 60;
        long secs = seconds % 60;
        if (minutes < 60) {
            return String.format("%dm %ds", minutes, secs);
        }
        long hours = minutes / 60;
        long mins = minutes % 60;
        return String.format("%dh %dm %ds", hours, mins, secs);
    }

    public static String formatDecimalHoursAsTimestamp(double hours) {
        long totalMillis = Math.round(hours * 3_600_000.0);
        long h = totalMillis / 3_600_000;
        long rem = totalMillis % 3_600_000;
        long m = rem / 60_000;
        rem = rem % 60_000;
        long s = rem / 1000;
        long ms = rem % 1000;
        return String.format("%d:%02d:%02d.%03d", h, m, s, ms);
    }
}
