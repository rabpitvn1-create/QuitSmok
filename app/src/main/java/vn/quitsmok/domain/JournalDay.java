package vn.quitsmok.domain;
import java.time.*;
public final class JournalDay {
    /** Zone and waking boundary are frozen per installation in this first slice. */
    public static LocalDate at(Instant instant, ZoneId zone, LocalTime wake) {
        ZonedDateTime local = instant.atZone(zone);
        return local.toLocalTime().isBefore(wake) ? local.toLocalDate().minusDays(1) : local.toLocalDate();
    }
    private JournalDay() {}
}
