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

    /** Find the CSVRecord with the lowest value in the named `column` in the data.
     *  
     *  @param parser   represents the weather data to search
     *  @param column   the name of the column being compared.  Must parse to a number.
     *  @return the CSVRecord cooresponding to the lowest temperature seen in the data.  If
     *  no valid return value found, returns null.
     */
    public CSVRecord minimumValueInColumn (CSVParser parser, String column) {
        CSVRecord minimumSoFar = null;
        for (CSVRecord currentRow : parser) {
            // The data record for this column might be empty, or contain "n/a" or "na" or 
            // "#N/A".  Skip those.
            String columnData = currentRow.get(column).toUpperCase();
            if (! columnData.isEmpty() && ! columnData.equals("N/A") && 
            ! columnData.equals("na") && ! columnData.equals("#N/A")) {
                // The columnData contains "something", try converting to a number.
                // We're also told that sometimes "no data" is indicated with a -9999 value.
                double columnValue = Double.parseDouble(columnData);
                if (columnValue != -9999) {
                    // Now we have a number that is valid.  Decide if we need
                    // to remember this row.
                    if (minimumSoFar == null) {
                        minimumSoFar = currentRow;
                    } else {
                        // Need values to compare.
                        double minValueSoFar = Double.parseDouble(minimumSoFar.get(column));
                        if (columnValue < minValueSoFar) {
                            minimumSoFar = currentRow;
                        }
                    }
                }
            }
        }
        return minimumSoFar;
    }

    /** Find the CSVRecord with the coldest temperature in the data and thus all the information about 
     *  the coldest temperature, such as the hour of the coldest temperature.
     *  
     *  @param parser   represents the weather data to search
     *  @return the CSVRecord cooresponding to the lowest temperature seen in the data. 
     */
    public CSVRecord coldestHourInFile (CSVParser parser) {
        return minimumValueInColumn(parser, TEMPERATURE_COLUMN);
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

    /** From a group of files the user picks, find the name of the file containing the
     *  minimum value in the indicated `column` and return the that file's name.
     *  
     *  @param column   the column to examine for the lowest value.  Must convert to a number.
     *  @return the filename of the data file that contains the minimum value in the
     *  passed `column` in a group of files.
     */
    public String fileWithMinimumValue (String column) {
        DirectoryResource dr = new DirectoryResource();
        String minimumFilename = null;
        CSVRecord minimumSoFar = null;

        for (File f : dr.selectedFiles()) {
            FileResource fr = new FileResource(f);
            CSVRecord currentMinimum = minimumValueInColumn(fr.getCSVParser(), column);
            // Make sure we got a result before doing anything else.
            if (currentMinimum != null) {
                // First time thru
                if (minimumFilename == null || minimumSoFar == null) {
                    minimumFilename = f.getName();  // getCanonicalPath() requires an exception handler.
                    minimumSoFar = currentMinimum;
                } else {
                    // Rest of the times thru.  Need values to compare.
                    // Don't need to worry about invalid values here, because
                    // we would not have got here if minimumValueInColumn() found nothing.
                    double minimumValue = Double.parseDouble(minimumSoFar.get(column));
                    double currentValue = Double.parseDouble(currentMinimum.get(column));
                    if (currentValue < minimumValue) {
                        minimumFilename = f.getName();
                        minimumSoFar = currentMinimum;
                    }
                }
            }
        }

        return minimumFilename;
    }

    /** From a group of files the user picks, find the name of the file containing the coldest 
     *  temperature value and return that name.
     *  
     *  @return the filename of the weather data file that contains the coldest temperature in a group of files.
     */
    public String fileWithColdestTemperature () {
        return fileWithMinimumValue(TEMPERATURE_COLUMN);
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
    
    /** Find the CSVRecord with the lowest humidity in the data and thus all the information about 
     *  that day, such as the hour of the lowest humidity.
     *  
     *  @param parser   represents the weather data to search
     *  @return the CSVRecord cooresponding to the lowest humidity seen in the data. 
     */
    public CSVRecord lowestHumidityInFile (CSVParser parser) {
        return minimumValueInColumn(parser, HUMIDITY_COLUMN);
    }
    
    /** Test driver for lowestHumidityInFile(). */
    public void testLowestHumidityInFile () {
        FileResource fr = new FileResource("nc_weather/2014/weather-2014-01-20.csv");
        CSVRecord lowestHumidity = lowestHumidityInFile(fr.getCSVParser());
        System.out.println("Expected:  Lowest Humidity was 24 at 2014-01-20 19:51:00");
        if (lowestHumidity != null) {
            System.out.println("Lowest Humidity was " + lowestHumidity.get(HUMIDITY_COLUMN) + 
                " at " + lowestHumidity.get(DATE_COLUMN));
        } else {
            System.out.println("no valid humidity found");
        }
    }

}  // ParseWeatherData
