import edu.duke.*;
import org.apache.commons.csv.*;
import java.io.*;

/**
 * Find the coldest day of the year and other interesting facts about the temperature and humidity in a day.
 * 
 * @author Jeff Brown
 * @version 1
 */
public class ParseWeatherData {
    // Column names can change, so refer to them via constants.
    private String TEMPERATURE_COLUMN = "TemperatureF";
    private String TIME_COLUMN = "TimeEST";
    private String HUMIDITY_COLUMN = "Humidity";
    private String DATE_COLUMN = "DateUTC";
    
    /** Find the CSVRecord with the coldest temperature in the data and thus all the information about 
     *  the coldest temperature, such as the hour of the coldest temperature.
     *  
     *  @param parser   represents the weather data to search
     *  @return the CSVRecord cooresponding to the lowest temperature seen in the data. 
     */
    public CSVRecord coldestHourInFile (CSVParser parser) {
        CSVRecord coldestSoFar = null;
        for (CSVRecord currentRow : parser) {
            if (coldestSoFar == null) {
                coldestSoFar = currentRow;
            } else {
                // Need temps to compare.
                double coldestTemp = Double.parseDouble(coldestSoFar.get(TEMPERATURE_COLUMN));
                double currentTemp = Double.parseDouble(currentRow.get(TEMPERATURE_COLUMN));
                if (currentTemp != -9999 && currentTemp < coldestTemp) {
                    coldestSoFar = currentRow;
                }
            }
        }
        return coldestSoFar;
    }
    
    /** Test driver for coldestHourInFile() */
    public void testColdestHourInFile () {
        FileResource fr = new FileResource("nc_weather/2014/weather-2014-01-08.csv");
        CSVRecord coldestHour = coldestHourInFile(fr.getCSVParser());
        if (coldestHour != null) {
            System.out.println("coldest temp is " + coldestHour.get(TEMPERATURE_COLUMN) + 
                               " at time " + coldestHour.get(TIME_COLUMN));
        } else {
            System.out.println("no valid temperature found");
        }
    }
    
    /** From a group of files the user picks, find the name of the file containing the coldest 
     *  temperature value and return that name.
     *  
     *  @return the filename of the weather data file that contains the coldest temperature in a group of files.
     */
    public String fileWithColdestTemperature () {
        DirectoryResource dr = new DirectoryResource();
        String coldestFilename = null;
        CSVRecord coldestSoFar = null;
        
        for (File f : dr.selectedFiles()) {
            FileResource fr = new FileResource(f);
            CSVRecord currentColdest = coldestHourInFile(fr.getCSVParser());
            if (coldestFilename == null || coldestSoFar == null) {
                coldestFilename = f.getName();  // getCanonicalPath() requires an exception handler.
                coldestSoFar = currentColdest;
            } else {
                // Need temps to compare.
                double coldestTemp = Double.parseDouble(coldestSoFar.get(TEMPERATURE_COLUMN));
                double currentTemp = Double.parseDouble(currentColdest.get(TEMPERATURE_COLUMN));
                if (currentTemp != -9999 && currentTemp < coldestTemp) {
                    coldestFilename = f.getName();
                    coldestSoFar = currentColdest;
                }
            }
        }
        
        return coldestFilename;
    }
    
    /** Test driver for fileWithColdestTemperature() */
    public void testFileWithColdestTemperature () {
        String coldestFilename = fileWithColdestTemperature();
        
        // using getCanonicalPath() in fileWithColdestTemperature() requires an exception handler,
        // which we don't know yet.  So this test is kludged to hardcode choices in the 2014 directory.
        FileResource fr = new FileResource("nc_weather/2014/"+coldestFilename);
        CSVRecord coldestHour = coldestHourInFile(fr.getCSVParser());
        
        System.out.println("Coldest day was in file " + coldestFilename);
        System.out.println("Coldest temperature on that day was " + coldestHour.get(TEMPERATURE_COLUMN));
        System.out.println("All the Temperatures on the coldest day were:");
        for (CSVRecord row : fr.getCSVParser()) {
            System.out.println(row.get(DATE_COLUMN) + ": " + row.get(TEMPERATURE_COLUMN));
        }
    }

}  // ParseWeatherData
