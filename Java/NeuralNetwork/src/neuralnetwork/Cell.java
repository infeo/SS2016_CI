package neuralnetwork;

import java.util.concurrent.ThreadLocalRandom;

public class Cell {
	
	private Transfer trans;
	private Integrate inte;
	private double derivatedval;
	private double [] weights;
	
	
	public Cell (Transfer t, Integrate i, int dimension ){
		trans =t;
		inte = i;
		weights = new double [dimension+1];
		weights [0]=1; //Bias
		for (int j=1; j<= dimension;j++){
			weights [j]=ThreadLocalRandom.current().nextDouble(-0.5, 0.5);
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
		return weights.length-1;
	}
	
	public double [] getWeights(){
		return weights;
	}
}
