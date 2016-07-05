package basics;

import java.util.Map;
import java.util.Map.Entry;

public class MyEntry<K, V> implements Entry<K, V> {

	private K key;
	private V value;

	public MyEntry(K key, V value) {
		this.key = key;
		this.value = value;
	}

	public MyEntry(Map.Entry<? extends K, ? extends V> entry) {
		this.key = entry.getKey();
		this.value = entry.getValue();
	}

	public K setKey(K key) {
		K tmp = this.key;
		this.key = key;
		return tmp;
	}

	public V setValue(V value) {
		V tmp = this.value;
		this.value = value;
		return tmp;
	}

	public V getValue() {
		return this.value;
	}

	public K getKey() {
		return this.key;
	}

	public boolean equals(Object o) {
		if (o instanceof Map.Entry) {
			return (((Map.Entry<K,V>) o).getKey() == null ? key == null : ((Map.Entry<K,V>) o).getKey().equals(key))
					&& (((Map.Entry<K,V>) o).getValue() == null ? value == null : ((Map.Entry<K,V>) o).getValue().equals(value));
			// return ((Map.Entry) o).getKey()==key && ((Map.Entry)
			// o).getValue()==value;
		}
		return false;
	}

	public String toString() {
		return key.toString() + "=" + value.toString();
	}

}
