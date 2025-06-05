/**
 * This script checks the integrity of the environment needed to execute this software.
 */
package com.panama_hitek;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import javax.swing.SwingUtilities;

public class EnvironmentChecker {

    private static final String PYTHON_FOLDER_PREFIX = "python/";
    private static final String EXTRACTION_MARKER = ".python_extracted";

    public static void checkEnvironment() {
        final JFrameProgress progressDialog = new JFrameProgress();
        // Show the progress dialog on the EDT
        SwingUtilities.invokeLater(() -> progressDialog.setVisible(true));
        try {
            System.out.println("Checking for resources in the JAR...");
            // Check resources root in classpath
            checkClasspathResources();
            try {
                URL codeSourceUrl = EnvironmentChecker.class.getProtectionDomain().getCodeSource().getLocation();
                System.out.println("Code source URL: " + codeSourceUrl);

                File jarFile = getJarFile(codeSourceUrl);
                System.out.println("JAR file path: " + jarFile.getAbsolutePath());

                if (jarFile.getName().endsWith(".jar")) {
                    System.out.println("Running from JAR: " + jarFile.getAbsolutePath());

                    // Get the directory where the JAR is located
                    File outputDir = jarFile.getParentFile();
                    System.out.println("Python files directory: " + outputDir.getAbsolutePath());

                    // Check if Python environment is already set up properly
                    boolean extractionPerformed = false;
                    if (isPythonEnvironmentReady(outputDir)) {
                        System.out.println("Python environment is already set up properly. Skipping extraction.");
                    } else {
                        System.out.println("Python environment check failed. Proceeding with extraction.");
                        // Extract Python files from JAR
                        extractPythonFiles(jarFile, outputDir);
                        extractionPerformed = true;
                    }

                    // List resources in JAR for debugging only if extraction was performed
                    if (extractionPerformed) {
                        listJarResources(jarFile);
                    }
                } else {
                    System.out.println("Not running from a JAR file. Running in development environment.");
                }
            } catch (Exception e) {
                System.out.println("Unexpected error: " + e.getMessage());
                e.printStackTrace();
                System.exit(1);
            }
        } finally {
            // Dispose the progress dialog on the EDT
            SwingUtilities.invokeLater(progressDialog::dispose);
        }
    }

