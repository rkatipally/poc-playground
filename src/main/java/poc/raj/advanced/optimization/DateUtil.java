package poc.raj.advanced.optimization;

import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Date;
import java.util.Optional;


public class DateUtil {

    private static final String ATPCO_DATE_FORMAT = "ddMMMyy";

    public static Optional<LocalDate> parseToOptionalLocalDate(String dateStr, String pattern) {
        LocalDate date = null;

        try {
            if (!StringUtils.isEmpty(dateStr) && !StringUtils.isEmpty(pattern)) {
                DateTimeFormatter formatter = (new DateTimeFormatterBuilder()).parseCaseInsensitive().appendPattern(pattern).toFormatter();
                date = LocalDate.parse(dateStr, formatter);
            }
        } catch (Exception var4) {
            return Optional.empty();
        }

        return Optional.ofNullable(date);
    }

    public static String formatLocalDate(Optional<LocalDate> optionalDate, String pattern) {
        String formattedDate = "";
        if (optionalDate.isPresent()) {
            formattedDate = ((LocalDate)optionalDate.get()).format(DateTimeFormatter.ofPattern(pattern));
        }

        return formattedDate;
    }
    
    public static Date parseToDateFromStr(String dateStr) {
    	Optional<LocalDate> locDate = parseToOptionalLocalDate(dateStr, ATPCO_DATE_FORMAT);
    	if(locDate.isPresent()) {
    		return Date.from(locDate.get().atStartOfDay(ZoneId.systemDefault()).toInstant());
    	}
    	return null;
    }

    public static String formatLocalDate(LocalDate localDate, String pattern) {
        String formattedDate = "";
        if (localDate != null && StringUtils.isNotEmpty(pattern)) {
            formattedDate = localDate.format(DateTimeFormatter.ofPattern(pattern)).toUpperCase();
        }

        return formattedDate;
    }

    public static String formatLocalTime(LocalTime localTime, String pattern) {
        String formattedTime = "";
        if (localTime != null && StringUtils.isNotEmpty(pattern)) {
            formattedTime = localTime.format(DateTimeFormatter.ofPattern(pattern)).toUpperCase();
        }

        return formattedTime;
    }

    public static String formatLocalDate(LocalDate localDate) {
        return formatLocalDate(localDate, ATPCO_DATE_FORMAT);
    }
}

