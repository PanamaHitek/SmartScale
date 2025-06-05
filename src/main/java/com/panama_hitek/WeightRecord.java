package com.panama_hitek;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Class representing a weight measurement record
 */
public class WeightRecord {
    private final String date;
    private final String time;
    private final double weight;
    
    /**
     * Create a new weight record
     * @param date Date string in format yyyy-MM-dd
     * @param time Time string in format HH:mm:ss
     * @param weight Weight measurement
     */
    public WeightRecord(String date, String time, double weight) {
        this.date = date;
        this.time = time;
        this.weight = weight;
    }
    
    /**
     * Get the date string
     * @return Date in format yyyy-MM-dd
     */
    public String getDate() {
        return date;
    }
    
    /**
     * Get the time string
     * @return Time in format HH:mm:ss
     */
    public String getTime() {
        return time;
    }
    
    /**
     * Get the weight value
     * @return Weight measurement
     */
    public double getWeight() {
        return weight;
    }
    
    /**
     * Get timestamp in milliseconds
     * @return Timestamp in milliseconds since epoch
     */
    public long getTimestampInMillis() {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date dateObj = format.parse(date + " " + time);
            return dateObj.getTime();
        } catch (ParseException e) {
            // If parsing fails, return current time
            return System.currentTimeMillis();
        }
    }
}
