package program.boundary.console;

import java.time.format.DateTimeFormatter;

public final class DateTimeFormat {
    public final static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    public final static DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
    public final static DateTimeFormatter getDateFormatter(){
        return dateFormat;
    }
    public final static DateTimeFormatter getDateTimeFormatter(){
        return dateTimeFormat;
    }
}
