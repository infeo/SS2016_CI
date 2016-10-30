package basics;

import functions.Integrate;

public class EuclidsDistance implements Integrate {

	public double integrate(double[] val, double[] weights) {
		int sum=0;
		for(int i =0;i<weights.length;i++){
			sum+=Math.pow(weights[i] -val[i],2);
		}
		return Math.sqrt(sum);
	}

}
