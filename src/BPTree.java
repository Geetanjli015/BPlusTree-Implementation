import java.io.IOException;
import java.util.ArrayList;

/**
 * @author geetanjli BPlusTree main class
 * @param <K>
 * @param <T>
 */
public class BPTree<K extends Comparable<K>, T> {

	Logger l = new Logger("output_file.txt");
	public Node<K, T> root;
	public static int m;

	public BPTree(int d) {
		super();
		// this.root = root;
		m = d;
	}

	/**
	 * Insert a key/value pair
	 * 
	 * @param key
	 * @param value
	 */
	public Node<K, T> insertKeyValuePair(K key, T value) {
		LeafDataNode<K, T> newLeaf = new LeafDataNode<K, T>(key, value);
		MappingUtilityClass<K, T> entry = new MappingUtilityClass<>(key, newLeaf);

		// If root is null or keys size is zero then assign new node as root
		if (root == null || root.keys.size() == 0) {
			root = entry.getnode();
		}

		// getNewChildEntry will be null if there is no need to split a node
		MappingUtilityClass<K, T> getNewChildEntry = getChildEntry(root, entry, null);

		if (getNewChildEntry == null) {
			return root;
		} else {
			// In case node is split it will make a new index node
			IndexNode<K, T> rootNew = new IndexNode<K, T>(getNewChildEntry.getKey(), root, getNewChildEntry.getnode());
			root = rootNew;
			return root;
		}

	}

