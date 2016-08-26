package ru.cft.chuldrenofcorn.cornchat.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * User: azhukov
 * Date: 27.08.2016
 * Time: 0:19
 */
public final class DateUtils {

    private DateUtils() {
    }

    public static final int DURATION_YEAR = 365;
    public static final int DURATION_DAY = 1;

    public static String getSendMessageDate(final long millis) {
        long now = System.currentTimeMillis();
        long diff = now - millis;
        String format = "HH:mm";
        if (diff > TimeUnit.DAYS.toMillis(DURATION_YEAR)) {
            format = "MMM d yyyy, HH:mm";
        } else if (diff > TimeUnit.DAYS.toMillis(DURATION_DAY)) {
            format = "MMM d, HH:mm";
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.getDefault());
        return simpleDateFormat.format(new Date(millis));
    }

}