    /**
     * Check if the Python environment is already properly set up by trying to run a test script
     */
    private static boolean isPythonEnvironmentReady(File baseDir) {
        try {
            File pythonExe = new File(baseDir, "python/venv/Scripts/python.exe");
            File checkupScript = new File(baseDir, "python/environment_checkup.py");

            if (!pythonExe.exists() || !checkupScript.exists()) {
                System.out.println("Python executable or checkup script not found.");
                System.exit(1);
            }

            // Create process to run Python check script
            ProcessBuilder processBuilder = new ProcessBuilder(
                pythonExe.getAbsolutePath(),
                checkupScript.getAbsolutePath()
            );

            // Set working directory to base directory
            processBuilder.directory(baseDir);

            // Redirect output and error
            processBuilder.redirectErrorStream(true);

            System.out.println("Running Python environment check script...");
            Process process = processBuilder.start();

            // Read and display output
            boolean okFound = false;
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()))) {

                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println("Python check: " + line);
                    if (line.equals("OK")) {
                        okFound = true;
                    }
                }
            }

            // Wait for process to complete and get exit code
            int exitCode = process.waitFor();
            System.out.println("Python check script exited with code: " + exitCode);
            if (!(okFound && (exitCode == 0))) {
                System.out.println("Python environment check failed.");
                System.exit(1);
            }
            return true;
        } catch (IOException | InterruptedException e) {
            System.out.println("Failed to run Python environment check: " + e.getMessage());
            System.exit(1);
            return false; // Unreachable, but required for compilation
        }
    }

    private static void checkClasspathResources() {
        URL resourcesUrl = EnvironmentChecker.class.getClassLoader().getResource("");
        if (resourcesUrl != null) {
            System.out.println("Resources root at: " + resourcesUrl);
        } else {
            System.out.println("Resources root not found in classpath.");
            // Attempt to extract Python files from JAR
            try {
                URL codeSourceUrl = EnvironmentChecker.class.getProtectionDomain().getCodeSource().getLocation();
                File jarFile = getJarFile(codeSourceUrl);
                File outputDir = jarFile.getParentFile();
                extractPythonFiles(jarFile, outputDir);
            } catch (Exception e) {
                System.out.println("Failed to extract Python files from JAR: " + e.getMessage());
                e.printStackTrace();
                System.exit(1);
            }
        }
    }

    private static File getJarFile(URL codeSourceUrl) throws URISyntaxException {
        try {
            return new File(new URI(codeSourceUrl.toString()));
        } catch (URISyntaxException e) {
            // Fallback method if URI conversion fails
            File file = new File(codeSourceUrl.getPath());
            if (!file.exists()) {
                System.out.println("Could not resolve JAR file path.");
                System.exit(1);
            }
            return file;
        }
    }

    private static void extractPythonFiles(File jarFile, File outputDir) {
        // Check if extraction was already done
        File markerFile = new File(outputDir, EXTRACTION_MARKER);

        if (markerFile.exists()) {
            System.out.println("\nPython files were already extracted. Skipping extraction.");
            return;
        }

        try (JarFile jar = new JarFile(jarFile)) {
            System.out.println("\nExtracting Python resources from JAR:");
            Enumeration<JarEntry> entries = jar.entries();
            int extractedCount = 0;

            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                String entryName = entry.getName();

                // Check if it's a Python-related file we want to extract
                if (entryName.startsWith(PYTHON_FOLDER_PREFIX) && !entry.isDirectory()) {
                    System.out.println("Extracting: " + entryName);
                    extractFile(jar, entry, outputDir, entryName);
                    extractedCount++;
                }
            }

            System.out.println("\nExtracted " + extractedCount + " Python files to: " + outputDir.getAbsolutePath());

            // Create marker file to indicate extraction is complete
            try {
                markerFile.createNewFile();
                System.out.println("Created extraction marker file: " + markerFile.getName());
            } catch (IOException e) {
                System.out.println("Warning: Could not create marker file: " + e.getMessage());
            }
        } catch (IOException e) {
            System.out.println("Error extracting files from JAR: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static void extractFile(JarFile jar, JarEntry entry, File outputDir, String entryName) throws IOException {
        // Create the target file with directories
        File targetFile = new File(outputDir, entryName);

        // Ensure parent directories exist
        File parent = targetFile.getParentFile();
        if (!parent.exists() && !parent.mkdirs()) {
            throw new IOException("Failed to create directory " + parent);
        }

        // Extract the file
        try (InputStream is = jar.getInputStream(entry);
             FileOutputStream fos = new FileOutputStream(targetFile)) {

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
            }
            fos.flush();
        } catch (IOException e) {
            System.out.println("Failed to extract file: " + entryName + " - " + e.getMessage());
            System.exit(1);
        }
    }

    private static void listJarResources(File jarFile) {
        try (JarFile jar = new JarFile(jarFile)) {
            System.out.println("\nListing resources in JAR:");
            Enumeration<JarEntry> entries = jar.entries();
            boolean foundResources = false;
            int resourceCount = 0;

            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                String entryName = entry.getName();

                if (entryName.startsWith("src/main/resources/") ||
                    // Also check for resources directly at the root level (typical for Maven)
                    (!entryName.startsWith("META-INF/") &&
                     !entryName.startsWith("com/") &&
                     !entry.isDirectory())) {


                    foundResources = true;
                    resourceCount++;
                }
            }

            System.out.println("Total resources found: " + resourceCount);

            if (!foundResources) {
                System.out.println("No resources found with src/main/resources path.");
                System.out.println("\nNote: Maven typically places resources directly at the root of the JAR, not in src/main/resources/");
            }
        } catch (IOException e) {
            System.out.println("Error reading JAR file: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}