import java.io.*;
import java.util.*;

/**
 * Utility class for interacting with files in terms of _bit_ sequences, represented
 * as Lists of Boolean values.
 *
 * @author PAS
 * @version 7/10/11
 */

public class FileIO {
	/**
	 * Convert a list of booleans into the corresponding byte array.
	 * The last byte in the array is padded with 0's to make up a
	 * full byte length.
	 *
	 * @param a list of booleans to convert
	 * @return byte array corresponding to a
	 */
	private static byte[] toByteArray(List<Boolean> a) {
		// 8 bits per byte in a, plus extra full byte if a not an
		// exact multiple of 8
		int outSize = a.size()/8 + ( (a.size()%8 == 0) ? 0 : 1);
		byte [] out = new byte[outSize];
		int outIndex = 0;
		int byteCounter = 0;
		byte outByte = 0;

		for (boolean b : a) {
			byteCounter++;
			outByte <<= 1;	// shift left 1 bit	
			if (b)
				outByte |= 1;	// set rightmost bit to 1


			// when byte full, put into out array
			if (byteCounter == 8) {
				byteCounter = 0;
				out[outIndex] = outByte;
				outIndex++;
				outByte = 0;
			}
		}

		// pad last byte if required
		if (byteCounter != 0) {
				outByte <<= (8-byteCounter);
				out[outIndex] = outByte;
		}

		return out;
	}


	/**
	 * Write the byte sequence corresponding to a list of booleans to file.
	 *
	 * @param a list of booleans to write
	 */
	public static void write(String filename, List<Boolean> a) {
		try {
			FileOutputStream f = new FileOutputStream(filename);
			f.write(toByteArray(a));
			f.close();
		} catch (IOException e) { e.printStackTrace(); }
	}


	/**
	 * Convert a byte array to a list of booleans representing the bit sequence
	 * of the bytes in the array concatenated.
	 *
	 * @param a byte array to decode
	 * @return list of boolean values corresponding to bits in a
	 */
	private static List<Boolean> toBooleanList(byte [] a) {
		List<Boolean> bs = new Vector<Boolean>();

		for (byte b : a)
			for (int i = 0; i < 8; i++)
				bs.add( ((b<<i)&128) != 0 );  // examine leftmost bit and shift left 8 times

		return bs;
	}


	/**
	 * Read the contents of the given file as a boolean list representing
	 * the bits contained in the file.
	 *
	 * @param filename path to file to read
	 * @return list of booleans encoding bit data in file
	 *
	 * Note: this method reads the whole file as one chunk.  This is not
	 * practical for large files.
	 */
	public static List<Boolean> read(String filename) {
		try {
			File f = new File(filename);
			FileInputStream fi = new FileInputStream(f);
			byte [] a = new byte[(int) f.length()];
			fi.read(a);
			fi.close();
			return toBooleanList(a);
		} catch (IOException e) { e.printStackTrace(); }

		return null;
	}
}
