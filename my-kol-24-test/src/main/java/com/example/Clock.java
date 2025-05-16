package com.example;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public abstract class Clock {
    // K1:
    // Napisz klasę abstrakcyjną Clock przechowującą wewnętrznie stan zegara, posiadającą publiczne metody:
    protected LocalTime clockState = LocalTime.of(0, 0, 0);
    private City city;

    public Clock(City city) {
        this.city = city;
    }

    public void setCity(City city) {
        int currentTZ = this.city.getsummerTimeZone();
        int newTZ = city.getsummerTimeZone();
        int diffTZ = newTZ - currentTZ;
        this.clockState = this.clockState.plusHours(diffTZ);
        this.city = city;
    }

    // setCurrentTime, która ustawia czas zegara na bieżącą godzinę zgodnie z zegarem systemowym.
    public void setCurrentTime() {
        this.clockState = LocalTime.now();
    }
    
    // setTime, która przyjmuje trzy zmienne całkowite - godzinę, minutę i sekundę i ustawia zgodnie z nią czas 
    // zegara.  Jeżeli  dane  nie  są  poprawne  w  kontekście  zegara  24-godzinnego,  należy  rzucić  wyjątek 
    // IllegalArgumentException i opisać przyczynę w wiadomości tego wyjątku (która ze zmiennych nie mieści się 
    // w jakim zakresie).
    public void setTime(int hour, int minute, int second) {
        if (hour < 0 || hour > 23) {
            throw new IllegalArgumentException("Godzina jest poza zakresem 0-24: " + hour);
        }
        if (minute < 0 || minute > 59) {
            throw new IllegalArgumentException("Minuta jest poza zakresem 0-60: " + minute);    
        }
        if (second < 0 || second > 59) {
            throw new IllegalArgumentException("Sekunda jest poza zakresem 0-60: " + second);
        }
        this.clockState = LocalTime.of(hour, minute, second);        
    }

    // toString, która zwraca napis zawierający godzinę w formacie hh:mm:ss
    @Override
    public String toString() {
        //return String.format("%02d:%02d:%02d", this.clockState.getHour(), this.clockState.getMinute(), this.clockState.getSecond());
        DateTimeFormatter df =  DateTimeFormatter.ofPattern("HH:mm:ss");
        return df.format(this.clockState);
    }
}
