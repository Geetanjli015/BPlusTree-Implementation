
/**
 * @author geetanjli This mapping utility is used for mapping new key value and
 *         respective nodes while we insert ne wnode
 * @param <K>
 * @param <T>
 */
public class MappingUtilityClass<K extends Comparable<K>, T> {

	K key;
	Node<K, T> node;

	public K getKey() {
		return key;
	}

	public void setKey(K key) {
		this.key = key;
	}

	public Node<K, T> getnode() {
		return node;
	}

	public void setnode(Node<K, T> node) {
		this.node = node;
	}

	public MappingUtilityClass(K key, Node<K, T> node) {
		this.key = key;
		this.node = node;
	}

}
