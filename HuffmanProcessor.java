
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Vector;

/**
 * HuffmanProcessor:  top-level class for directing the Huffman encoding operations.
 *
 * This class directs the calculation of Huffman codes.
 *
 *    - Character information and resulting Huffman codes are stored in a (Huffman) Map
 *    - This class manipulates a priority queue of Huffman trees to calculate the codes
 *
 * Interface:
 *    - processFile(String): read the characters in the given file and calculate the Huffman coding tree
 *    - codeFile(String, String): encode an input file according to the code tree and write to output file
 *
 *
 * @author PAS
 * @version 7/10/11
 */


public class HuffmanProcessor implements Runnable {
	// Need to encode this many symbols
	private static final int BYTESIZE = (int) Math.pow(2,8);

	// Special end-of-file symbol outside the range of ordinary symbols
	private static final int EOF = BYTESIZE;

	// Huffman characters and codes are stored in a map for writing output file
    private Map<Integer, List<Boolean>> hTab;

	// Huffman code tree created using a priority queue
    private AbstractPriorityQueue<HuffmanTree> pq;

	// Huffman code tree: characters to code in leaf nodes, less frequently occurring characters deeper in tree
	private HuffmanTree hTree;

    private File file;

    private Selectable<int[]> port;

	/**
	 * Create a new Huffman processor.  A Huffman processor provides methods to determine the coding for a given file
	 * and write an encoded output file.
	 */
    public HuffmanProcessor(File f, Selectable<int[]> p) {
		hTab = new HuffmanMap(BYTESIZE+1);
		pq = new ConcretePriorityQueue<HuffmanTree>();
		hTree = null;

        file = f;
        port = p;

        run();
	}


	/**
	 * Process the file with the given path: read all the characters, counting their frequency of occurrence
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

        //codeHuffmanTable(charFreq);

        port.send(charFreq);
    }


  	// Perform the Huffman encoding for the given the character frequencies 
  	//
  	// Algorithm:
  	//    - place each character in a single-node (binary) Huffman tree with its frequency
  	//    - put all these trees into a priority queue, prioritised on their frequency
  	//    - take them off in pairs and make a new tree with these as the leaves, and the combined frequency of the children
  	//    - re-insert into the priority queue
  	//    - when only one tree is left in the priority queue, it is the final Huffman tree
  	//
  	//    - encode the characters in the final Huffman tree and store codes in Huffman map
	//
	// @param array of frequencies for given index byte value
  	//
    private void codeHuffmanTable(int [] charFreq) {
		// First stage: insert a single element HUffman tree into the queue for each character
        for (char c = 0; c < charFreq.length; c++)
            if (charFreq[c] > 0)
                pq.enqueue(new HuffmanTree(c, charFreq[c]));
            
        try {
            // Second stage: iteratively process the priority queue, making new Huffman trees from the first two elements until only one tree remains in the queue
            while ( pq.size() > 1 ) {
                HuffmanTree first = pq.front();
                pq.dequeue();
                HuffmanTree second = pq.front();
                pq.dequeue();
                HuffmanTree newTree = new HuffmanTree('\u0000', first.frequency()+second.frequency(), first, second);
                pq.enqueue(newTree);
            }

            // Third stage: when the full Huffman tree is built, the coding can be given to each character in it
            hTree = pq.front();
            hTree.encode(hTab);

        } catch (NoSuchElementException e) {
        // This should never happen.  If it does, something's wrong with the queue implementation.
            e.printStackTrace();
        }

    }


	/**
	 * Encode an input file and write to an output file according to this processor's Huffman map.
	 *
	 * @param inFile path to input file
	 * @param outFile path to output file to write/create
	 */
    public void codeFile(String inFile, String outFile) {
		// The output is accumulated as a single list of booleans
		List<Boolean> output = new Vector<Boolean>();

		try {
            FileInputStream fr = new FileInputStream(inFile);

            int c = fr.read();

            while ( c >= 0 ) {
				List<Boolean> bs = hTab.get(c);
				output.addAll(bs);
				//output.addAll(hTab.get(c));
                c = fr.read();
            }

			output.addAll(hTab.get(EOF));

            fr.close();

			// Write the output list to the file
			FileIO.write(outFile, output);

        } catch (IOException ioe) { ioe.printStackTrace(); }
    }
}
