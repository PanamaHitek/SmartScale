package com.panama_hitek;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.swing.JOptionPane;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

public class JFrameWindow extends javax.swing.JFrame {

    String scriptPath = "python/scan_ble.py";

    JProgressBar jProgressBar1 = new JProgressBar();
    
    // Add these missing class variables
    private Process pythonProcess;
    private SwingWorker<Void, String> dataWorker;
    private final AtomicBoolean connectionActive = new AtomicBoolean(false);
    private ScheduledExecutorService timeoutChecker;
    private long lastDataTimestamp = 0;
    // Add DataLogger instance
    private DataLogger dataLogger;

    public JFrameWindow() {
        initComponents();
        ((DefaultTableModel)jTable1.getModel()).setRowCount(0);
        jProgressBar1 = new javax.swing.JProgressBar();
        jProgressBar1.setIndeterminate(true);
        jProgressBar1.setVisible(false);  // Hidden by default
        
        // Initialize the data logger
        dataLogger = new DataLogger();
        
        // Set row height to 20 pixels
        jTable1.setRowHeight(20);
    }

    public String runBleScanScript() {
        try {

            Path basePath = Paths.get(JFrameWindow.class
                    .getProtectionDomain()
                    .getCodeSource()
                    .getLocation()
                    .toURI())
                    .getParent();

            Path scriptPath = basePath.resolve("python").resolve("scan_ble.py");

            if (!Files.exists(scriptPath)) {
                return "{\"found\": false, \"error\": \"scan_ble.py not found at: " + scriptPath.toString() + "\"}";
            }

            ProcessBuilder builder = new ProcessBuilder("python", scriptPath.toString());
            builder.redirectErrorStream(true);
            Process process = builder.start();

            StringBuilder output = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line);
                }
            }

            int exitCode = process.waitFor();
            if (exitCode != 0) {
                return "{\"found\": false, \"error\": \"Python script exited with code " + exitCode + "\"}";
            }

            return output.toString().trim();

        } catch (Exception e) {
            return "{\"found\": false, \"error\": \"" + e.getMessage().replace("\"", "'") + "\"}";
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        jComboBox1 = new javax.swing.JComboBox<>();
        jButton2 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("SmartScale v1.0.0");

        jButton1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jButton1.setText("Scan BLE Devices");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jComboBox1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jComboBox1.setEnabled(false);

        jButton2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jButton2.setText("Connect");
        jButton2.setEnabled(false);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jTable1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Date", "Time", "Weight"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTable1);
        if (jTable1.getColumnModel().getColumnCount() > 0) {
            jTable1.getColumnModel().getColumn(0).setResizable(false);
            jTable1.getColumnModel().getColumn(1).setResizable(false);
            jTable1.getColumnModel().getColumn(2).setResizable(false);
        }

        jButton4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jButton4.setText("Clean Log");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jButton5.setText("Live Chart");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton6.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jButton6.setText("Export Log");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jButton1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 6, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jComboBox1)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
    if (jButton2.getText().equals("Connect")) {
         ((DefaultTableModel)jTable1.getModel()).setRowCount(0);
   jButton2.setText("Stop");
  
   
        // Extract MAC address from selected item
        String selectedDevice = jComboBox1.getSelectedItem().toString();
        // Format is typically "DeviceName - MAC_ADDRESS"
        int macIndex = selectedDevice.lastIndexOf(" - ");
        if (macIndex == -1) {
            JOptionPane.showMessageDialog(this, "Invalid device selection", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        final String macAddress = selectedDevice.substring(macIndex + 3);
        System.out.println("Connecting to: " + macAddress);
        
        // Change button text and set connection state
        jButton2.setText("Stop");
        connectionActive.set(true);
        
        // Start a worker thread to manage the Python process
        dataWorker = new SwingWorker<Void, String>() {
            @Override
            protected Void doInBackground() throws Exception {
                try {
                    Path basePath = Paths.get(JFrameWindow.class
                            .getProtectionDomain()
                            .getCodeSource()
                            .getLocation()
                            .toURI())
                            .getParent();
                    
                    // Use virtual environment Python executable
                    Path pythonExePath = basePath.resolve("python").resolve("venv").resolve("Scripts").resolve("python.exe");
                    Path scriptPath = basePath.resolve("python").resolve("retrieve_data.py");
                    
                    // Diagnostics
                    System.out.println("Base path: " + basePath);
                    System.out.println("Python executable: " + pythonExePath);
                    System.out.println("Script path: " + scriptPath);
                    System.out.println("Python exe exists: " + Files.exists(pythonExePath));
                    System.out.println("Script exists: " + Files.exists(scriptPath));
                    
                    if (!Files.exists(scriptPath)) {
                        publish("ERROR: retrieve_data.py not found at: " + scriptPath.toString());
                        return null;
                    }
                    
                    if (!Files.exists(pythonExePath)) {
                        // Fall back to system Python if venv not found
                        System.out.println("Python virtual environment not found, falling back to system Python");
                        pythonExePath = Paths.get("python");
                    }
                    
                    // Start the Python process with unbuffered output (-u flag)
                    String[] command = {
                        pythonExePath.toString(),
                        "-u",
                        scriptPath.toString(),
                        macAddress
                    };
                    
                    System.out.println("Command: " + String.join(" ", command));
                    
                    ProcessBuilder builder = new ProcessBuilder(command);
                    builder.redirectErrorStream(true);
                    
                    System.out.println("Starting Python process...");
                    pythonProcess = builder.start();
                    System.out.println("Python process started");
                    
                    // Set initial timestamp
                    lastDataTimestamp = System.currentTimeMillis();
                    
                    // Start timeout checker
                    timeoutChecker = Executors.newSingleThreadScheduledExecutor();
                    timeoutChecker.scheduleAtFixedRate(() -> {
                        if (connectionActive.get() && System.currentTimeMillis() - lastDataTimestamp > 5000) {
                            // Connection timed out
                            connectionActive.set(false);
                            System.out.println("Connection timed out - no data received for 5 seconds");
                            SwingUtilities.invokeLater(() -> {
                                handleConnectionLost("Connection timeout - no data received for 5 seconds");
                            });
                            timeoutChecker.shutdown();
                        }
                    }, 1, 1, TimeUnit.SECONDS);
                    
                    // Read data continuously
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(pythonProcess.getInputStream()))) {
                        String line;
                        System.out.println("Starting to read Python output...");
                        while (connectionActive.get() && (line = reader.readLine()) != null) {
                            // Update timestamp when data is received
                            lastDataTimestamp = System.currentTimeMillis();
                            System.out.println("Raw data from Python: " + line);
                            publish(line);
                        }
                        System.out.println("Python output reader loop ended");
                    }
                    
                    // Check exit code when process completes
                    if (pythonProcess != null) {
                        try {
                            int exitCode = pythonProcess.waitFor();
                            System.out.println("Python process exited with code: " + exitCode);
                            if (exitCode != 0 && connectionActive.get()) {
                                publish("ERROR: Python script exited with code " + exitCode);
                            }
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                    
                } catch (Exception e) {
                    e.printStackTrace();
                    publish("ERROR: " + e.getMessage());
                }
                return null;
            }
            
            @Override
            protected void process(java.util.List<String> chunks) {
                // Process received data here
                for (String data : chunks) {
                    // Removed console printing
                    
                    if (data.startsWith("ERROR:")) {
                        // Only print errors to the console
                        System.err.println(data);
                        // Handle error if needed
                    } else {
                        // Process the data and update the UI
                        try {
                            JSONObject jsonData = new JSONObject(data);
                            long timestamp = jsonData.getLong("timestamp");
                            double weight = jsonData.getDouble("weight");
                            
                            // Convert Unix timestamp to Date
                            java.util.Date date = new java.util.Date(timestamp * 1000L);
                            
                            // Format the date and time
                            java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd");
                            java.text.SimpleDateFormat timeFormat = new java.text.SimpleDateFormat("HH:mm:ss");
                            String dateStr = dateFormat.format(date);
                            String timeStr = timeFormat.format(date);
                            
                            // Format weight to 2 decimal places
                            String weightStr = String.format("%.2f", weight);
                            
                            // Get the table model
                            DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
                            
                            // Add a new row to the table
                            model.addRow(new Object[]{dateStr, timeStr, weightStr});
                            
                            // Add to the logger
                            dataLogger.addRecord(dateStr, timeStr, weight);
                            
                            // Make sure the table scrolls to show the last row
                            int lastRow = jTable1.getRowCount() - 1;
                            if (lastRow >= 0) {
                                jTable1.scrollRectToVisible(jTable1.getCellRect(lastRow, 0, true));
                                // Ensure selection is on the last row to keep focus there
                                jTable1.setRowSelectionInterval(lastRow, lastRow);
                            }
                        } catch (Exception e) {
                            System.err.println("Error parsing JSON data: " + e.getMessage());
                            e.printStackTrace();
                        }
                    }
                }
            }
            
            @Override
            protected void done() {
                if (!connectionActive.get()) {
                    // Already handled elsewhere
                    return;
                }
                
                // Process completed normally or with error
                try {
                    get(); // Will throw an exception if the background task failed
                } catch (Exception e) {
                    System.err.println("Error in data worker: " + e.getMessage());
                    handleConnectionLost("Connection error: " + e.getMessage());
                }
            }
        };
        dataWorker.execute();
        
   
   
    } else {
        // Stop the connection
        stopConnection();
        jButton2.setText("Connect");
    }
}//GEN-LAST:event_jButton2ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        ((DefaultTableModel)jTable1.getModel()).setRowCount(0);
        // Also clear the logger data
        dataLogger.clearRecords();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
    JFrameLiveChart chartFrame = new JFrameLiveChart();
    chartFrame.setDataLogger(dataLogger);
    chartFrame.setVisible(true);
    
    // If there's an active connection, create a listener to update the chart in real-time
    if (connectionActive.get()) {
        dataLogger.addDataListener((timestamp, weight) -> {
            chartFrame.addDataPoint(timestamp, weight);
        });
    }
}//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
         dataLogger.exportToCSV(this);
    }//GEN-LAST:event_jButton6ActionPerformed

