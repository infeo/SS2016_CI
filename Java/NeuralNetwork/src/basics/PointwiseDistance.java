package basics;

public class PointwiseDistance implements Measure<double[]> {

	private double [] fixpoint;
	
	public PointwiseDistance(double [] fixpoint){
		this.fixpoint=fixpoint;
	}
	public double measure(double [] point){
		double sum=0;
		for(int i=0; i<fixpoint.length;i++){
			sum+=Math.abs(fixpoint[i]-point[i]);
		}
		return sum;
	}
}
