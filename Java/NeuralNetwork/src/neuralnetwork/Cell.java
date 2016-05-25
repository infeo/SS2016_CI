package neuralnetwork;

public class Cell {
	
	private int layer;
	private Transfer trans;
	private Integrate inte;
	private double derivatedval;
	private double [] weights;
	
	public Cell (Transfer t, Integrate i, int dimension ){
		trans =t;
		inte = i;
		weights = new double [dimension+1];
		weights [0]=1; //Bias
		for (int j=0; j< dimension;j++){
		// generate Random weights
		}
		
	}
	
	public double computeOutput (double [] input){
		double tmp = inte.integrate (input,weights);
		derivatedval = trans.derivative(tmp);
		return trans.transit(tmp);
	}
	
	
}
