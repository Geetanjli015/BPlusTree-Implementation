import java.util.ArrayList;
import java.util.Collections;
import java.util.ListIterator;

/**
 * @author geetanjli
 *
 * @param <K>
 * @param <T>
 */
public class LeafDataNode<K extends Comparable<K>, T> extends Node<K, T> {
	// values are taken as list of lists
	protected ArrayList<ArrayList<T>> valueList;
	protected LeafDataNode<K, T> nextLeaf;
	protected LeafDataNode<K, T> previousLeaf;

	/**
	 * constructor for creating leaf node
	 * 
	 * @param firstKey
	 * @param firstValue
	 */
	public LeafDataNode(K firstKey, T firstValue) {
		isLeafNode = true;
		keys = new ArrayList<K>();
		valueList = new ArrayList<ArrayList<T>>();
		insertInToLeafNode(firstKey, firstValue);
	}

	/**
	 * insert into leaf node such that value is sorted
	 * 
	 * @param key
	 * @param value
	 */
	private void insertInToLeafNode(K key, T value) {
		int index = Collections.binarySearch(keys, key);
		if (index < 0) {
			int position = -index - 1;
			keys.add(position, key);
			ArrayList<T> values = new ArrayList<>();
			values.add(value);
			valueList.add(position, values);
		} else {
			valueList.get(index).add(value);
		}
	}

	/**
	 * constructor to add list of keys and list of values to the leaf data node
	 * 
	 * @param newKeys
	 * @param newValues
	 */
	public LeafDataNode(ArrayList<K> newKeys, ArrayList<ArrayList<T>> newValues) {
		isLeafNode = true;
		keys = new ArrayList<K>(newKeys);
		valueList = new ArrayList<ArrayList<T>>();
		for (ArrayList<T> a : newValues)
			valueList.add(a);
	}

	/**
	 * insert key/value pair into this node so that it still remains sorted
	 * 
	 * @param key
	 * @param value
	 */
	public void insertSorted(K key, ArrayList<T> value) {
		if (key.compareTo(keys.get(keys.size() - 1)) > 0) {
			keys.add(key);
			valueList.add(value);
		} else if (key.compareTo(keys.get(0)) < 0) {
			keys.add(0, key);
			valueList.add(0, value);
		} else {
			ListIterator<K> iterator = keys.listIterator();
			while (iterator.hasNext()) {
				K next = iterator.next();
				int position = iterator.previousIndex();
				if (next.compareTo(key) == 0) {
					// [Duplicate case] //if key is already present then we will
					// merge the valueLists together
					// to get the current index
					valueList.set(position, (mergeLists(valueList.get(position), value)));
					break;
				} else if (next.compareTo(key) > 0) {
					keys.add(position, key);
					valueList.add(position, value);
					break;
				}
			}

		}
	}

	// This method combines two list by removing duplicates.
	// Hence, handling duplicate values for a key
	/**
	 * Merge List
	 * 
	 * @param first
	 * @param second
	 * @return
	 */
	public ArrayList<T> mergeLists(ArrayList<T> first, ArrayList<T> second) {
		for (T num : second) { // iterate through the second list
			if (!first.contains(num)) { // if first list doesn't contain current
										// element
				first.add(num); // add it to the first list
			}
		}
		return first;
	}

}
