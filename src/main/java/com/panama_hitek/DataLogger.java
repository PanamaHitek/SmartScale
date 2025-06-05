package com.panama_hitek;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JOptionPane;

/**
 * DataLogger class for storing and exporting weight measurements
 */
public class DataLogger {
    
    // Data class to hold weight measurements
    public static class WeightRecord {
        private String date;
        private String time;
        private double weight;
        
        public WeightRecord(String date, String time, double weight) {
            this.date = date;
            this.time = time;
            this.weight = weight;
        }
        
        // Getters
        public String getDate() { return date; }
        public String getTime() { return time; }
        public double getWeight() { return weight; }
        
        // For CSV formatting
        @Override
        public String toString() {
            return date + "," + time + "," + String.format("%.2f", weight);
        }
        
        public long getTimestampInMillis() {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                Date date = sdf.parse(this.date + " " + this.time);
                return date.getTime();
            } catch (Exception e) {
                return 0;
            }
        }
    }
    
    // Interface for data listeners
    public interface DataListener {
        void onNewData(long timestamp, double weight);
    }
    
    private List<WeightRecord> records = new ArrayList<>();
    private final List<DataListener> listeners = new ArrayList<>();
    
    /**
     * Constructor to initialize the records list
     */
    public DataLogger() {
        records = new ArrayList<>();
    }
    
    /**
     * Add a new weight measurement record
     * @param date Date of measurement
     * @param time Time of measurement
     * @param weight Weight value in kg
     */
    public void addRecord(String date, String time, double weight) {
        WeightRecord record = new WeightRecord(date, time, weight);
        records.add(record);
        
        // Notify listeners
        for (DataListener listener : listeners) {
            listener.onNewData(record.getTimestampInMillis(), weight);
        }
    }
    
    /**
     * Add a listener to be notified when new data arrives
     * @param listener The listener to add
     */
    public void addDataListener(DataListener listener) {
        listeners.add(listener);
    }
    
    /**
     * Remove a data listener
     * @param listener The listener to remove
     */
    public void removeDataListener(DataListener listener) {
        listeners.remove(listener);
    }
    
    /**
     * Clear all stored records
     */
    public void clearRecords() {
        records.clear();
    }
    
    /**
     * Get current records
     * @return Copy of the records list
     */
    public List<WeightRecord> getRecords() {
        return new ArrayList<>(records); // Return a copy to prevent modification
    }
    
    /**
     * Export data to CSV file
     * @param parent Parent component for dialog boxes
     * @return true if export was successful, false otherwise
     */
    public boolean exportToCSV(JFrame parent) {
        if (records.isEmpty()) {
            JOptionPane.showMessageDialog(parent, 
                    "No data to export", 
                    "Export Error", 
                    JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Weight Data");
        
        // Set default file name with timestamp
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String defaultFileName = "weight_data_" + formatter.format(new Date()) + ".csv";
        fileChooser.setSelectedFile(new File(defaultFileName));
        
        // Set filter for CSV files
        FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV Files (*.csv)", "csv");
        fileChooser.setFileFilter(filter);
        
        int userSelection = fileChooser.showSaveDialog(parent);
        
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            
            // Add .csv extension if not already present
            if (!fileToSave.getAbsolutePath().toLowerCase().endsWith(".csv")) {
                fileToSave = new File(fileToSave.getAbsolutePath() + ".csv");
            }
            
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileToSave))) {
                // Write header
                writer.write("Date,Time,Weight (kg)");
                writer.newLine();
                
                // Write data
                for (WeightRecord record : records) {
                    writer.write(record.toString());
                    writer.newLine();
                }
                
                JOptionPane.showMessageDialog(parent, 
                        "Data exported successfully to:\n" + fileToSave.getPath(), 
                        "Export Complete", 
                        JOptionPane.INFORMATION_MESSAGE);
                return true;
            } catch (IOException e) {
                JOptionPane.showMessageDialog(parent, 
                        "Error exporting data: " + e.getMessage(), 
                        "Export Error", 
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
                return false; // User canceled
    }
}
