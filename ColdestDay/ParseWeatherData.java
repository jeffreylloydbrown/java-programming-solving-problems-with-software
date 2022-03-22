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
    private double VALUE_MISSING_FLAG = -9999.0;
    
    /** Convert the data found in column `column` of `record` into a numeric value.
     * 
     * @param record    the data record to examine.
     * @param column    the column to attempt to convert into a number.
     * @returns the converted value if possible, or Double.NaN if anything fails.
     */
    private double getNumber (CSVRecord record, String column) {
        // I don't care that we don't know exception handling yet.
        // The column might be empty, or not exist in the record.  Attempting
        // the get will throw an exception.  The value might not be a number,
        // so it won't parse and instead throw an exception.  In all those cases,
        // return Double.NaN as the "value".
        double result;
        
        try {
            result = Double.parseDouble(record.get(column));
        } catch (Exception e) {
            result = Double.NaN;
        }
        
        return result;
    }

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
            double currentValue = getNumber(currentRow, column);
            if (currentValue != Double.NaN && currentValue != VALUE_MISSING_FLAG) {
                if (minimumSoFar == null) {
                    minimumSoFar = currentRow;
                } else {
                    double minimumValue = getNumber(minimumSoFar, column);
                    if (currentValue < minimumValue) {
                        minimumSoFar = currentRow;
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
                    double minimumValue = getNumber(minimumSoFar, column);
                    double currentValue = getNumber(currentMinimum, column);
                    if (currentValue < minimumValue) {
                        minimumFilename = f.getName();
                        minimumSoFar = currentMinimum;
                    }
                }
            }
        }

        return minimumFilename;
    }
    
    /** From a group of files the user picks, find the row of data containing
     *  minimum value in the indicated `column` and return that row.
     *  
     *  @param column   the column to examine for the lowest value.  Must convert to a number.
     *  @return the CSVRecord with the minimum value in `column`.  If no valid values found, 
     *  return null.
     */
    public CSVRecord minimumValueManyFiles (String column) {
        DirectoryResource dr = new DirectoryResource();
        CSVRecord minimumSoFar = null;

        for (File f : dr.selectedFiles()) {
            FileResource fr = new FileResource(f);
            CSVRecord currentMinimum = minimumValueInColumn(fr.getCSVParser(), column);
            // Make sure we got a result before doing anything else.
            if (currentMinimum != null) {
                // First time thru
                if (minimumSoFar == null) {
                    minimumSoFar = currentMinimum;
                } else {
                    // Rest of the times thru.  Need values to compare.
                    // Don't need to worry about invalid values here, because
                    // we would not have got here if minimumValueInColumn() found nothing.
                    double minimumValue = getNumber(minimumSoFar, column);
                    double currentValue = getNumber(currentMinimum, column);
                    if (currentValue < minimumValue) {
                        minimumSoFar = currentMinimum;
                    }
                }
            }
        }

        return minimumSoFar;
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
    
    /** From a group of files the user picks, find the row of data with the lowest
     *  humidity value and return its data record.
     *  
     *  @return the data record containing the lowest humidity value in a group of files
     */
    public CSVRecord lowestHumidityInManyFiles () {
        return minimumValueManyFiles(HUMIDITY_COLUMN);
    }

    /** Test driver for fileWithColdestTemperature() */
    public void testLowestHumidityInManyFiles () {
        CSVRecord lowestHumidity = lowestHumidityInManyFiles();
        System.out.println("Expected:  Lowest Humidity was 24 at 2014-01-20 19:51:00");
        if (lowestHumidity != null) {
            System.out.println("Lowest Humidity was " + lowestHumidity.get(HUMIDITY_COLUMN) + 
                " at " + lowestHumidity.get(DATE_COLUMN));
        } else {
            System.out.println("no valid humidity found");
        }
    }
    
    // Calculate an average temperature for days with some minimum humidity.  Humidity cannot
    // be negative, so to get just a simple average of all temperatures pass a negative
    // humidity value.
    private double averageTemperatureWithMinimumHumidity (CSVParser parser, int minHumidity) {
        double totalTemp = 0.0;
        int count = 0;
        for (CSVRecord record : parser) {
            double temp = getNumber(record, TEMPERATURE_COLUMN);
            double humidity = getNumber(record, HUMIDITY_COLUMN);
            if (humidity >= minHumidity) {
                totalTemp = totalTemp + temp;
                count = count + 1;
            }
        }
        
        if (count == 0) {
            return Double.NaN;
        } else {
            return totalTemp / count;
        }
    }
    
    /** Calculate the average temperature in the data represented by `parser`.
     * 
     *  @param parser   the data to calculate the average temperature on.
     *  @return the average temperature of the data `parser` represents.  If no average
     *  possible, then return Double.NaN.
     */
    public double averageTemperatureInFile (CSVParser parser) {
        return averageTemperatureWithMinimumHumidity(parser, -9999);
    }
    
    /** Test driver for averageTemperatureInFile(). */
    public void testAverageTemperatureInFile () {
        FileResource fr = new FileResource("nc_weather/2014/weather-2014-01-20.csv");
        System.out.println("Expected:  Average temperature in file is 44.93333333333334");
        System.out.println("Average temperature in file is " + averageTemperatureInFile(fr.getCSVParser()));
    }
    
    /** Calculate the average temperature in the data represented by `parser`, but only
     *  for days at least as humid as `minHumidity`.
     * 
     *  @param parser   the data to calculate the average temperature on.
     *  @param minHumidity  the minimum humidity to qualify a measurement for being in the average.
     *  @return the average temperature of the data `parser` represents.  If no average
     *  possible, then return Double.NaN.
     */
    public double averageTemperatureWithHighHumidityInFile (CSVParser parser, int minHumidity) {
        return averageTemperatureWithMinimumHumidity(parser, minHumidity);
    }
    
    /** Test driver for averageTemperatureWithHighHumidityInFile(). */
    public void testAverageTemperatureWithHighHumidityInFile () {
        FileResource fr = new FileResource("nc_weather/2014/weather-2014-01-20.csv");
        double avg;
        System.out.println("Case 1 Expected:  No temperatures with that humidity");
        avg = averageTemperatureWithHighHumidityInFile(fr.getCSVParser(), 80);
        if (! Double.isNaN(avg)) {
            System.out.println("Average Temp when high Humidity is " + avg);
        } else {
            System.out.println("No temperatures with that humidity");
        }
        
        System.out.println("Case 2 Expected:  Average Temp when high Humidity is 41.78666666666667");
        fr = new FileResource("nc_weather/2014/weather-2014-03-20.csv");
        avg = averageTemperatureWithHighHumidityInFile(fr.getCSVParser(), 80);
        if (! Double.isNaN(avg)) {
            System.out.println("Average Temp when high Humidity is " + avg);
        } else {
            System.out.println("No temperatures with that humidity");
        }
    }
    
    public void finalQuiz () {
        FileResource fr = new FileResource("nc_weather/2014/weather-2014-06-29.csv");
        CSVRecord lowestHumidity = lowestHumidityInFile(fr.getCSVParser());
        System.out.println("lowest humidity on 6/29/14 was " + getNumber(lowestHumidity, HUMIDITY_COLUMN));
        
        fr = new FileResource("nc_weather/2014/weather-2014-07-22.csv");
        lowestHumidity = lowestHumidityInFile(fr.getCSVParser());
        System.out.println("lowest humidity on 7/22/14 was " + getNumber(lowestHumidity, HUMIDITY_COLUMN) + 
            " at " + lowestHumidity.get(DATE_COLUMN));
        
        lowestHumidity = lowestHumidityInManyFiles();
        System.out.println("lowest humidity in 2013 was " + getNumber(lowestHumidity, HUMIDITY_COLUMN) +
            " at " + lowestHumidity.get(DATE_COLUMN));
       
        fr = new FileResource("nc_weather/2013/weather-2013-08-10.csv");
        double averageTemp = averageTemperatureInFile(fr.getCSVParser());
        System.out.println("average temp on 8/10/2013 was " + averageTemp);
        
        fr = new FileResource("nc_weather/2013/weather-2013-09-02.csv");
        averageTemp = averageTemperatureWithHighHumidityInFile(fr.getCSVParser(), 80);
        System.out.println("average temp over 80% humidity on 9/2/2013 is " + averageTemp);
        
        String filename = fileWithColdestTemperature();
        System.out.println("file with coldest 2013 temperature = " + filename);
        
        fr = new FileResource("nc_weather/2013/"+filename);
        CSVRecord coldest = coldestHourInFile(fr.getCSVParser());
        System.out.println("coldest temperature that day is " + getNumber(coldest, TEMPERATURE_COLUMN));
    }

}  // ParseWeatherData
