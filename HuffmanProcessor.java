
import mp.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;


/**
 * This class calculates of byte frequency for given file.
 *
 * Interface:
 *    - processFile(File, Port): read the characters in the given file and calculate byte frequencies, then return frequencies array
 *    through given port.
 *
 * @author Roman
 * @version 2/12/13
 */


public class HuffmanProcessor implements Runnable {
	// Need to encode this many symbols
	private static final int BYTESIZE = (int) Math.pow(2,8);

	// Special end-of-file symbol outside the range of ordinary symbols
	private static final int EOF = BYTESIZE;

    // Link to the proceeded file;
    private File file;

    // Communication channel back to Huffman Central Processor
    private Port<int[]> port;

	/**
	 * Create a new Huffman processor.  A Huffman processor provides methods to determine the coding for a given file
	 * and write an encoded output file.
	 */
    public HuffmanProcessor(File f, Port<int[]> p) {
        file = f;
        port = p;

        run();
	}


	/**
	 * Process the file: read all the characters, counting their frequency of occurrence
	 * then build the Huffman tree.
	 *
	 */
    public void run() {
        int[] charFreq = new int[BYTESIZE+1];

        try {
            FileInputStream fr = new FileInputStream(file);

            int c = fr.read();
            
            while ( c >= 0 ) {
                charFreq[c]++;
                c = fr.read();
            }

			// record EOF character
			charFreq[EOF] = 1;
    
            fr.close();

        } catch (IOException ioe) { ioe.printStackTrace(); }

        // Send frequencies back
        port.send(charFreq);
    }
}
