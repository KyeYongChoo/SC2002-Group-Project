package program.boundary.console;

import java.time.format.DateTimeFormatter;

/**
 * <p>
 * The {@code DateTimeFormat} class provides common instances of {@link java.time.format.DateTimeFormatter}
 * for consistent date and time formatting throughout the application. This utility class ensures
 * that all date and time values are displayed in a standardized manner, promoting uniformity and
 * reducing the risk of formatting errors.
 * </p>
 *
 * <p>
 * The class defines two static final {@code DateTimeFormatter} instances: one for formatting
 * dates only and another for formatting both dates and times. This standardization allows for
 * easy reuse of formatters across different parts of the application, enhancing code reusability
 * and simplifying maintenance. If a change to the date or time format is required, it can be
 * made in one place, and the change will be reflected wherever the formatters are used.
 * </p>
 */
public final class DateTimeFormat {

    /**
     * The formatter for dates in the pattern {@code dd-MM-yyyy}.
     */
    public static final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    /**
     * The formatter for date and time in the pattern {@code dd-MM-yyyy HH:mm}.
     */
    public static final DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

    /**
     * <p>
     * Returns the {@link java.time.format.DateTimeFormatter} used for formatting dates only.
     * This method provides access to the standardized date-only formatter, ensuring consistent
     * date representation throughout the application.
     * </p>
     *
     * @return the date-only formatter
     */
    public static final DateTimeFormatter getDateFormatter() {
        return dateFormat;
    }

    /**
     * <p>
     * Returns the {@link java.time.format.DateTimeFormatter} used for formatting both date and time.
     * This method provides access to the standardized date-time formatter, ensuring consistent
     * date and time representation throughout the application.
     * </p>
     *
     * @return the date-time formatter
     */
    public static final DateTimeFormatter getDateTimeFormatter() {
        return dateTimeFormat;
    }
}