package neuralnetwork_simple;

import java.util.concurrent.ThreadLocalRandom;

import functions.Integrate;
import functions.Linear;
import functions.Sum;
import functions.Transfer;

public class Cell {
	
	private Transfer trans;
	private Integrate inte;
	private double derivatedval;
	private double [] weights;
	
	public Cell(){
		trans = new Linear();
		inte = new Sum();
		weights = new double[2];
		weights[0]= 0;
		weights[1]=1;
	}
	
	public Cell (Transfer t, Integrate i, int dimension, double range ){
		trans =t;
		inte = i;
		weights = new double [dimension+1];
		weights [0]=1; //Bias
		weights = ThreadLocalRandom.current().doubles(dimension+1,-range,range).toArray();
		weights[0]=1;
	}
	
	public double computeOutput (double [] input){
		double tmp = inte.integrate (input,weights);
		derivatedval = trans.derivative(tmp);
		return trans.transit(tmp);
	}
	
//	public void fit(int pos, double val){
//		weights [pos] += val;
//	}
	
	
	public void fitWeights(double [] val){
		for(int i=0; i<weights.length; i++){
			weights[i] += val[i];
		}
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
