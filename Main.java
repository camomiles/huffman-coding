
/**
 * Main program for Huffman encoder.  Creates the Huffman processor and directs it to encode
 * the file given as the first command line argument and write to the file given as the
 * second command line argument.
 *
 * @author PAS
 * @version 7/10/11
 */
 
public class Main {
	/**
	 * Huffman coding
	 *
	 * @param args[0] file to encode
	 * @param args[1] file to write coded input file
	 */
	public static void main(String [] args) {
		HuffmanProcessor h = new HuffmanProcessor();

		if (args.length < 1)
			System.out.println("No filenames given.");
		else {
			h.processFile(args[0]);
			h.codeFile(args[0],args[1]);
		}
	}
}
