import java.util.ArrayList;
import java.util.List;

/**
 * @author geetanjli Index node structure
 * @param <K>
 * @param <T>
 */
public class IndexNode<K extends Comparable<K>, T> extends Node<K, T> {

	protected ArrayList<Node<K, T>> children;

	public IndexNode(K key, Node<K, T> child0, Node<K, T> child1) {
		isLeafNode = false;
		keys = new ArrayList<K>();
		keys.add(key);
		children = new ArrayList<Node<K, T>>();
		children.add(child0);
		children.add(child1);
	}

	public IndexNode(List<K> newKeys, List<Node<K, T>> newChildren) {
		isLeafNode = false;
		keys = new ArrayList<K>(newKeys);
		children = new ArrayList<Node<K, T>>(newChildren);
	}

	/**
	 * insert such that keys remain sorted
	 * 
	 * @param e
	 * @param position
	 */
	public void insertSorted(MappingUtilityClass<K, T> e, int position) {
		K key = e.getKey();
		Node<K, T> child = e.getnode();
		// add keys and children
		if (position >= keys.size()) {
			keys.add(key);
			children.add(child);
		} else {
			// if not then to next index position
			keys.add(position, key);
			children.add(position + 1, child);
		}
	}

}
