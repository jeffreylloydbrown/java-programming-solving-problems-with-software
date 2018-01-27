import edu.duke.*;

/**
 * Write the code from the lesson to use a StorageResource to store
 * genes you find instead of printing them out.
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
    
    private void testFSC(String desc, String dna, int startIndex, String stopCodon, int expected) {
        int index = findStopCodon(dna, startIndex, stopCodon);
        if (index == expected) {
            System.out.println("PASS: "+desc+": dna='"+dna+"'");
        } else {
            System.out.println("FAIL: "+desc+": dna='"+dna+"' expected "+expected+", got "+index);
        }
    }
    
    /** Test driver for findStopCodon(). */
    public void testFindStopCodon () {
        String dna = "xxxyyyzzzTAAxxxyyyzzzTAAxx";
        testFSC("test 1", dna, 0, "TAA", 9);
        testFSC("test 2", dna, 9, "TAA", 21);
        testFSC("test 3", dna, 1, "TAA", dna.length());
        testFSC("test 4", dna, 0, "TAG", dna.length());
        testFSC("empty dna", "", 0, "TAG", 0);
        testFSC("empty stop codon", dna, 0, "", dna.length());
        testFSC("negative start index", dna, -1, "TAA", dna.length());
        testFSC("start index too large", dna, dna.length(), "TAA", dna.length());
        testFSC("smallest gene", "ATGTAA", 0, "TAA", 3);
        testFSC("2 TAA, one not in right place", "ATGCTAACCTAA", 0, "TAA", 9);
        testFSC("multiple stop codons TAG", "ATGTAGTGATAA", 0, "TAG", 3);
        testFSC("multiple stop codons TGA", "ATGTAGTGATAA", 0, "TGA", 6);
        testFSC("multiple stop codons TAA", "ATGTAGTGATAA", 0, "TAA", 9);
        System.out.println("tests completed");
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
    
    private void testFG(String desc, String dna, String expected) {
       String gene = findGene(dna);
        if (gene.equals(expected)) {
            System.out.println("PASS: "+desc+": dna='"+dna+"'");
        } else {
            System.out.println("FAIL: "+desc+": dna='"+dna+"' expected '"+expected+"', got '"+gene+"'");
        }
    }
    
    /** Test driver for findGene() */
    public void testFindGene() {
        testFG("smallest gene", "ATGTAA", "ATGTAA");
        testFG("2 TAA, one not in right place", "ATGCTAACCTAA", "ATGCTAACCTAA");
        testFG("multiple stop codons TAG first", "ATGTAGTGATAA", "ATGTAG");
        testFG("multiple stop codons TGA valid", "ATGCTAGCCTGATGA", "ATGCTAGCCTGA");
        testFG("multiple stop codons TAA first", "ATGCCCTAATAGTGA", "ATGCCCTAA");
        testFG("multiple stop codons TGA first", "ATGCCCTGATAGTAA", "ATGCCCTGA");
        testFG("multiple stop codons TAG first", "ATGCCCTAGTAATGA", "ATGCCCTAG");
        testFG("empty dna", "", "");
        testFG("no gene", "CCCTGA", "");
        System.out.println("tests completed");
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
    
    /** Test driver for printAllGenes */
    public void testPrintAllGenes() {
        printAllGenes("ATGTAAGATGCCCTAGT");
        System.out.println("should see 2 genes, 'ATGTAA' and 'ATGCCCTAG'");
        printAllGenes("ABCDE");
        System.out.println("should see no genes");
        printAllGenes("");
        System.out.println("should see no genes because dna was empty");
    }
    
    /** Given a DNA strand `dna`, extract genes in it and put
     * into a StorageResource object.
     * 
     * @param dna   the DNA strand to search
     * @returns a StorageResource object containing the genes in `dna`.
     */
    public StorageResource getAllGenes (String dna) {
        String gene;
        StorageResource sr = new StorageResource();
        while (true) {
            gene = findGene(dna);
            if (gene.isEmpty()) { // all done
                break;
            } else {
                // Found a gene.  Remember it.  Then update dna
                // to be the substring AFTER the end of the
                // located gene.
                sr.add(gene);
                dna = dna.substring(dna.indexOf(gene)+gene.length(), dna.length());
            }
        }
        return sr;
    }
    
    private void testGAG(String dna, String expectedMsg) {
        System.out.println("dna: '"+dna+"'");
        StorageResource sr = getAllGenes(dna);
        for (String s: sr.data()) System.out.println("  "+s);
        System.out.println(expectedMsg);
    }
    
    /** Test driver for getAllGenes() */
    public void testGetAllGenes() {
        testGAG("ATGTAAGATGCCCTAGT", "should see 2 genes, 'ATGTAA' and 'ATGCCCTAG'");
        testGAG("ABCDE", "should see no genes");
        testGAG("", "should see no genes because dna was empty");
    }
} // Part1

