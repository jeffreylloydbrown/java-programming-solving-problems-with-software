
/**
 * HowMany - Finding Multiple Occurrences 
 * How many occurrences of a string appear in another string, without overlapping.
 * 
 * @author Jeff Brown
 * @version 1
 */
public class Part2 {

    /** Determine how many times `stringa` appears in `stringb`, where each occurrence of
     *  `stringa` must not overlap with another occurrence of it.
     *  
     *  @param stringa  the string value to search for
     *  @param stringb  the string to search within
     *  @returns the count of non-overlapping occurrences.  For example,
     *  howMany("aa", "ataaaa") returns 2.  If no occurrences found, returns zero.
     */
    public int howMany (String stringa, String stringb) {
        // Check parameters.  If either empty, answer is 0.
        if (stringa.isEmpty() || stringb.isEmpty()) return 0;
        
        // OK, count occurrences.
        int count = 0;
        int idx = stringb.indexOf(stringa, 0);
        while (idx >= 0) {
            count = count + 1;
            // Careful: advance by the length of stringa to avoid overlap
            idx = stringb.indexOf(stringa, idx+stringa.length());
        }
        return count;
    }
    
    /** Test driver for howMany() */
    public void testHowMany() {
        int count;
        count = howMany("GAA", "ATGAACGAATTGAATC");
        if (count != 3) System.out.println("case 1 failed, expected 3 got "+count);
        count = howMany("aa", "ataaaa");
        if (count != 2) System.out.println("case 2 failed, expected 2 got "+count);
        count = howMany("a", "ataaaa");
        if (count != 5) System.out.println("case 3 failed, expected 5 got "+count);
        count = howMany("a", "");
        if (count != 0) System.out.println("case 4 failed, expected 0 got "+count);
        count = howMany("a", "bbb");
        if (count != 0) System.out.println("case 5 failed, expected 0 got "+count);
        count = howMany("", "bb");
        if (count != 0) System.out.println("case 6 failed, expected 0 got "+count);
        System.out.println("tests finished");
    }
}
