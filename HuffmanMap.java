import java.util.*;

/**
 * Implementation of the Map interface to take advantage of perfect hash of keys.
 *
 * This map stores byte values and their corresponding Huffman codes.  Any map implementation
 * would be sufficient, but because the byte values form a perfect hash in a small
 * integral range, they can be used as indices into an array for better effiency.
 *
 * This introduces a problem with generic type arrays, so an unchecked type cast is
 * required.  Restricted usage to this class makes the cast safe.
 *
 * @author PAS
 * @version 7/10/11
 */

public class HuffmanMap extends AbstractMap<Integer, List<Boolean>> {
	// byte to Huffman code map.  Huffman codes represented as lists of booleans
	private List[] map;

	/**
	 * Make a new Huffman Map of the given size.
	 *
	 * @param size number of byte values to encode
	 */
	public HuffmanMap(int size) {
		map = new Vector[size];
	}

	/**
	 * @{inheritDoc}
	 * This method is required by the Map interface, but not used in the Huffman coder
	 * so a dummy implementation is given.
	 */
	public Set<Map.Entry<Integer, List<Boolean>>> entrySet() {
		return null; }

	/**
	 * @{inheritDoc}
	 */
	public List<Boolean> put(Integer c, List<Boolean> a) {
		map[c] = a;
		return null;
	}

	/**
	 * @{inheritDoc}
	 */
	public List<Boolean> get(Object o) {
		try {
			return get((Integer) o);
		} catch (ClassCastException e) { e.printStackTrace(); }
		return null;
	}
		
	/**
	 * @{inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public List<Boolean> get(Integer c) {
		return (List<Boolean>) map[c];
	}
}


