
/**
 * Problem Solving with Strings
 * 
 * @author Jeff Brown 
 * @version 1
 */
public class Part3 {
    /** Determine if `stringa` occurs at least 2 times in `stringb`.  Overlaps are allowed.
     * @param stringa   the string to search for
     * @param stringb   the string being searched
     * @returns true if `stringa` appears at least 2 times in `stringb`, false otherwise
     */
    public boolean twoOccurrences (String stringa, String stringb) {
        int first = stringb.indexOf(stringa);
        if (first == -1) return false;
        // If first is the last character, there cannot be another stringa so return false.
        if (first + 1 > stringb.length()) return false;
        // Now look for the second occurrence.
        int second = stringb.indexOf(stringa, first+1);
        return second != -1;        
    }
    
    /** Test driver for Part3. */
    void testing() {
        testCase("by", "A story by Abby Long", true);
        testCase("a", "banana", true);
        testCase("atg", "ctgtatgta", false);
        testCase("aa", "aa", false);
        testCase("aa", "aaa", true);    // because overlapping allowed
        
        lastPartTestCase("an", "banana", "ana");
        lastPartTestCase("zoo", "forest", "forest");
        lastPartTestCase("", "forest", "forest");
        lastPartTestCase("zoo", "", "");
    }
    
    private void testCase (String stringa, String stringb, boolean expected) {
        System.out.println("2+ of '"+stringa+"' in '"+stringb+"' is "+twoOccurrences(stringa, stringb)+" expected "+expected);
    }
    
    private void lastPartTestCase (String stringa, String stringb, String expected) {
        System.out.println("part of '"+stringb+"' after '"+stringa+"' is '"+lastPart(stringa, stringb)+"' expected '"+expected+"'");
    }
    
    /** Find the first occurrence of `stringa` in `stringb`, and returns 
     *  the part of `stringb` that follows `stringa`.  If `stringa` is not
     *  in `stringb`, all of `stringb` is returned.
     * @param stringa   the string to look for
     * @param stringb   the string being searched
     */
    public String lastPart (String stringa, String stringb) {
        int index = stringb.indexOf(stringa);
        if (index == -1) {
            return stringb;
        } else {
            return stringb.substring(index+stringa.length());
        }
    }
}
