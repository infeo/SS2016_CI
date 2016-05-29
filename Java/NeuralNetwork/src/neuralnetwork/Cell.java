package neuralnetwork;

public class Cell {
	
	private int layer;
	private Transfer trans;
	private Integrate inte;
	private double derivatedval;
	private double [] weights;
	private int inlength;
	
	
	/*
	 * TODO: program the weigths generator
	 */
	public Cell (Transfer t, Integrate i, int dimension ){
		trans =t;
		inte = i;
		inlength = dimension+1;
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
	
	public void fit(int pos, double val){
		weights [pos] += val;
	}
	
	public double getDerivatedVal(){
		return derivatedval;
	}
	
	public int getInLength(){
		return inlength;
	}
	
	public double [] getWeights(){
		return weights;
	}
}
