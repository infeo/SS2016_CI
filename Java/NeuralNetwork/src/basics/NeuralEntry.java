package basics;

import java.util.Map;
import java.util.Map.Entry;

public class NeuralEntry implements Entry<double [], double[]> {
	
	private double[] key;
	private double[] value;

	public NeuralEntry(double[] key, double[] value) {
		this.key = key;
		this.value = value;
	}

	public NeuralEntry(Map.Entry<double[], double[]> entry) {
		this.key = entry.getKey();
		this.value = entry.getValue();
	}

	public double[] setKey(double[] key) {
		double[] tmp = this.key;
		this.key = key;
		return tmp;
	}

	public double[] setValue(double[] value) {
		double[] tmp = this.value;
		this.value = value;
		return tmp;
	}

	public double[] getValue() {
		return this.value;
	}

	public double[] getKey() {
		return this.key;
	}

	public boolean equals(Object o) {
		if (o instanceof Map.Entry && ((Map.Entry) o).getKey() instanceof double[] && ((Map.Entry) o).getValue() instanceof double[]) {
			Map.Entry<double[], double[]> e = (Map.Entry<double[],double[]>) o;
			return (e.getKey() == null ? key == null : e.getKey().equals(key))
					&& (e.getValue() == null ? value == null : e.getValue().equals(value));
			// return ((Map.Entry) o).getKey()==key && ((Map.Entry)
			// o).getValue()==value;
		}
		return false;
	}

	public String toString() {
		return key.toString() + "=" + value.toString();
	}
}
