package functions;

public class Gaussian implements Transfer {

	private double radius;
	
	public Gaussian(double radius){
		this.radius = radius;
	}
	
	@Override
	public double transit(double val) {
		// TODO Auto-generated method stub
		return Math.exp(-0.5*Math.pow(val/radius, 2));
	}

	@Override
	public double derivative(double val) {
		// TODO Auto-generated method stub
		return 0;
	}

	public void setRadius(double radius){
		this.radius=radius;
	}
	
}
