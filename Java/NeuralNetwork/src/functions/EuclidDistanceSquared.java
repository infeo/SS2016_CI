package functions;

public class EuclidDistanceSquared implements Integrate {

	@Override
	public double integrate(double[] val, double[] weights) {
		double sum = 0;
		for(int i =0; i<val.length;i++){
			sum+=Math.pow(val[i]-weights[i], 2);
		}
		return sum;
	}

}