/**
 * Stops the current connection and cleans up resources
 */
private void stopConnection() {
    connectionActive.set(false);
    
    // Cancel the timeout checker
    if (timeoutChecker != null && !timeoutChecker.isShutdown()) {
        timeoutChecker.shutdown();
    }
    
    // Cancel the data worker
    if (dataWorker != null && !dataWorker.isDone()) {
        dataWorker.cancel(true);
    }
    
    // Stop the Python process
    if (pythonProcess != null && pythonProcess.isAlive()) {
        pythonProcess.destroy();
        try {
            // Wait briefly for the process to terminate
            if (!pythonProcess.waitFor(2, TimeUnit.SECONDS)) {
                pythonProcess.destroyForcibly();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

/**
 * Handles a lost connection
 */
private void handleConnectionLost(String reason) {
    stopConnection();
    jButton2.setText("Connect");
    JOptionPane.showMessageDialog(this, 
            "Connection lost!\n" + reason,
            "Connection Error", 
            JOptionPane.WARNING_MESSAGE);
}

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {                                         
        // Create the progress dialog with this frame as the parent
        final JFrameProgress progress = new JFrameProgress(this);
        progress.setLocationRelativeTo(this); // Center on parent window
        
        // Clear previous entries in the combo box
        jComboBox1.removeAllItems();
        jComboBox1.setEnabled(false);
        jButton2.setEnabled(false);
        
        // Create a worker to do the BLE scanning in the background
        SwingWorker<String, Void> worker = new SwingWorker<String, Void>() {
            @Override
            protected String doInBackground() throws Exception {
                return runBleScanScript();
            }
            
            @Override
            protected void done() {
                if (!isCancelled()) {
                    try {
                        String result = get();
                        System.out.println("Scan result: " + result);
                        
                        // Parse the JSON result
                        JSONObject jsonResult = new JSONObject(result);
                        
                        if (jsonResult.getBoolean("found")) {
                            JSONArray devices = jsonResult.getJSONArray("devices");
                            
                            // If devices were found
                            if (devices.length() > 0) {
                                // Add each device to the combo box
                                for (int i = 0; i < devices.length(); i++) {
                                    JSONObject device = devices.getJSONObject(i);
                                    String name = device.getString("name");
                                    String address = device.getString("address");
                                    
                                    // Format: "SC02 - 50:FB:19:8A:A0:C2"
                                    String displayText = name + " - " + address;
                                    jComboBox1.addItem(displayText);
                                }
                                
                                // Enable the combo box and connect button
                                jComboBox1.setEnabled(true);
                                jButton2.setEnabled(true);
                                jComboBox1.setSelectedIndex(0);  // Select first item
                            } else {
                                // No devices found
                                jComboBox1.addItem("No devices found");
                            }
                        } else {
                            // Error occurred
                            String errorMsg = jsonResult.has("error") ? 
                                    jsonResult.getString("error") : "Unknown error";
                            jComboBox1.addItem("Error: " + errorMsg);
                        }
                        
                    } catch (Exception e) {
                        e.printStackTrace();
                        jComboBox1.addItem("Error parsing results");
                    }
                }
                // Close the progress dialog when done
                progress.dispose();
            }
        };
        
        // Link the worker to the progress dialog for cancellation
        progress.setWorker(worker);
        
        // Start the worker and show the progress dialog
        worker.execute();
        progress.setVisible(true);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {

        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(JFrameWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JFrameWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JFrameWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JFrameWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new JFrameWindow().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
