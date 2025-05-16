package com.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

public class City {
    private String name;
    private int summerTimeZone;
    private String latitude;
    private String longitude;

    public City(String name, int summerTimeZone, String latitude, String longitude) {
        this.name = name;
        this.summerTimeZone = summerTimeZone;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getName() {
        return this.name;
    }

    public int getsummerTimeZone() {
        return this.summerTimeZone;
    }

    public LocalTime localMeanTime(LocalTime zoneTime) {
        // Zamiana długości geograficznej na double
        String[] longitudeParts = this.longitude.trim().split(" ");
        System.out.println(longitudeParts[1]);
        double longitudeValue = Double.parseDouble(longitudeParts[0]);
        if (longitudeParts[1].equalsIgnoreCase("W")) {
            longitudeValue = -longitudeValue;
        }
        // Przesunięcie godzinowe: -180 -> -12, 0 -> 0, 180 -> 12
        double offsetHours = longitudeValue * 12.0 / 180.0;
        // Rozdziel na godziny, minuty, sekundy
        int hours = (int) offsetHours;
        int minutes = (int) ((Math.abs(offsetHours) * 60) % 60);
        int seconds = (int) ((Math.abs(offsetHours) * 3600) % 60);

        // Dodaj przesunięcie do zoneTime
        LocalTime localMean = zoneTime.plusHours(hours)
                                    .plusMinutes(offsetHours >= 0 ? minutes : -minutes)
                                    .plusSeconds(offsetHours >= 0 ? seconds : -seconds);
        return localMean;
}
    
    private static City parseLine(String line) {
        String[] lineParts = line.split(",");
        return new City(lineParts[0], Integer.parseInt(lineParts[1]), lineParts[2], lineParts[3]);        
    }

    public static Map<String, City> parseFile(String filePath) {
        Map<String, City> cityMap = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line = br.readLine();
            while ((line = br.readLine()) != null) {
                City city = parseLine(line);
                cityMap.put(city.getName(), city);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cityMap;   
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.name).append(";");
        sb.append(this.summerTimeZone).append(";");
        sb.append(this.latitude).append(";");
        sb.append(this.longitude);
        return sb.toString();
    }
}
