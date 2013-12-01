import java.util.Map;
import java.util.List;
import java.util.Vector;

/**
 * A Huffman Coding tree.
 *
 * A Huffman tree is a binary tree structure, each node storing a character and frequency.
 * Leaf nodes represent a single character and its frequency of occurrence.
 * Internal nodes represent groupings of characters in the subtrees.  A dummy character value is used.
 *
 * "Characters" are represented as int values so that they encoding can be done using
 * unicode if desired, but will otherwise work with single byte characters or just
 * byte values.	
 *
 * @author PAS
 * @version 7/7/05
 */

// External nodes in a Huffman tree store characters and frequencies, internal nodes in the tree store frequencies

class HuffmanTree implements Comparable<HuffmanTree> {
  // character value of this node
  private int ch;

  // frequency of occurrence of character
  private int freq;

  // left and right subtrees
  private HuffmanTree left;
  private HuffmanTree right;

  /**
   * Make a new Huffman tree for the given character and frequency with the given child trees
   *
   * @param c character for this node
   * @param f frequency for this node
   * @param l left subtree
   * @param r right subtree
   */
  public HuffmanTree(int c, int f, HuffmanTree l, HuffmanTree r) { ch = c; freq = f; left = l; right = r; }

  /**
   * Make a new Huffman tree for the given character and frequency with no children
   *
   * @param c character for this node
   * @param f frequency for this node
   */
  public HuffmanTree(int c, int f) { this(c, f, null, null); }

  /**
   * Make a new empty Huffman tree
   */
  public HuffmanTree() { this('\u0000', 0, null, null); }


  /**
   * Get the occurrence frequency for this tree.
   *
   * @return combined frequency of occurrence for all characters in this tree
   */
  public int frequency() { return freq; }

  /**
   * Huffman tree natural ordering based on frequency of occurrence.
   *
   * @param other tree to compare against
   * @return negative if this tree comes first in natural order, positive if second, 0 otherwise
   */
  public int compareTo(HuffmanTree other) {
	return frequency() - other.frequency();
  }

  /**
   * Determine the encoding for each character in the tree and store in the given map
   *
   * @param h map to store character/codes in
   */
  public void encode(Map<Integer, List<Boolean>> h) { encode(h, new Vector<Boolean>()); }

  /**
   * Determine the encoding for each character in the tree and store in the given map
   *
   * @param h map to store character/codes in
   * @param code the code as a list of booleans on the path to this node so far
   */
  private void encode(Map<Integer, List<Boolean>> h, List<Boolean> code) {
	// If at a leaf node, insert the character/code pair into the map
    if (left == null && right == null)
      h.put(ch, code);
    else
    {
      	if (left != null) {
			// traverse left by appending false to the current code
			List<Boolean> codeLeft = new Vector<Boolean>(code);
			codeLeft.add(false);
			left.encode(h, codeLeft);
		}
      	if (right != null) {
			// traverse left by appending true to the current code
			List<Boolean> codeRight = new Vector<Boolean>(code);
			codeRight.add(true);
			right.encode(h, codeRight);
		}
    }
  }
}
