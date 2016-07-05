package basics;

import java.util.Map;
import java.util.Map.Entry;

/**
 * Eine schnellere Implementierung der myEntry-Klasse (weniger Objektaufrufe)
 * @author Armin Schrenk
 *
 */
public class FastEntry implements Entry<Double, Integer> {
	
	private double key;
	private int value;

	public FastEntry(double key, int value) {
		this.key = key;
		this.value = value;
	}

	public FastEntry(Map.Entry<? extends Double, ? extends Integer> entry) {
		
		if(entry.getKey()==null || entry.getValue() ==null)
			throw new IllegalArgumentException("Null-Werte sind nicht erlaubt");
		this.key = entry.getKey();
		this.value = entry.getValue();
	}

	public double setKey(double key) {
		double tmp = this.key;
		this.key = key;
		return tmp;
	}

	public Integer setValue(Integer value) {
		int tmp = this.value;
		this.value = value;
		return tmp;
	}

	public Integer getValue() {
		return this.value;
	}

	public Double getKey() {
		return this.key;
	}

	public boolean equals(Object o) {
		if (o instanceof Map.Entry) {
			return ((Map.Entry<Integer, Double>)o).getKey()==key && ((Map.Entry<Integer, Double>)o).getValue()==value;
		}
		return false;
	}

	public String toString() {
		return key+"="+value;
	}
}
