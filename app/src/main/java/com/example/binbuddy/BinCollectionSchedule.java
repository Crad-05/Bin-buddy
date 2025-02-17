package com.example.binbuddy;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class BinCollectionSchedule {
    private final Map<String, LocalDate> collectionDates;
    private static DateTimeFormatter dateFormatter = null;

    public BinCollectionSchedule() {
        this.collectionDates = new HashMap<>();
        this.dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yy");

        // Hardcoded bin collection start dates for 2025
        collectionDates.put("Black Bin", LocalDate.of(2025, 1, 2));
        collectionDates.put("Blue Bin", LocalDate.of(2025, 1, 8));
        collectionDates.put("Brown Bin", LocalDate.of(2025, 1, 3));
    }

    public Map<String, String> getNextBinCollectionDates(LocalDate currentDate) {
        if (currentDate == null) {
            currentDate = LocalDate.now();
        }

        Map<String, String> nextDates = new HashMap<>();

        for (Map.Entry<String, LocalDate> entry : collectionDates.entrySet()) {
            LocalDate collectionDate = entry.getValue();
            int interval = entry.getKey().equals("Brown Bin") ? 7 : 14; // Brown bin updates every 7 days

            while (collectionDate.isBefore(currentDate)) {
                collectionDate = collectionDate.plusDays(interval);
            }
            nextDates.put(entry.getKey(), collectionDate.format(dateFormatter));
        }

        return nextDates;
    }

    public boolean isValidDate(String dateStr) {
        try {
            LocalDate.parse(dateStr, dateFormatter);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static LocalDate parseDate(String dateStr) {
        try {
            return LocalDate.parse(dateStr, dateFormatter);
        } catch (Exception e) {
            return null;
        }
    }
}
