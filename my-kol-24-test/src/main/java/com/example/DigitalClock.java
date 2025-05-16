package com.example;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Locale;

public class DigitalClock extends Clock {
    // W klasie DigitalClock 
    // utwórz  publiczny  typ wyliczeniowy pozwalający na rozróżnienie między 
    // zegarem  24-godzinnym  i  12-godzinnym.
    public enum TimeFormat {
        HOUR_12,
        HOUR_24
    }

    private TimeFormat timeFormat;

    public DigitalClock(City city, TimeFormat timeFormat) {
        super(city);
        this.timeFormat = timeFormat;
    }

    @Override
    public String toString() {
        if (this.timeFormat == TimeFormat.HOUR_24) {
            return super.toString();
        }           
        DateTimeFormatter df =  DateTimeFormatter.ofPattern("h:mm:ss a");
        return df.format(this.clockState);
    }    
}
