package com.example;

import java.io.File;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Map;

import com.example.DigitalClock.TimeFormat;

public class Main {
    public static void main(String[] args) {
        String filePath = Main.class.getClassLoader().getResource("strefy.csv").getPath();
        Map<String, City> cityMap = City.parseFile(filePath);


        DigitalClock dc = new DigitalClock(cityMap.get("Warszawa"), TimeFormat.HOUR_24);
        dc.setTime(23, 0, 0);
        System.out.println(dc.toString());
        dc.setCity(cityMap.get("Kijów"));
        System.out.println(dc.toString());

        City lublin = cityMap.get("Lublin");
        System.out.println(lublin);
        System.out.println(lublin.localMeanTime(LocalTime.of(12,0,0)));

    }
}