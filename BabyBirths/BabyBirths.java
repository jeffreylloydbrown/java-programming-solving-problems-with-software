import edu.duke.*;
import org.apache.commons.csv.*;
import java.io.File;

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
    void testTotalBirths () {
        FileResource fr = new FileResource(EXAMPLE_FILENAME);
        System.out.println("Expect 1700 total births, 1500 females, 200 males");
        totalBirths(fr);

        fr = new FileResource(byYearFilename(2014));
        System.out.println("Expect 3670151 total births, 1768775 females, 1901376 males");
        totalBirths(fr);
    }

    /** Given a FileResource and a `gender`, create a StorageResource that
     *  contains only rows matching `gender`.
     *  
     *  This filtering will make finding ranks easier, and separates the concern of
     *  filtering from counting to the appropriate rank number.
     *  
     *  @param fr       The FileResource we are filtering.
     *  @param gender   The gender we are filtering for.  We do not confirm the gender
     *  is actually a supported gender, so future genders won't change this code.
     *  @return a StorageResource that contains only records matching `gender`, in
     *  the order they occur in `fr`.
     *  @throws exception if `fr` isn't valid.
     *  @throws exception if `fr` does not represent CSV data.
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

    // Control whether we use the simplified test data or the real data for a year.
    // The decade data doesn't have simplified data, so no need to do this for decade stuff.
    // Unit tests should start by calling useTestData() and finish by calling useYearData().
    private char useData = 'y';
    private void useTestData() { useData = 't'; }

    private void useDecadeData() { useData = 'd'; }

    private void useYearData() { useData = 'y'; }

    private String getFilename(int year) {
        if (useData == 't') {
            return byTestFilename(year);
        } else if (useData == 'd') {
            return byDecadeFilename(year);
        } else {
            return byYearFilename(year);
        }
    }

    // Return true if a string is actually present:  not null and not empty.
    private boolean hasValue (String s) { return s != null && ! s.isEmpty(); }

    /** Given a `name`, a `gender` and a `year`, determine the rank of `name` in `year`.
     *  Rank starts at 1.  That is, the most popular name of each gender is rank 1.
     *  
     *  @param year     The year to examine.
     *  @param name     The baby name to find in `year`
     *  @param gender   The gender to use in the search
     *  @return the rank of `name` in `year`.  If `name` isn't found, return -1.
     *  @throws exception if `year` does not have a data file.
     */
    public int getRank (int year, String name, String gender) {
        // Only bother searching if name and gender are actually present.
        if (hasValue(name) && hasValue(gender)) {
            FileResource fr = new FileResource(getFilename(year));
            CSVParser parser = filterByGender(fr, gender).getCSVParser(false);

            // Thanks to filtering, I don't need to count the records seen.
            // The parser keeps track of that for me, and starts with 1.
            // Perfect.
            for (CSVRecord rec : parser) {
                if (getBabyName(rec).equals(name)) {
                    return (int) parser.getRecordNumber();
                }
            }
        }

        // Didn't find name.
        return -1;
    }

    /** Test driver for getRank(). */
    void testGetRank () {
        useTestData();
        System.out.println("Expect 1 for Sophia, got "+getRank(2012, "Sophia", "F"));
        System.out.println("Expect 4 for Olivia, got "+getRank(2012, "Olivia", "F"));
        System.out.println("Expect 1 for Jacob, got "+getRank(2012, "Jacob", "M"));
        System.out.println("Expect 2 for Mason, got "+getRank(2012, "Mason", "M"));
        System.out.println("Expect 3 for Ethan, got "+getRank(2012, "Ethan", "M"));
        System.out.println("Expect -1 for female Mason, got "+getRank(2012, "Mason", "F"));
        System.out.println("Expect -1 for male Olivia, got "+getRank(2012, "Olivia", "M"));
        System.out.println("Expect -1 for female empty name, got "+getRank(2012, "", "F"));
        System.out.println("Expect -1 for male null, got "+getRank(2012, null, "M"));
        System.out.println("Expect -1 for unsupported gender, got "+getRank(2012, "Sophia", ""));
        useYearData();
    }

    /** Given a `rank` in a `year` for a particular `gender`, look up and return the
     *  baby name at that rank.
     *  
     *  @param year     The year to examine.
     *  @param rank     The nth name to find.
     *  @param gender   The gender to use in the search
     *  @return the baby name at `rank` in `year` for `gender`.  If no such rank
     *  exists, return "NO NAME".
     *  @throws exception if `year` does not have a data file.
     */
    public String getName (int year, int rank, String gender) {
        FileResource fr = new FileResource(getFilename(year));
        CSVParser parser = filterByGender(fr, gender).getCSVParser(false);

        // If rank is not at least 1, don't bother searching.
        if (rank >= 1) {
            // Thanks to filtering, I simply have to count down the
            // rank number (a parser cannot return a specific record 
            // directly, itself).  When it reaches 1, return the name 
            // from the current record.
            for (CSVRecord rec : parser) {
                if (rank == 1) {
                    return getBabyName(rec);
                }
                rank = rank - 1;
            }
        }

        // Rank doesn't exist, too big or too small.
        return "NO NAME";
    }

    /** Test driver for getName(). */
    void testGetName () {
        useTestData();
        System.out.println("Expect Sophia for #1 female, got "+getName(2012, 1, "F"));
        System.out.println("Expect Olivia for #4 female, got "+getName(2012, 4, "F"));
        System.out.println("Expect Jacob for #1 male, got "+getName(2012, 1, "M"));
        System.out.println("Expect Mason for #2 male, got "+getName(2012, 2, "M"));
        System.out.println("Expect Ethan for #3 male, got "+getName(2012, 3, "M"));
        System.out.println("Expect NO NAME for #6 female, got "+getName(2012, 6, "F"));
        System.out.println("Expect NO NAME for #6 male, got "+getName(2012, 6, "M"));
        System.out.println("Expect NO NAME for -1 female, got "+getName(2012, -1, "F"));
        System.out.println("Expect NO NAME for -1 male, got "+getName(2012, -1, "M"));
        useYearData();
    }

    /** Given a `name` in a `year` and a `gender`, look up that same name and gender in 
     *  `newYear` and print `name` born in `year` would be `newName` if she/he was born in `newYear`.
     *  
     *  @param name     The baby name to find in `year`
     *  @param year     The year to search for `name` to get its rank
     *  @param newYear  The other year to search for that same rank
     *  @param gender   The gender to use in the search
     *  @throws exception if `year` or `newYear` do not have data files.
     */
    public void whatIsNameInYear (String name, int year, int newYear, String gender) {
        String newName = "NO NAME";

        // Only bother searching if name and gender are actually present.
        if (hasValue(name) && hasValue(gender)) {
            int rank = getRank(year, name, gender);
            newName = getName(newYear, rank, gender);
        }

        System.out.println(name + " born in " + year + " would be " + newName + 
            " if " + genderPronoun(gender) + " was born in " + newYear + ".");
    }

    /** Test driver for whatIsNameInYear(). */
    void testWhatIsNameInYear () {
        // Jennifer in 1994 is rank 17.  (not 21 like said in the course video.)
        // Jennifer in 1994 is Ella in 2014.  (Grace is rank 21 in 2014, but Ella is 17th.)
        useYearData();
        System.out.println("Expect:  Jennifer born in 1994 would be Ella if she was born in 2014.");
        whatIsNameInYear("Jennifer", 1994, 2014, FEMALE);
        useTestData();
        System.out.println("Expect:  Isabella born in 2012 would be Sophia if she was born in 2014.");
        whatIsNameInYear("Isabella", 2012, 2014, FEMALE);
        useYearData();
    }

    /** Given a `name` in a `year` and a `gender`, look up that same name and gender in 
     *  `decade` and print `name` born in `year` would be `newName` if she/he was born in the `newYear`s.
     *  
     *  @param name     The baby name to find in `year`
     *  @param year     The year to search for `name` to get its rank
     *  @param newYear  The other year to search for that same rank
     *  @param gender   The gender to use in the search
     *  @throws exception if `year` or `decade` do not have data files.
     */
    public void whatIsNameInDecade (String name, int year, int decade, String gender) {
        String newName = "NO NAME";

        // Only bother searching if name and gender are actually present.
        if (hasValue(name) && hasValue(gender)) {
            useYearData();
            int rank = getRank(year, name, gender);
            useDecadeData();
            newName = getName(decade, rank, gender);
            useYearData();
        }

        System.out.println(name + " born in " + year + " would be " + newName + 
            " if " + genderPronoun(gender) + " was born in the " + decade + "s.");
    }

    /** Test driver for whatIsNameInDecade(). */
    void testWhatIsNameInDecade () {
        System.out.println("Expected:  Jennifer born in 1994 would be Sandra if she was born in the 2000s.");
        whatIsNameInDecade("Jennifer", 1994, 2000, FEMALE);
        System.out.println("1990 is Sandra");
        whatIsNameInDecade("Jennifer", 1994, 1990, FEMALE);
        System.out.println("1980 is Carol");
        whatIsNameInDecade("Jennifer", 1994, 1980, FEMALE);
        System.out.println("1970 is Carol");
        whatIsNameInDecade("Jennifer", 1994, 1970, FEMALE);
        System.out.println("1920 is Rose");
        whatIsNameInDecade("Jennifer", 1994, 1920, FEMALE);
        System.out.println("1910 is Grace");
        whatIsNameInDecade("Jennifer", 1994, 1910, FEMALE);
        System.out.println("1900 is Grace");
        whatIsNameInDecade("Jennifer", 1994, 1900, FEMALE);
        System.out.println("1890 is Ruth");
        whatIsNameInDecade("Jennifer", 1994, 1890, FEMALE);
        // The 1994 data file shows 44 boys named Jennifer, so must use another name.
        System.out.println("NO NAME");
        whatIsNameInDecade("Supergirl", 1994, 2000, MALE);
    }

    // Extract the year from the filename of a File.
    // The year is the 4 characters after the right-most "yob" in the file name.
    private int fileYear (File f) {
        int year = -1;
        String filename = f.getName().toLowerCase();
        int yobPosition = filename.lastIndexOf("yob");
        if (yobPosition >= 0 && yobPosition+7 < filename.length()) {
            String yearStr = filename.substring(yobPosition+3, yobPosition+7);
            year = Integer.parseInt(yearStr);
        }

        return year;
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
        int bestRank = -1;      // tracks the best rank seen so far.
        int bestRankYear = -1;  // what year did bestRank happen in?

        // Only bother searching if name and gender are actually present.
        if (hasValue(name) && hasValue(gender)) {
            DirectoryResource dr = new DirectoryResource();
            for (File f : dr.selectedFiles()) {
                int year = fileYear(f);
                if (year != -1) {
                    // Get the current rank and compare it to bestRank.  Remember that
                    // on the first file bestRank won't be valid.  And we might have
                    // an unranked name, so check for that as well.
                    int currentRank = getRank(year, name, gender);
                    if (currentRank != -1) {
                        if (bestRank == -1) {
                            bestRank = currentRank;
                            bestRankYear = year;
                        } else if (currentRank < bestRank) {  // remember, better means lower rank
                            bestRank = currentRank;
                            bestRankYear = year;
                        }
                    }
                }
            }
        }

        return bestRankYear;
    }

    void testYearOfHighestRank () {
        useTestData();
        System.out.println("Expected year is 2012, got "+yearOfHighestRank("Mason", MALE));
        System.out.println("Expected year is 2013, got "+yearOfHighestRank("Noah", MALE));
        System.out.println("Expected year is -1, got "+yearOfHighestRank("Charlie", MALE));
        System.out.println("Expected year is -1, got "+yearOfHighestRank("Charlie", FEMALE));
        System.out.println("Expected year is -1, got "+yearOfHighestRank("", FEMALE));
        System.out.println("Expected year is -1, got "+yearOfHighestRank(null, FEMALE));
        System.out.println("Expected year is -1, got "+yearOfHighestRank("Mason", ""));
        System.out.println("Expected year is -1, got "+yearOfHighestRank("Mason", null));
        useYearData();
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
        int totalRank = 0;
        int ranks = 0;
        
        // Only bother computing if name and gender are actually present.
        if (hasValue(name) && hasValue(gender)) {
            DirectoryResource dr = new DirectoryResource();
            for (File f : dr.selectedFiles()) {
                int year = fileYear(f);
                if (year != -1) {
                    int currentRank = getRank(year, name, gender);
                    if (currentRank != -1) {
                        totalRank = totalRank + currentRank;
                        ranks = ranks + 1;
                    }
                }
            }
        }

        return (ranks == 0) ? -1.0 : ((double) totalRank) / ranks;
    }
    
    /** Test driver for getAverageRank(). */
    void testGetAverageRank () {
        useTestData();
        System.out.println("Expect 3.0, got "+getAverageRank("Mason", MALE));
        System.out.println("Expect 2.666..., got "+getAverageRank("Jacob", MALE));
        System.out.println("Expect -1.0, got "+getAverageRank("", MALE));
        System.out.println("Expect -1.0, got "+getAverageRank(null, MALE));
        System.out.println("Expect -1.0, got "+getAverageRank("Mason", ""));
        System.out.println("Expect -1.0, got "+getAverageRank("Mason", null));
        useYearData();
    }

    /** Given a `name` and `gender` in some `year`, determine how many babies were
     *  born with a higher ranked name (meaning lower rank number) than `name`.
     *  
     *  @param year     The year to search for `name` to get its rank
     *  @param name     The baby name to search for
     *  @param gender   The gender to use in the search
     *  @return the total number of births of the same gender that have names
     *  more popular than `name`.  If `name` not found, return -1.
     *  @throws exception if `year` does not have a data file.
     */
    public int getTotalBirthsRankedHigher (int year, String name, String gender) {
        int total = -1;
        
        // Only bother searching if name and gender are actually present.
        if (hasValue(name) && hasValue(gender)) {
            // First we need to know what rank `name` has, and it must be valid.
            int rank = getRank(year, name, gender);
            if (rank != -1) {
                // We know we have at least 1 entry so total cannot be -1.
                // Reset it, then go sum the births up to but not including `rank`.
                total = 0;
                FileResource fr = new FileResource(getFilename(year));
                CSVParser parser = filterByGender(fr, gender).getCSVParser(false);
                for (CSVRecord rec : parser) {
                    if (parser.getRecordNumber() >= rank) break;
                    total = total + getCount(rec);
                }
            }
        }

        return total;
    }
    
    /** Test driver for getTotalBirthsRankedHigher(). */
    void testGetTotalBirthsRankedHigher () {
        useTestData();
        System.out.println("Expect 15, got "+getTotalBirthsRankedHigher(2012, "Ethan", MALE));
        System.out.println("Expect  0, got "+getTotalBirthsRankedHigher(2012, "Jacob", MALE));
        System.out.println("Expect -1, got "+getTotalBirthsRankedHigher(2012, "Supergirl", FEMALE));
        System.out.println("Expect -1, got "+getTotalBirthsRankedHigher(2012, "", MALE));
        System.out.println("Expect -1, got "+getTotalBirthsRankedHigher(2012, null, MALE));
        System.out.println("Expect -1, got "+getTotalBirthsRankedHigher(2012, "Ethan", ""));
        System.out.println("Expect -1, got "+getTotalBirthsRankedHigher(2012, "Ethan", null));
        useYearData();
    }

    //////////////////////////////////////////////////////////////////////////////////
    //// Quiz answer methods                                                      ////
    //////////////////////////////////////////////////////////////////////////////////   

}  // BabyBirths
