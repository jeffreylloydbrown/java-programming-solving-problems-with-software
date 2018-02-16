import edu.duke.*;
import org.apache.commons.csv.*;

/**
 * Given a large amount of data on babies born in the USA 1880-2014,
 * answer different questions about all that data.
 * 
 * @author Jeff Brown
 * @version 1
 * <p>
 * Baby name data is provided in a series of CSV files.  There is by-year data
 * in the us_babynames_by_year directory.  There is by-decade data in the 
 * us_babynames_by_decade directory.  There are small test datafiles in the
 * us_babynames_test directory.  All data files share a common format,
 * and none have header rows.  (So catting them together is easy.)
 * <p>
 * Name, Gender, Count
 * Name:  the baby name
 * Gender:  "M" for male and "F" for female.  Other codes are not supported.
 * Count: the number of babies born with `name`.  String representation of an integer.
 * <p>
 * Data file rows are presorted by females, then males, each in descending count
 * order.
 * <p>
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
    private String getBabyName (CSVRecord record) { return record.get(NAME); }

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
    private boolean isFemale (CSVRecord record) { return isGender(record, FEMALE); }

    private boolean isMale (CSVRecord record) { return isGender(record, MALE); }
    
    private boolean isGender (CSVRecord record, String gender) { return getGender(record).equals(gender); }
    
    private String genderPronoun(String gender) {
        if (gender.equals(MALE))         return "he";
        else if (gender.equals(FEMALE))  return "she";
        else                             return "don't recognize gender '" + gender + "'";
    }
    
    // We will need to build a filename from a year code.
    private String byYearFilename (int year) { return "us_babynames_by_year/yob" + year + ".csv"; }
    
    private String byDecadeFilename (int year) { return "us_babynames_by_decade/yob" + year + "s.csv"; }
    
    private String byTestFilename (int year) { return "us_babynames_test/yob" + year + "short.csv"; }
    
    private String EXAMPLE_FILENAME = "us_babynames_test/example-small.csv";

    //////////////////////////////////////////////////////////////////////////////////
    //// Primary implementation.                                                  ////
    //////////////////////////////////////////////////////////////////////////////////

    /** Example code from course video that reads a data file and does nothing interesting.
     * 
     *  @param year     the year to read
     */
    public void readOneFile (int year) {
        FileResource fr = new FileResource(byYearFilename(year));
        for (CSVRecord rec : fr.getCSVParser(false)) {
            String name = getBabyName(rec);
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
                System.out.println("Name " + getBabyName(rec) + 
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
        FileResource fr = new FileResource(EXAMPLE_FILENAME);
        System.out.println("Expect 1700 total births, 1500 females, 200 males");
        totalBirths(fr);
        
        fr = new FileResource(byYearFilename(2014));
        System.out.println("Expect 3670151 total births, 1768775 females, 1901376 males");
        totalBirths(fr);
    }
    
    /** Given a FileResource and a `gender`, create a StorageResource that
     *  contain only rows matching `gender`.
     *  
     *  This filtering will make finding ranks easier, and separates the concern of
     *  filtering from counting to the appropriate rank number.
     *  
     *  @param fr       The FileResource we are filtering.
     *  @param gender   The gender we are filtering for.  We do not confirm the gender
     *  is actually a supported gender, so future genders won't change this code.
     *  @return a StorageResource that contains only records matching `gender`, in
     *  the order they occur in `fr`.
     */
    public MyStorageResource filterByGender (FileResource fr, String gender) {
        MyStorageResource sr = new MyStorageResource();
        for (CSVRecord record : fr.getCSVParser(false)) {
            if (isGender(record, gender)) {
                sr.add(getBabyName(record)+","+getGender(record)+","+getCount(record));
            }
        }
        return sr;
    }
    
    /** Given a `name`, a `gender` and a `year`, determine the rank of `name` in `year`.
     *  Rank starts at 1.  That is, the most popular name of each gender is rank 1.
     *  
     *  @param year     The year to examine.
     *  @param name     The baby name to find in `year`
     *  @param gender   The gender to use in the search
     *  @return the rank of `name` in `year`.  If `name` isn't found, return -1.
     */
    public int getRank (int year, String name, String gender) {
        return -1;
    }
    
    /** Given a `rank` in a `year` for a particular `gender`, look up and return the
     *  baby name at that rank.
     *  
     *  @param year     The year to examine.
     *  @param rank     The nth name to find.
     *  @param gender   The gender to use in the search
     *  @return the baby name at `rank` in `year` for `gender`.  If no such rank
     *  exists, return "NO NAME".
     */
    public String getName (int year, int rank, String gender) {
        return "NO NAME";
    }
    
    /** Given a `name` in a `year` and a `gender`, look up that same name and gender in 
     *  `newYear` and print `name` born in `year` would be `newName` if she/he was born in `newYear`.
     *  
     *  @param name     The baby name to find in `year`
     *  @param year     The year to search for `name` to get its rank
     *  @param newYear  The other year to search for that same rank
     *  @param gender   The gender to use in the search
     */
    public void whatIsNameInYear (String name, int year, int newYear, String gender) {
        String newName = "NO NAME";
        System.out.println(name + " born in " + year + " would be " + newName + 
                            " if " + genderPronoun(gender) + " was born in " + newYear + ".");
    }
    
    public void testWhatIsNameInYear () {
        // Jennifer in 1994 is rank 21.
        // Jennifer in 1994 is Grace in 2014.  (Grace is rank 21 in 2014.)
        System.out.println("Expect:  Jennifer born in 1994 would be Grace if she was born in 2014.");
        whatIsNameInYear("Jennifer", 1994, 2014, FEMALE);
    }
    
    /** Given a `name` in a `year` and a `gender`, look up that same name and gender in 
     *  `decade` and print `name` born in `year` would be `newName` if she/he was born in the `newYear`s.
     *  
     *  @param name     The baby name to find in `year`
     *  @param year     The year to search for `name` to get its rank
     *  @param newYear  The other year to search for that same rank
     *  @param gender   The gender to use in the search
     */
    public void whatIsNameInDecade (String name, int year, int decade, String gender) {
        String newName = "NO NAME";
        System.out.println(name + " born in " + year + " would be " + newName + 
                            " if " + genderPronoun(gender) + " was born in the " + decade + "s.");
    }
    
    public void testWhatIsNameInDecade () {
    System.out.println("Expected:  Jennifer born in 1994 would be Alexandra if she was born in the 2000s.");
    whatIsNameInDecade("Jennifer", 1994, 2000, FEMALE);
    System.out.println("1990 is Crystal");
    whatIsNameInDecade("Jennifer", 1994, 1990, FEMALE);
    System.out.println("1980 is Erica");
    whatIsNameInDecade("Jennifer", 1994, 1980, FEMALE);
    System.out.println("1970 is Barbara");
    whatIsNameInDecade("Jennifer", 1994, 1970, FEMALE);
    System.out.println("1920 is Edith");
    whatIsNameInDecade("Jennifer", 1994, 1920, FEMALE);
    System.out.println("1910 is Lucille");
    whatIsNameInDecade("Jennifer", 1994, 1910, FEMALE);
    System.out.println("1900 is Sarah");
    whatIsNameInDecade("Jennifer", 1994, 1900, FEMALE);
    System.out.println("1890 is Cora");
    whatIsNameInDecade("Jennifer", 1994, 1890, FEMALE);
    System.out.println("NO NAME");
    whatIsNameInDecade("Jennifer", 1994, 2000, MALE);
    }
    
    /** Given a set of data files selected by the user, determine when `name` and `gender`
     *  had the highest rank.
     *  
     *  @param name     The baby name to search for
     *  @param gender   The gender to use in the search
     *  @return the year where `name` & `gender` are the most frequent.  If `name`
     *  isn't found, return -1.
     */
    public int yearOfHighestRank (String name, String gender) {
        return -1;
    }
    
    /** Given a set of data files selected by the user, determine the average rank
     *  held by `name` and `gender`.
     *  
     *  @param name     The baby name to search for
     *  @param gender   The gender to use in the search
     *  @return the average rank that `name` has within the selected data files.
     *  If `name` isn't ranked in any of the files, return -1.0.
     */
    public double getAverageRank (String name, String gender) {
        return -1.0;
    }
    
    /** Given a `name` and `gender` in some `year`, determine how many babies were
     *  born with a higher ranked name (meaning lower rank number) than `name`.
     *  
     *  @param year     The year to search for `name` to get its rank
     *  @param name     The baby name to search for
     *  @param gender   The gender to use in the search
     *  @return the total number of births, regardless of gender, that have names
     *  more popular than `name`.
     */
    public int getTotalBirthsRankedHigher (int year, String name, String gender) {
        return -1;
    }

}  // BabyBirths
