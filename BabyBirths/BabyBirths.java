import edu.duke.*;
import org.apache.commons.csv.*;

/**
 * Given a large amount of data on babies born in the USA 1880-2014,
 * answer different questions about all that data.
 * 
 * @author Jeff Brown
 * @version 1
 *
 * Baby name data is provided in a series of CSV files.  There is by-year data
 * in the us_babynames_by_year directory.  There is by-decade data in the 
 * us_babynames_by_decade directory.  There are small test datafiles in the
 * us_babynames_test directory.  All data files share a common format,
 * and none have header rows.  (So catting them together is easy.)
 * 
 * Name, Gender, Count
 * Name:  the baby name
 * Gender:  "M" for male and "F" for female.  Other codes are not supported.
 * Count: the number of babies born with `name`.  String representation of an integer.
 * 
 * Data file rows are presorted by females, then males, each in descending count
 * order.
 * 
 * All by-year files are called "yobNNNN.csv", where NNNN is the year.
 * All by-decade files are called "yobDDDDs.csv", where DDDD is a 4-digit decade like 1890, 1900, 1910, etc.
 * The small test files use "yobNNNNshort.csv", where NNNN is the year.  There is also
 * an example-small.csv file present, which is used in the course video.
 * 
 */
public class BabyBirths {

    //////////////////////////////////////////////////////////////////////////////////
    //// Data representation.  All of this really should be put in its own class. ////
    //////////////////////////////////////////////////////////////////////////////////

    // Columns are not named, so use variables to hold their current indices.
    // Avoids hard-coding so that if the layout changes later, just adjust it here.
    private int NAME = 0;
    private int GENDER = 1;
    private int COUNT = 2;

    // Gender is encoded as a string, so it could change or get additional
    // values "later".  Use these constants instead of hard-coding.
    private String MALE = "M";
    private String FEMALE = "F";

    // Accessors to help code readability, instead of more cryptic get() calls.
    private String getName (CSVRecord record) { return record.get(NAME); }

    private String getGender (CSVRecord record) { return record.get(GENDER); }

    // We don't technically know about try-catch, and I just don't care.  This avoids
    // convoluted error handling.  Since actual counts are not negative, return -1
    // for any error.  
    private int getCount (CSVRecord record) {
        int count = -1;

        try {
            count = Integer.parseInt(record.get(COUNT));
        } catch (Exception e) {
            count = -1;
        }

        return count;
    }

    // We will need to test for gender, so encapsulate those.
    private boolean isFemale (CSVRecord record) { return getGender(record).equals(FEMALE); }

    private boolean isMale (CSVRecord record) { return getGender(record).equals(MALE); }

    //////////////////////////////////////////////////////////////////////////////////
    //// Primary implementation.                                                  ////
    //////////////////////////////////////////////////////////////////////////////////

    /** Example code from course video that reads a data file and does nothing interesting.
     * 
     *  @param year     the year to read
     */
    public void readOneFile (int year) {
        String filename = "us_babynames_by_year/yob" + year + ".csv";
        FileResource fr = new FileResource(filename);
        for (CSVRecord rec : fr.getCSVParser(false)) {
            String name = getName(rec);
            String gender = getGender(rec);
            // TODO
        }
    }

    /** Example code from course video that prints each row in the data file the user selects. */
    public void printNames () {
        FileResource fr = new FileResource();
        for (CSVRecord rec : fr.getCSVParser(false)) {
            int numBorn = getCount(rec);
            if (numBorn <= 100) {
                System.out.println("Name " + getName(rec) + 
                    " Gender " + getGender(rec) +
                    " Num Born " + getCount(rec));
            }
        }
    }

    /** Example code from course video that calculates the total births as well as the girl births
     *  and boy births and prints them.
     *  
     *  @param fr   A `FileResource` representing the CSV file to examine.
     */
    public void totalBirths (FileResource fr) {
        int totalBirths = 0;
        int totalBoys = 0;
        int totalGirls = 0;
        for (CSVRecord rec : fr.getCSVParser(false)) {
            int numBorn = getCount(rec);
            totalBirths += numBorn;
            if (isMale(rec)) totalBoys += numBorn;
            if (isFemale(rec)) totalGirls += numBorn;
        }
        System.out.println("total births = " + totalBirths);
        System.out.println("total girls = " + totalGirls);
        System.out.println("total boys = " + totalBoys);
    }
    
    /** Test driver for totalBirths(), from the course video. */
    public void testTotalBirths () {
        FileResource fr = new FileResource("us_babynames_test/example-small.csv");
        System.out.println("Expect 1700 total births, 1500 females, 200 males");
        totalBirths(fr);
        
        fr = new FileResource("us_babynames_by_year/yob2014.csv");
        System.out.println("Expect 3670151 total births, 1768775 females, 1901376 males");
        totalBirths(fr);
    }

    //////////////////////////////////////////////////////////////////////////////////
    //// Higher level tests.                                                      ////
    //////////////////////////////////////////////////////////////////////////////////

    // These answers come from the course videos, which will become test cases.
    // 
    // Jennifer in 1994 is rank 21.
    // Jennifer in 1994 is Grace in 2014.  (Grace is rank 21 in 2014.)
    // 2000s rank 21 is Alexandra
    // 1990s rank 21 is Crystal
    // 1980s rank 21 is Erica
    // 1970s rank 21 is Barbara
    // 1920s rank 21 is Edith
    // 1910s rank 21 is Lucille
    // 1900s rank 21 is Sarah
    // 1890s rank 21 is Cora    

}  // BabyBirths
