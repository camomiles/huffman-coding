import mp.Port;

import javax.swing.*;
import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public class HuffmanCentralProcessor extends SwingWorker {

    int[] finalFrequency;
    Map<Integer, List<Boolean>> codesMap;
    List<File> files;
    JLabel filesLabel;
    JProgressBar progressBar;


    public HuffmanCentralProcessor(List<File> fs, JLabel fL, JProgressBar pB) {
        files = fs;
        filesLabel = fL;
        progressBar = pB;
    }

    /**
     *
     * @return null
     * @throws Exception
     */
    protected Object doInBackground() throws Exception {

        Port<int[]> port = new Port<int[]>();

        int filesLeft = files.size();
        Thread[] processorThreads = new Thread[filesLeft];

        for (int i = 0; i < filesLeft; i++ ) {
            File tempFile = files.get(0);
            processorThreads[i] = new Thread( new HuffmanProcessor(tempFile, port) );
            processorThreads[i].start();
        }


        finalFrequency = null;

        while (filesLeft != 0) {

            try {
                int[] tempFreq = port.receive();


                if (finalFrequency == null) {
                    finalFrequency = tempFreq;
                    progressBar.setIndeterminate(false);
                } else {
                    for (int i = 0; i < tempFreq.length; i++) {
                        finalFrequency[i] = finalFrequency[i] + tempFreq[i];
                    }
                }

                filesLeft = filesLeft - 1;
                // Decrease label value;
                filesLabel.setText(filesLeft + " files left to process.");

                // Make step in progress bar
                progressBar.setValue(progressBar.getValue() + 1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        codesMap = codeHuffmanTable(finalFrequency);

        filesLabel.setText("Done! (" + progressBar.getMaximum() + " files) ");

        return 0;
    }


    private Map<Integer, List<Boolean>> codeHuffmanTable(int [] charFreq) {

        AbstractPriorityQueue<HuffmanTree> pq = new ConcretePriorityQueue<HuffmanTree>();
        Map<Integer, List<Boolean>> hTab = new HuffmanMap(257);
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
            hTree.encode(hTab);

            return hTab;

        } catch (NoSuchElementException e) {
            // This should never happen.  If it does, something's wrong with the queue implementation.
            e.printStackTrace();
        }

        return null;

    }
}