import edu.duke.*;
import org.apache.commons.csv.*;

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

}  // ParseWeatherData
