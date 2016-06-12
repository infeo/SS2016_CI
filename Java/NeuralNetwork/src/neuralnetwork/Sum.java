package neuralnetwork;

import basics.Integrate;

public class Sum implements Integrate {

	public double integrate(double[] val, double[] weights) {
		double sum = weights[0];
		for (int i = 0; i < weights.length - 1; i++) {
			sum += val[i] * weights[i + 1];
		}
		return sum;
	}

}
