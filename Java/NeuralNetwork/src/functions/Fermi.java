package functions;

import java.lang.Math;

public class Fermi implements Transfer {

	public double transit(double val) {
		return 1 / (1 + Math.pow(Math.E, -val));
	}

	public double derivative(double val) {
		double tmp = transit(val);
		return tmp * (1 - tmp);
	}

}
