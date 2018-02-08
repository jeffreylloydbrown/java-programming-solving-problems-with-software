import edu.duke.*;
import org.apache.commons.csv.*;

/**
 * Write a description of ParseExportData here.
 * 
 * @author Jeff Brown 
 * @version 1
 */
public class ParseExportData {
    
    /** Search for `country` in the CSV data represented by `parser`.  
     * 
     *  @param parser   An instance of `CSVParser` representing the data
     *  @param country  The country name to search for.
     *  @returns If `country` is found, return <country>: <exports>: <value (dollars)>.  
     *  If not found, returns "NOT FOUND".
     */
    public String countryInfo(CSVParser parser, String country) {

        // Search for `country` in each row of data.  If found, return
        // specific information in a string.
        for (CSVRecord record : parser) {
            String countryValue = record.get("Country");
            if (countryValue.equals(country)) {
                // Found it.  Build the country info string and return it.
                return countryValue + ": " + record.get("Exports") + ": " + record.get("Value (dollars)");
            }
        }
        
        // Search failed.  We are to return "NOT FOUND".
        return "NOT FOUND";
    }
    
    public void listExportersTwoProducts (CSVParser parser, String exportItem1, String exportItem2) {
        for (CSVRecord record : parser) {
            String exports = record.get("Exports");
            if (!exportItem1.isEmpty() && exports.contains(exportItem1) && 
                !exportItem2.isEmpty() && exports.contains(exportItem2)) {
                System.out.println(record.get("Country"));
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
    
    /** Test driver for ParseExportData */
    public void tester() {
        // Step 1.  Pick a file and get a CSV parser for it.
        FileResource fr = new FileResource();
        
        // Step 2.  Look up info on 1 particular country.
        // Expected result:  "Germany: motor vehicles, machinery, chemicals: $1,547,000,000,000"
        step2(fr.getCSVParser(), "Moon Base", "NOT FOUND");
        step2(fr.getCSVParser(), "Germany", "Germany: motor vehicles, machinery, chemicals: $1,547,000,000,000");
        step2(fr.getCSVParser(), "", "NOT FOUND");
        
        // Step 3.  Return countries that export both items in the argument list.
        step3(fr.getCSVParser(), "junk food", "gold", "Should be no countries");
        step3(fr.getCSVParser(), "", "diamonds", "Should be no countries");
        step3(fr.getCSVParser(), "gold", "", "Should be no countries");
        step3(fr.getCSVParser(), "gold", "diamonds", "Should be Namibia, South Africa");
        
        
        System.out.println("tests finished");
    }

} // ParseExportData
