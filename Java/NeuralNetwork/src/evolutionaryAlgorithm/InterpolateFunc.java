package evolutionaryAlgorithm;

import basics.Measure;

public class InterpolateFunc implements Individual {

	private int [] points;
	private Measure<int []> measure;
	
	@Override
	public Individual replicate(Mutator t) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double measure() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public double getValue(double x){
		int bottom = (int) Math.floor(x);
		return points[bottom]+ (points[bottom+1]-points[bottom])*(x-bottom);
	}

}
