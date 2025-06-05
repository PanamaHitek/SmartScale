package com.panama_hitek;

import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import java.awt.BorderLayout;
import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A JPanel that displays a real-time chart of weight measurements over time.
 */
public class WeightChartPanel extends JPanel {
    
    private TimeSeries series;
    private TimeSeriesCollection dataset;
    private JFreeChart chart;
    private int maxDataPoints = 100; // Maximum number of data points to prevent memory issues
    
    public WeightChartPanel() {
        super(new BorderLayout());
        
        // Create dataset
        series = new TimeSeries("Weight");
        dataset = new TimeSeriesCollection(series);
        
        // Create chart
        chart = ChartFactory.createTimeSeriesChart(
                "Weight Measurements", // chart title
                "Time",               // x axis label
                "Weight (kg)",        // y axis label
                dataset,              // data
                true,                 // include legend
                true,                 // tooltips
                false                 // urls
        );
        
        // Customize chart
        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setDomainGridlinePaint(Color.LIGHT_GRAY);
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);
        
        // Set up the renderer
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesPaint(0, Color.BLUE);
        renderer.setSeriesShapesVisible(0, true);
        renderer.setSeriesLinesVisible(0, true);
        plot.setRenderer(renderer);
        
        // Format the time axis
        DateAxis dateAxis = (DateAxis) plot.getDomainAxis();
        dateAxis.setDateFormatOverride(new SimpleDateFormat("HH:mm:ss"));
        
        // Format the weight axis
        NumberAxis weightAxis = (NumberAxis) plot.getRangeAxis();
        weightAxis.setAutoRangeIncludesZero(false);
        
        // Add chart to panel
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(600, 270));
        chartPanel.setDomainZoomable(true);
        chartPanel.setRangeZoomable(true);
        
        add(chartPanel, BorderLayout.CENTER);
    }
    
    /**
     * Add a new data point to the chart
     * @param timestamp The timestamp in milliseconds
     * @param weight The weight measurement
     */
    public void addDataPoint(long timestamp, double weight) {
        series.addOrUpdate(new Millisecond(new Date(timestamp)), weight);
        
        // Limit the number of points to prevent memory issues
        while (series.getItemCount() > maxDataPoints) {
            series.delete(0, 0);
        }
    }
    
    /**
     * Add a new data point to the chart using current time
     * @param weight The weight measurement
     */
    public void addDataPoint(double weight) {
        addDataPoint(System.currentTimeMillis(), weight);
    }
    
    /**
     * Set the maximum number of data points to display
     * @param max Maximum number of data points
     */
    public void setMaxDataPoints(int max) {
        this.maxDataPoints = max;
    }
    
    /**
     * Clear all data points from the chart
     */
    public void clearChart() {
        series.clear();
    }
}
