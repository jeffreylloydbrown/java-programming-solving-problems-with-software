
/**
 * Finding Many Genes:  this assignment is to write the code from
 * the lession to make the following improvements:
 * A. Find a gene in a strand of DNA where the stop codon is one of
 *    "TAA", "TAG" or "TGA".  Previously only looked for "TAA".
 * B. Find all the genes (where any of the 3 stop codons are used)
 *    in a strand of DNA.
 * 
 * @author Jeff Brown
 * @version 1
 */
public class Part1 {
    String START_CODON = "ATG";

    /** Locate the next valid stop codon after `startIndex` in `dna`.
     * @param dna   the DNA strand to search
     * @param startIndex   where to start searching
     * @param stopCodon    the codon to search for.
     * @returns the index of the first occurrence of `stopCodon`
     * past `startIndex` that is a multiple of 3 away from `startCodon`.
     * If not found, return dna.length().
     */
    public int findStopCodon (String dna, int startIndex, String stopCodon) {
        
        // Not found, so return the dna's length
        return dna.length();
    }
    
    /** Test driver for findStopCodon(). */
    public void testFindStopCodon () {
    }
    
    /** Locates the next valid gene sequence in `dna` and returns it.
     * @param dna   the DNA strand to search
     * @returns a valid gene sequence or "" if `dna` does not contain a gene.
     */
    public String findGene (String dna) {
        
        // Not found, return "".
        return "";
    }
    
    /** Test driver for findGene() */
    public void testFindGene() {
    }
    
    /** Given a DNA strand `dna`, print each gene it contains to
     * standard output.
     * @param dna   the DNA strand to search
     */
    public void printAllGenes (String dna) {
    }
}
