package com.framework.utility;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.json.Json;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class AllureConfig {

    private static File resultDir = new File("allure-results");
    private static File resultHistoryDir = new File("allure-results" + File.separator + "history");
    private static File reportHistoryDir = new File("allure-report" + File.separator + "history");

    private static final String PATH = resultDir + File.separator + "%s";

    /**
     * Configures the environment by creating necessary files and directories.
     * This method invokes the creation of an environment properties file,
     * a copy of the report history, and an executor JSON file.
     *
     * @throws IOException If an error occurs during file operations.
     */
    public static void configure() throws IOException {
        makeCopyOfHistory();
        createEnvFile();
        createExecutorJSON();
    }

    /**
     * Creates a properties file containing environment information for Allure reporting.
     *
     * @throws IOException If an error occurs during file writing.
     */
    private static void createEnvFile() throws IOException {
        Properties props = new Properties();
        props.put("User", System.getProperty("user.name"));
        props.put("Browser", "Chrome");
        props.put("Flights.URL", DataReader.getProperty("FlightBooking.url"));
        props.put("Hotel.URL", DataReader.getProperty("HotelBooking.url"));
        props.put("OS", System.getProperty("os.name"));

        FileOutputStream fileOutputStream = new FileOutputStream(String.format(PATH, "environment.properties"));
        props.store(fileOutputStream, "Properties file for Allure environment");
        System.out.println("Environment Properties file created");
    }


    /**
     * Makes a copy of the report history directory.
     * If the report directory does not exist, it creates a new one.
     *
     * @throws IOException If an error occurs during directory creation or file copy.
     */
    private static void makeCopyOfHistory() throws IOException {
        if (!resultDir.exists()) {
            System.out.println("Creating new Report Directory");
            resultDir.mkdir();
        }

        if (reportHistoryDir.exists()) {
            resultHistoryDir.mkdir();
            FileUtils.copyDirectory(new File(reportHistoryDir.getAbsolutePath()),
                    new File(resultHistoryDir.getAbsolutePath()));
            System.out.println("Copied history to results directory!");
        }
    }

    /**
     * Creates an executor JSON file containing build information for Allure reporting.
     *
     * @throws IOException If an error occurs during file writing.
     */
    private static void createExecutorJSON() throws IOException {
        Map<String, String> executorMap = new HashMap<>();
        executorMap.put("name", System.getProperty("user.name"));
        executorMap.put("buildName", System.getProperty("os.name"));
        executorMap.put("type", "jenkins");

        FileWriter fileWriter = new FileWriter(String.format(PATH, "executor.json"));

        fileWriter.write(new Json().toJson(executorMap));
        fileWriter.flush();
        System.out.println("Executor JSON file created");

    }
}
