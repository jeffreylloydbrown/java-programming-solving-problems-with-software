
/**
 * How Many Genes?  Write a program to count how many genes
 * are in a strand of DNA.
 * 
 * This is where I wish I could submit code like the Scala,\
 * classes, to get execution on very large predefined strands.
 * 
 * @author Jeff Brown
 * @version 1
 */
public class Part3 {
    String START_CODON = "ATG";

    /** Locate the next valid stop codon after `startIndex` in `dna`.
     * @param dna   the DNA strand to search
     * @param startIndex   where to start searching
     * @param stopCodon    the codon to search for.
     * @returns the index of the first occurrence of `stopCodon`
     * past `startIndex` that is a multiple of 3 away from `startCodon`.
     * If not found, return dna.length() (which works well with searching
     * for a minimum value, as dna.length() is always greater than any
     * possible stop codon index.)
     */
    public int findStopCodon (String dna, int startIndex, String stopCodon) {
        // Check parameters.  If any are not valid, return "not found"
        // (which is dna.length()).
        if (dna.isEmpty() || stopCodon.isEmpty() || 
            startIndex < 0 || startIndex >= dna.length()) {
            return dna.length();
        }
        
        // Keep searching for stop codons until none found.  When
        // one is found, if it is a multiple of 3 away from startIndex
        // return that index.  If not, keep searching at the next letter.
        int currIndex = dna.indexOf(stopCodon, startIndex+3);
        while (currIndex != -1) {
            if ((currIndex - startIndex) % 3 == 0) { // found
                return currIndex;
            } else {  // not found
                currIndex = dna.indexOf(stopCodon, currIndex+1);
            }
        }
        
        // Not found, so return the dna's length
        return dna.length();
    }
    
    /** Locates the next valid gene sequence in `dna` and returns it.
     * @param dna   the DNA strand to search
     * @returns a valid gene sequence or "" if `dna` does not contain a gene.
     */
    public String findGene (String dna) {
        if (dna.isEmpty()) return "";

        // Find the start of the next gene.  If none, return "".
        int startIndex = dna.indexOf(START_CODON);
        if (startIndex < 0) return "";

        // Find each of the next possible stop codons.            
        int taaIndex = findStopCodon(dna, startIndex, "TAA");
        int tagIndex = findStopCodon(dna, startIndex, "TAG");
        int tgaIndex = findStopCodon(dna, startIndex, "TGA");

        // Which of the 3 is closest to startIndex?  This is where
        // returning the dna length for "not found" helps:  it will
        // automatically be larger than any real codon index.
        int minIndex = taaIndex;
        if (tagIndex < minIndex) minIndex = tagIndex;
        if (tgaIndex < minIndex) minIndex = tgaIndex;
        
        // Return the gene between startIndex and minIndex+3.  But if
        // minIndex is dna.length(), there was no gene found so return "";
        if (minIndex < dna.length()) {
            return dna.substring(startIndex, minIndex+3);
        } else {
            return "";
        }
    }
    
    /** Given a DNA strand `dna`, print each gene it contains to
     * standard output.
     * @param dna   the DNA strand to search
     */
    public void printAllGenes (String dna) {
        String gene;
        System.out.println("dna: "+dna);
        System.out.println("genes:");
        while (true) {
            gene = findGene(dna);
            if (gene.isEmpty()) { // all done
                break;
            } else {
                // Found a gene.  Print it.  Then update dna
                // to be the substring AFTER the end of the
                // located gene.
                System.out.println(gene);
                dna = dna.substring(dna.indexOf(gene)+gene.length(), dna.length());
            }
        }
        System.out.println("done");
    }
    
    /** Count the number of genes found in a DNA strand.
     * @param dna   the DNA strand to examine
     * @returns the number of genes found.  Returns 0 if no genes found.
     */
    public int countGenes(String dna) {
        String gene;
        int count = 0;
        while (true) {
            gene = findGene(dna);
            if (gene.isEmpty()) { // all done
                break;
            } else {
                // Found a gene.  Bump count.  Then update dna
                // to be the substring AFTER the end of the
                // located gene.
                count = count + 1;
                dna = dna.substring(dna.indexOf(gene)+gene.length(), dna.length());
            }
        }
        return count;
    }
    
    /** Test driver for countGenes() */
    public void testCountGenes() {
        int count;
        count = countGenes("");
        if (count != 0) System.out.println("count on empty dna was not zero, count = "+count);
        count = countGenes("ABCDE");
        if (count != 0) System.out.println("count on non-DNA string was not zero, count = "+count);
        count = countGenes(START_CODON+"CCC");
        if (count != 0) System.out.println("count with no stop codons was not zero, count = "+count);
        count = countGenes(START_CODON+"TAA");
        if (count != 1) System.out.println("count with 1 gene not 1, count = "+count);
        count = countGenes("ATGTAAGATGCCCTAGT");
        if (count != 2) System.out.println("homework case 1 should be 2, count = "+count);
        System.out.println("tests finished");
    }
}
