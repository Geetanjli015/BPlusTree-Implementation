import java.util.ArrayList;

/**
 * @author geetanjli Basic structure of Node
 * @param <K>
 * @param <T>
 */
public class Node<K extends Comparable<K>, T> {
	protected boolean isLeafNode;
	protected ArrayList<K> keys;

	/**
	 * isOverflowed checks whether leaf node is overflown based on given degree
	 * 
	 * @return
	 */
	public boolean isOverflowed() {
		return (keys.size() + 1 > BPTree.m);
	}

}
