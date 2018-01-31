
/**
 * Add a gene processor to our tools.  It
 * - prints all the genes longer than 9 nucleotides
 * - prints the count of genes longer than 9 nucleotides
 * - prints the genes with CG ratio > 0.35
 * - prints the count of high CG ratio genes 
 * - prints the length of the longest gene
 * 
 * @author Jeff Brown
 * @version 1
 */
public class Part3 {
    /** Return the ratio of Cs and Gs in `dna` as a fraction
     *  of the entire strand of DNA.
     *  
     *  For example, with "ATGCCATAG", the ratio is 4/9 or .444
     *  
     *  @param dna      the DNA strand to process
     *  @returns the ratio of C & G nucleotides in `dna`
     */
    public double cgRatio (String dna) {
        int cgCount = 0;
        
        // Check parameter to avoid divide by zero.
        if (dna.isEmpty()) return 0.0;
        
        // Convert string to a collection of characters, then loop 
        // over it.  
        for (Character n : dna.toCharArray()) {
            n = Character.toUpperCase(n);
            if (n == 'C' || n == 'G') {
                cgCount = cgCount + 1;
            }
        }
        return (double) cgCount/dna.length();
    }
    
    /** Test driver for cgRatio */
    public void testCGRatio() {
        double ratio = cgRatio("ATGCCATAG");
        if (ratio <= 0.44444 || ratio >= 0.44445)
            System.out.println("expected 0.44444444, got "+ratio);
        ratio = cgRatio("ABDE");
        if (ratio != 0.0) System.out.println("expected 0.0, got "+ratio);
        ratio = cgRatio("");
        if (ratio != 0.0) System.out.println("expected 0.0, got "+ratio);
        System.out.println("tests finished");
    }
    
    /** Determine how many times the codon "CTG" appears in a DNA strand.
     *  
     *  @param dna  the DNA strand to search
     *  @returns the count of "CTG" codons.  Returns 0 if no occurrences
     *  or if `dna` is empty.
     */
    public int countCTG (String dna) {
        // Check parameters.  No dna, no count.
        if (dna.isEmpty()) return 0;
        
        // OK, count occurrences.
        int count = 0;
        String CTG = "CTG";
        int idx = dna.indexOf(CTG, 0);
        while (idx >= 0) {
            count = count + 1;
            // Careful: advance by the length of the codon to avoid overlap
            idx = dna.indexOf(CTG, idx+CTG.length());
        }
        return count;
    }
    
    /** Test driver for countCTG() */
    public void testCountCTG() {
        int count;
        count = countCTG("ATGCTGCTGTAACTG");
        if (count != 3) System.out.println("case 1 failed, expected 3 got "+count);
        count = countCTG("ATGTAA");
        if (count != 0) System.out.println("case 2 failed, expected 0 got "+count);
        count = countCTG("");
        System.out.println("tests finished");
    }
}  // class Part3
