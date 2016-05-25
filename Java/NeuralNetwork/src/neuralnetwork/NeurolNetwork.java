package neuralnetwork;

import java.util.Collection;
import java.util.Iterator;
import java.util.Arrays;

public class NeurolNetwork {
	
	private Cell [] [] network;
	private int [] dimension;
	//private double [] [] cellderiv;
	private double [] [] cellout;
	private double learningrate;
	private double output;
	
	public void NeuralNetwork( int [] size, Transfer t,Integrate in){
		int tmp=0;
		for(int i=0; i<size.length; i++){
			if( tmp < size[i]) tmp = size[i];
		}
		network = new Cell [size.length] [tmp];
		for (int i =0; i< size.length; i++){
			for (int j =0; j<size[i];j++){
				network [i] [j] = new Cell (t,in,{10,1});
			}
		}
	}
	
	public void learn (Collection <Tuple <double [], double []>> testdata){
		Iterator <Tuple <double [], double []>> it = testdata.iterator();
		Tuple <double [], double []> elem;
		while (it.hasNext()){
			elem = it.next();
			propagate( (double []) elem.getFst());
			backPropagate (elem);
		}
		
	}
	
	/*
	 * TODO: is it wise to return an array? what is with the bias unit?
	 */
	private void propagate(double [] realinput){
		double [] cellin = realinput;
		//double [] cellout = new double [network [0].length];
		for (int i = 0; i< network.length; i++){
			for (int j= 0; j < dimension[i]; j++){
				cellout [i][j] = network[i][j].computeOutput(cellin);
				}
			cellin = Arrays.copyOf(cellout[i],cellout[i].length);
		}
	}
	
	/*
	 * TODO: Problem appeared, where should i get the output of the cell before?
	 */
	private void backPropagate(Tuple <double [], double []> t){
		double [] aim = t.getSnd();
		double delta;
		double grad;
		int last = dimension.length-1;
		Cell curr;
		for(int i=0; i< dimension [last];i++){
			curr = network[last][i];
			delta = curr.getDerivatedVal()*(aim[i] - cellout[last][i]);
			for(int j=0;j<curr.getInLength();j++){
				grad = -learningrate*delta *cellout[cellout.length-1][i];
				curr.fit(j, grad);
			}
		}
		
	}
	
}
	
	

