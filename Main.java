import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

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
		//HuffmanProcessor h = new HuffmanProcessor();



        // Create new GUI

        GUI userInterface = new GUI();

        // Pop up JFileChooser

//        JFileChooser chooser = new JFileChooser();
//        FileNameExtensionFilter filter = new FileNameExtensionFilter(
//                "JPG & GIF Images", "jpg", "gif");
//        chooser.setFileFilter(filter);
//        int returnVal = chooser.showOpenDialog(parent);
//        if(returnVal == JFileChooser.APPROVE_OPTION) {
//            System.out.println("You chose to open this file: " +
//                    chooser.getSelectedFile().getName());
//        }

        //Create a file chooser
//        final JFileChooser fc = new JFileChooser();
//        //...
//        //In response to a button click:
//        int returnVal = fc.showOpenDialog(aComponent);

//		if (args.length < 1)
//			System.out.println("No filenames given.");
//		else {
//			h.processFile(args[0]);
//			h.codeFile(args[0],args[1]);
//		}
	}
}