	/**
	 * search all the keys between key1 and key2 Range Search
	 * 
	 * @param key1
	 * @param key2
	 */
	public void search(K key1, K key2) {
		String searchedValues = "";
		// Return if root is null (empty tree)
		if (key1 == null || root == null) {
			try {
				// writing null to output file
				l.writeToFile("Null");
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}
		// trees search for leaf node that key1 is pointing to
		LeafDataNode<K, T> leaf = (LeafDataNode<K, T>) searchNode(root, key1);

		// Using doubly linked list property to get the next leaves and
		// iterating through next values
		while (leaf != null && leaf.keys.get(0).compareTo(key2) <= 0) {
			// Iterate through the value of keys to get value searched
			for (int i = 0; leaf != null && i < leaf.keys.size(); i++) {
				if (key1.compareTo(leaf.keys.get(i)) <= 0 && key2.compareTo(leaf.keys.get(i)) >= 0) {
					for (int j = 0; j < leaf.valueList.get(i).size(); j++)
						// append all the searched values in the string
						searchedValues = searchedValues + "(" + leaf.keys.get(i) + "," + leaf.valueList.get(i).get(j)
								+ "), ";
				}
			}
			// go to the next leaf
			leaf = leaf.nextLeaf;
		}

		try {
			if (searchedValues != null && !searchedValues.equals(""))
				// writing finally searched output to the file
				l.writeToFile(searchedValues.trim().substring(0, searchedValues.length() - 2));
			else
				// System.out.println("null");
				l.writeToFile("Null");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * search single value
	 * 
	 * @param key
	 */
	public void search(K key) {
		String searchedValues = "";
		// Return if empty tree or key
		if (key == null || root == null) {
			try {
				// writing output to a file
				l.writeToFile("Null");
			} catch (IOException e) {
				System.out.println("Exception occured in search ");
				e.printStackTrace();
			}
			return;
		}
		// Look for leaf node that key is pointing to
		LeafDataNode<K, T> leaf = (LeafDataNode<K, T>) searchNode(root, key);

		// Look for keys in the leaf
		for (int i = 0; i < leaf.keys.size(); i++) {
			if (key.compareTo(leaf.keys.get(i)) == 0) {
				for (int j = 0; j < leaf.valueList.get(i).size(); j++)
					searchedValues = searchedValues + leaf.valueList.get(i).get(j) + ", ";
			}
		}

		try {
			if (searchedValues != null && !searchedValues.equals(""))
				// writing searched values in the file
				l.writeToFile(searchedValues.trim().substring(0, searchedValues.length() - 2));
			else
				// case when search returned null or no values found in the tree
				l.writeToFile("Null");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * searching exact probable node location
	 * 
	 * @param node
	 * @param key
	 * @return
	 */
	private Node<K, T> searchNode(Node<K, T> node, K key) {
		if (node.isLeafNode) {
			return node;
		}
		// The node is index node
		else {
			IndexNode<K, T> index = (IndexNode<K, T>) node;
			// inserting at the end greater case
			if (key.compareTo(index.keys.get(node.keys.size() - 1)) >= 0) {
				return searchNode((Node<K, T>) index.children.get(index.children.size() - 1), key);
			}
			// insert at the beginning lesser case
			else if (key.compareTo(index.keys.get(0)) < 0) {
				return searchNode((Node<K, T>) index.children.get(0), key);
			} else {
				// search values >=0 and <0 Linear searching
				for (int i = 0; i < index.keys.size() - 1; i++) {
					if (key.compareTo(index.keys.get(i)) >= 0 && key.compareTo(index.keys.get(i + 1)) < 0) {
						return searchNode((Node<K, T>) index.children.get(i + 1), key);
					}
				}
			}
			return null;
		}
	}

	/**
	 * gets the new child entry and the null value when there is no spliting
	 * 
	 * @param node
	 * @param entry
	 * @param latestEntry
	 * @return
	 */
	private MappingUtilityClass<K, T> getChildEntry(Node<K, T> node, MappingUtilityClass<K, T> entry,
			MappingUtilityClass<K, T> latestEntry) {
		if (!node.isLeafNode) {
			// Choose subtree, find i such that Ki <= entry's key value < J(i+1)
			IndexNode<K, T> index = (IndexNode<K, T>) node;
			int i = 0;
			while (i < index.keys.size()) {
				if (entry.getKey().compareTo(index.keys.get(i)) < 0) {
					break;
				}
				i++;
			}
			// It will recursively move to the next index node in the path to
			// find the appropriate index node to reach leaf node
			latestEntry = getChildEntry((Node<K, T>) index.children.get(i), entry, latestEntry);

			// null when no splitting
			if (latestEntry == null) {
				return null;
			}

			// Split child case, must insert newChildEntry in node
			else {
				int j = 0;
				while (j < index.keys.size()) {
					if (latestEntry.getKey().compareTo(index.keys.get(j)) < 0) {
						break;
					}
					j++;
				}

				// insert all the keys in the sorted way and it there is
				// duplicate then add to exiting value list
				index.insertSorted(latestEntry, j);

				// Check the overflow condition of the index node
				if (!index.isOverflowed()) {
					return null;
				} else {
					latestEntry = handleOverflowIndex(index);

					// Root was just split
					if (index == root) {
						// create new root pointer as index node and point to
						// root
						IndexNode<K, T> newRoot = new IndexNode<K, T>(latestEntry.getKey(), root,
								latestEntry.getnode());
						root = newRoot;
						return null;
					}
					return latestEntry;
				}
			}
		} else {
			// case when you have reached to leaf or data node
			LeafDataNode<K, T> newLeaf = (LeafDataNode<K, T>) entry.getnode();
			LeafDataNode<K, T> leaf = (LeafDataNode<K, T>) node;
			// Merge above two to get the sorted keys
			leaf.insertSorted(entry.getKey(), newLeaf.valueList.get(0));

			// in case leaf node is overflown it will have to partition leaf
			// else just insert
			if (!leaf.isOverflowed()) {
				return null;
			} else {
				latestEntry = handleOverflowLeaf(leaf);
				if (leaf == root) {
					IndexNode<K, T> newRoot = new IndexNode<K, T>(latestEntry.getKey(), leaf, latestEntry.getnode());
					root = newRoot;
					return null;
				}
				return latestEntry;
			}
		}
	}

	/**
	 * splitting index node
	 * 
	 * @param index,
	 *            any other relevant data
	 * @return new key/node pair as an Entry
	 */
	public MappingUtilityClass<K, T> handleOverflowIndex(IndexNode<K, T> index) {
		ArrayList<Node<K, T>> updatedValues = new ArrayList<Node<K, T>>();
		ArrayList<K> updatedKeys = new ArrayList<K>();

		// Below operations ensure that righ half entries go to updated keys and
		// values while keeping left half in the exiting ones
		K splitKey = index.keys.get(m / 2);
		index.keys.remove(m / 2);

		updatedValues.add(index.children.get(m / 2 + 1));
		index.children.remove(m / 2 + 1);

		while (index.keys.size() > m / 2) {
			updatedKeys.add(index.keys.get(m / 2));
			index.keys.remove(m / 2);
			updatedValues.add(index.children.get(m / 2 + 1));
			index.children.remove(m / 2 + 1);
		}

		IndexNode<K, T> rightNode = new IndexNode<K, T>(updatedKeys, updatedValues);
		// new Entry added
		MappingUtilityClass<K, T> recentEntry = new MappingUtilityClass<K, T>(splitKey, rightNode);

		return recentEntry;
	}

	/**
	 * Split a leaf node and return the new right node and the splitting
	 * 
	 * @param leaf
	 * @return the key/node pair as an Entry
	 */
	public MappingUtilityClass<K, T> handleOverflowLeaf(LeafDataNode<K, T> leaf) {
		ArrayList<K> newKeys = new ArrayList<K>();
		ArrayList<ArrayList<T>> newValues = new ArrayList<ArrayList<T>>();

		// Making new node by taking right half
		for (int i = m / 2; i <= m - 1; i++) {
			newKeys.add(leaf.keys.get(i));
			newValues.add((ArrayList<T>) leaf.valueList.get(i));
		}

		// Removing right half from exiting leaf as we have move dthat to new
		// right leaf
		for (int i = m / 2; i <= m - 1; i++) {
			leaf.keys.remove(leaf.keys.size() - 1);
			leaf.valueList.remove(leaf.valueList.size() - 1);
		}

		K splitKey = newKeys.get(0);
		// We put the right node in the leaf data node
		LeafDataNode<K, T> rightNode = new LeafDataNode<K, T>(newKeys, newValues);

		// Connecting leaf nodes together
		LeafDataNode<K, T> tmp = leaf.nextLeaf;
		leaf.nextLeaf = rightNode;
		leaf.nextLeaf.previousLeaf = rightNode;
		rightNode.previousLeaf = leaf;
		rightNode.nextLeaf = tmp;

		MappingUtilityClass<K, T> recentEntry = new MappingUtilityClass<>(splitKey, rightNode);
		return recentEntry;
	}

}