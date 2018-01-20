
/**
 * Finding a Gene - Using the Simplified Algorithm
 * 
 * @author Jeff Brown 
 * @version 1
 */
public class Part1 {
    /** Extract the gene sequence from the DNA strand.
     * 
     * @param dna   a String of nucleotides that may or may not contain a gene.
     * @returns     the first gene found, including start and stop codon.  If 
     *              no gene found, returns an empty string.
     */
    public String findSimpleGene (String dna) {
        // these are constants
        String START_CODON = "ATG";
        String STOP_CODON = "TAA";
        
        // Find the first occurrence of the start codon
        // if we don't find it, then we don't have a gene
        // so return an empty string.
        int startIndex = dna.indexOf(START_CODON);
        if (startIndex == -1) {
            return "";
        }
        
        // Find the first occurrence of the stop codon
        // after the start codon.  Remember we know the stop
        // codon cannot appear within the start codon, so 
        // start looking 3 characters (length of START_CODON)
        // past the start index.  If we don't find the stop codon,
        // we also don't have a gene so return an empty string.
        int stopIndex = dna.indexOf(STOP_CODON, startIndex+3);
        if (stopIndex == -1) {
            return "";
        }
        
        // Now confirm the gene has the correct length.  It has
        // to be a multiple of 3.  If not, return an empty string.
        if ((stopIndex+3 - startIndex) % 3 != 0) {
            return "";
        }
        
        // Pull the gene out of the DNA string.  Remember we need
        // to include the full stop codon, so we go 3 past the stop
        // index.        
        return dna.substring(startIndex, stopIndex+3);
    }
    
    /** Test driver for findSimpleGene(). */
    void testSimpleGene() {
        String dna;
        
        // Test 1: no start codon, so no gene
        dna = "TAAGTA";
        System.out.println("dna = " + dna);
        System.out.println("gene = '" + findSimpleGene(dna) + "' expect ''");

        // Test 2: no stop stop codon, so no gene
        dna = "ATGTTTCCCTAT";
        System.out.println("dna = " + dna);
        System.out.println("gene = '" + findSimpleGene(dna) + "' expect ''");
        
        // Test 3: no codons at all, so no gene
        dna = "ATAGGGACG";
        System.out.println("dna = " + dna);
        System.out.println("gene = '" + findSimpleGene(dna) + "' expect ''");
        
        // Test 4: valid codons, but no other nucleitides.  Will be seen
        // as valid, for now.
        dna = "ATGTAA";
        System.out.println("dna = " + dna);
        System.out.println("gene = '" + findSimpleGene(dna) + "' expect ATGTAA");
        
        // Test 5: valid codons, but not in expected order.  No gene.
        dna = "TAAATGTAC";
        System.out.println("dna = " + dna);
        System.out.println("gene = '" + findSimpleGene(dna) + "' expect ''");       
        
        // Test 6:  valid codons, but gene isn't right length.  No gene.
        dna = "AATGCCTAAC";
        System.out.println("dna = " + dna);
        System.out.println("gene = '" + findSimpleGene(dna) + "' expect ''");
        
        // Test 7:  valid codons, correct length.  Result is ATGCCCTAA.
        dna = "CATGCCCTAAG";
        System.out.println("dna = " + dna);
        System.out.println("gene = '" + findSimpleGene(dna) + "' expect ATGCCCTAA");
    }
}
