//package edu.duke;
import edu.duke.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import java.io.*;


/**
 * The <code>StorageResource</code> class stores any number of <code>String</code> objects and
 * allows access to these stored values one at a time, using the method <code>data</code>. These
 * strings can then be iterated over in the order they were added using a <code>for</code> loop.
 * 
 * <P>
 * This class mirrors an <code>ArrayList&lt;String&gt;</code> in some functionality, but is simpler
 * to use and fits the Duke/Coursersa model of creating and using iterables.
 * 
 * <P>
 * Example usage:
 * 
 * <PRE>
 * FileResource fr = new FileResource();
 * StorageResource store = new StorageResource();
 * for (String s : fr.words()) {
 *     store.add(s);
 * }
 * // can process store here, e.g.,
 * // get number of strings stored
 * int x = store.size();
 * for (String s : store.data()) {
 *     // print or process s
 * }
 * </PRE>
 * 
 * <P>
 * This software is licensed with an Apache 2 license, see
 * http://www.apache.org/licenses/LICENSE-2.0 for details.
 * 
 * @author Duke Software Team
 */
public class StorageResource {
    private List<String> myStrings;

    /**
     * Create an empty <code>StorageResource</code> object
     */
    public StorageResource () {
        myStrings = new ArrayList<String>();
    }

    /**
     * Create a <code>StorageResource</code> object, loaded with the Strings passed as parameters.
     */
    StorageResource (String... data) {
        myStrings = new ArrayList<String>(Arrays.asList(data));
    }

    /**
     * Create an <code>StorageResource</code> object that is a copy of another list.
     * 
     * @param other the original list being copied
     */
    public StorageResource (StorageResource other) {
        myStrings = new ArrayList<String>(other.myStrings);
    }

    /**
     * Remove all strings from this object so that <code>.size() == 0</code>.
     */
    public void clear () {
        myStrings.clear();
    }

    /**
     * Adds a string to this storage object.
     * 
     * @param s the value added
     */
    public void add (String s) {
        myStrings.add(s);
    }

    /**
     * Returns the number of strings added/stored in this object.
     * 
     * @return the number of strings stored in the object
     */
    public int size () {
        return myStrings.size();
    }

    /**
     * Determines if a string is stored in this object.
     * 
     * @param s string searched for
     * @return true if and only if s is stored in this object
     */
    public boolean contains (String s) {
        return myStrings.contains(s);
    }

    /**
     * Create and return an iterable for all strings in this object.
     * 
     * @return an <code>Iterable</code> that allows access to each string in the order stored
     */
    public Iterable<String> data () {
        return myStrings;
    }

    /**
     * Returns a <code>CSVParser</code> object to access the contents of this object as CSV data.
     *
     * Each string of this object should be formatted as data separated by commas and with a header row
     * to describe the column names.
     *
     * @return a <code>CSVParser</code> that can provide access to the records in this object one at a
     *         time
     * @throws exception if this object does not represent CSV formatted data
     */
    public CSVParser getCSVParser () {
        return getCSVParser(true);
    }

    /**
     * Returns a <code>CSVParser</code> object to access the contents of this object, possibly
     * without a header row.
     *
     * Each string of this object should be formatted as data separated by commas and with/without a
     * header row to describe the column names.
     *
     * @param withHeader uses first row of data as a header row only if true
     * @return a <code>CSVParser</code> that can provide access to the records in this object one at a
     *         time
     * @throws exception if this object does not represent CSV formatted data
     */
    public CSVParser getCSVParser (boolean withHeader) {
        return getCSVParser(withHeader, ",");
    }

    /**
     * Returns a <code>CSVParser</code> object to access the contents of this object, possibly
     * without a header row and a different data delimiter than a comma.
     *
     * Each line of this object should be formatted as data separated by the delimiter passed as a
     * parameter and with/without a header row to describe the column names. This is useful if the
     * data is separated by some character other than a comma.
     *
     * @param withHeader uses first row of data as a header row only if true
     * @param delimiter a single character that separates one field of data from another
     * @return a <code>CSVParser</code> that can provide access to the records in this object one at a
     *         time
     * @throws exception if this object does not represent CSV formatted data
     * @throws exception if <code>delimiter.length() != 1</code>
     */
    public CSVParser getCSVParser (boolean withHeader, String delimiter) {
        if (delimiter == null || delimiter.length() != 1) {
            throw new ResourceException("StorageResource: CSV delimiter must be a single character: " + delimiter);
        }
        try {
            char delim = delimiter.charAt(0);
            Reader input = new StringReader(String.join("\n", myStrings));
            if (withHeader) {
                return new CSVParser(input, CSVFormat.EXCEL.withHeader().withDelimiter(delim));
            }
            else {
                return new CSVParser(input, CSVFormat.EXCEL.withDelimiter(delim));
            }
        }
        catch (Exception e) {
            throw new ResourceException("StorageResource: data in this object is not CSV data.");
        }
    }

    /**
     * Allows access to the column names of the header row of CSV data (the first line in the
     * data) one at a time. If the CSV data did not have a header row, then an empty
     * <code>Iterator</code> is returned.
     *
     * @param parser the <code>CSVParser</code> that has been created for this object
     * @return an <code>Iterable</code> that allows access one header name at a time
     */
    public Iterable<String> getCSVHeaders (CSVParser parser) {
        return parser.getHeaderMap().keySet();
    }

}
