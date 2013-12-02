import mp.Port;

import javax.swing.*;
import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * This class directs the calculation of bytes frequency for given file of folder.
 * It is created as background swing task, and as calculation goes, it updates filesLabel and progress bar.
 *
 * @author Roman
 * @version 2/12/13
 */

public class HuffmanCentralProcessor extends SwingWorker {

    // Total frequency for every byte in all files in folder.
    private int[] finalFrequency;
    // Map of byte-values to their Huffman codes.
    private Map<Integer, List<Boolean>> codesMap;
    // List of files to process.
    private List<File> files;
    // Links to GUI elements for updating.
    private JLabel filesLabel;
    private JProgressBar progressBar;

    /**
     * Create new Huffman central processor.
     */
    public HuffmanCentralProcessor(List<File> fs, JLabel fL, JProgressBar pB) {
        files = fs;
        filesLabel = fL;
        progressBar = pB;
    }

    /**
     * This method describes Huffman coding background task.
     * It creates new Huffman processor for every file in the list of files ( in new thread each )
     * and sum up frequencies they return using Port.
     *
     * Then, it encodes map of bytes to their Huffman codes.
     */
    protected Object doInBackground()  {
        // Create uni-directional many to one channel to this class.
        Port<int[]> port = new Port<int[]>();

        int filesLeft = files.size();
        // Create Huffman processor thread for every file.
        for (int i = 0; i < filesLeft; i++ ) {
            File tempFile = files.get(0);
            new HuffmanProcessor(tempFile, port);
        }

        finalFrequency = null;
        // Wait for frequencies and sum them up.
        while (filesLeft != 0) {
            try {
                int[] tempFreq = port.receive();

                if (finalFrequency == null) {
                    finalFrequency = tempFreq;
                    // When first frequency returned - switch progress bar back to determinate state.
                    progressBar.setIndeterminate(false);
                } else {
                    for (int i = 0; i < tempFreq.length; i++) {
                        finalFrequency[i] = finalFrequency[i] + tempFreq[i];
                    }
                }

                filesLeft = filesLeft - 1;
                // Decrease label value
                filesLabel.setText(filesLeft + " files left to process.");

                // Make step in progress bar
                progressBar.setValue(progressBar.getValue() + 1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Create map of Huffman codes using total frequency
        codeHuffmanTable(finalFrequency);
        // Update label when the job is done.
        filesLabel.setText("Done! (" + progressBar.getMaximum() + " files) " );

        return 0;
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

        AbstractPriorityQueue<HuffmanTree> pq = new ConcretePriorityQueue<HuffmanTree>();
        codesMap = new HuffmanMap(257);
        HuffmanTree hTree = null;

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
            hTree.encode(codesMap);


        } catch (NoSuchElementException e) {
            // This should never happen.  If it does, something's wrong with the queue implementation.
            e.printStackTrace();
        }

    }
}