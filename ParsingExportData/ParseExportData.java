import edu.duke.*;
import org.apache.commons.csv.*;

/**
 * Doing some simple data analysis on a CSV file of countries, exports, and export value.
 * 
 * @author Jeff Brown 
 * @version 1
 */
public class ParseExportData {
    
    // Column names can change, so use constants for them.
    private String COUNTRY_COLUMN = "Country";
    private String EXPORTS_COLUMN = "Exports";
    private String VALUE_COLUMN = "Value (dollars)";
    
    /** Search for `country` in the CSV data represented by `parser`.  
     * 
     *  @param parser   An instance of `CSVParser` representing the data
     *  @param country  The country name to search for.
     *  @return If `country` is found, return "country: exports: value (dollars)".  
     *  If not found, returns "NOT FOUND".
     */
    public String countryInfo(CSVParser parser, String country) {

        // Search for `country` in each row of data.  If found, return
        // specific information in a string.
        for (CSVRecord record : parser) {
            String countryValue = record.get("Country");
            if (countryValue.equals(country)) {
                // Found it.  Build the country info string and return it.
                return countryValue + ": " + record.get(EXPORTS_COLUMN) + ": " + record.get(VALUE_COLUMN);
            }
        }
        
        // Search failed.  We are to return "NOT FOUND".
        return "NOT FOUND";
    }
    
    /** Print the name of all countries that export `exportItem1` and `exportItem2`.
     * 
     *  @param parser   represents the CSV data
     *  @param exportItem1  the first item to search for
     *  @param exportItem2  the second item to search for
     */
    public void listExportersTwoProducts (CSVParser parser, String exportItem1, String exportItem2) {
        for (CSVRecord record : parser) {
            String exports = record.get(EXPORTS_COLUMN);
            if (!exportItem1.isEmpty() && exports.contains(exportItem1) && 
                !exportItem2.isEmpty() && exports.contains(exportItem2)) {
                System.out.println(record.get(COUNTRY_COLUMN));
            }
        }
    }
    
    /** Count how many countries export `exportItem` in the data represented by `parser`.
     * 
     *  @param parser   represents the data to search
     *  @param exportItem  the export item to search for
     *  @return the count of countries that export `exportItem`.  Returns zero if 
     *  `exportItem` isn't found or is the empty string.
     */
    public int numberOfExporters (CSVParser parser, String exportItem) {
        int count = 0;
        if (! exportItem.isEmpty()) {
            for (CSVRecord record : parser) {
                String exports = record.get(EXPORTS_COLUMN);
                if (exports.contains(exportItem)) {
                    count = count + 1;
                }
            }
        }
        return count;
    }
    
    /** Prints the country and export value of "big exporters", where "big" means
     *  the length of `value` is shorter than the character length of the value in
     *  the CSV data represented by `parser`.
     *  
     *  @param parser   represents the data to search
     *  @param value    a string representing the minimum value.  Since we don't know
     *  how to convert strings to numbers yet, we go off string length.
     */
    public void bigExporters (CSVParser parser, String value) {
        int valueLen = value.length();
        for (CSVRecord record : parser) {
            String valueData = record.get(VALUE_COLUMN);
            if (valueData.length() > valueLen) {
                System.out.println(record.get(COUNTRY_COLUMN) + " " + valueData);
            }
        }
    }
    
    private void step2(CSVParser parser, String country, String expected) {
        String info = countryInfo(parser, country);
        if (! info.equals(expected)) {
            System.out.println(country+" -- expected '"+expected+"', got '"+info+"'");
        }
    }
    
    private void step3 (CSVParser parser, String exportItem1, String exportItem2, String description) {
        System.out.println(description);
        listExportersTwoProducts(parser, exportItem1, exportItem2);
        System.out.println("");
    }
    
    private void step4 (CSVParser parser, String exportItem, int expected) {
        int count = numberOfExporters(parser, exportItem);
        if (count != expected)
            System.out.println(exportItem + "-- expected count = "+expected+", got "+count);
    }
    
    private void step5 (CSVParser parser, String value, String description) {
        System.out.println(description);
        bigExporters(parser, value);
        System.out.println("");
    }
    
    /** Test driver for ParseExportData */
    public void tester() {
        // Step 1.  Pick a file.  We have to get a CSV parser for each test call, so
        // not putting it in a variable.
        FileResource fr = new FileResource();
        
        // Step 2.  Look up info on 1 particular country.
        // Expected result:  "Germany: motor vehicles, machinery, chemicals: $1,547,000,000,000"
        step2(fr.getCSVParser(), "Moon Base", "NOT FOUND");
        step2(fr.getCSVParser(), "Germany", "Germany: motor vehicles, machinery, chemicals: $1,547,000,000,000");
        step2(fr.getCSVParser(), "", "NOT FOUND");
        
        // Step 3.  Print countries that export both items in the argument list.
        step3(fr.getCSVParser(), "junk food", "gold", "Should be no countries");
        step3(fr.getCSVParser(), "", "diamonds", "Should be no countries");
        step3(fr.getCSVParser(), "gold", "", "Should be no countries");
        step3(fr.getCSVParser(), "gold", "diamonds", "Should be Namibia, South Africa");
        
        // Step 4.  Count the number of countries that export a specific item.
        step4(fr.getCSVParser(), "", 0);
        step4(fr.getCSVParser(), "junk food", 0);
        step4(fr.getCSVParser(), "gold", 3);
        
        // Step 5.  Print countries with high dollar value exports
        step5(fr.getCSVParser(), "$999,999,999,999,999,999,999,999,999", "Should be no countries");
        step5(fr.getCSVParser(), "$999,999,999", "Should be Germany, Macedonia, Malawi, Malaysia, Namibia, Peru, S. Africa, USA");
        step5(fr.getCSVParser(), "$", "Should be 10 countries");
        step5(fr.getCSVParser(), "", "Should be 10 countries");
        
        System.out.println("tests finished");
    }

} // ParseExportData
