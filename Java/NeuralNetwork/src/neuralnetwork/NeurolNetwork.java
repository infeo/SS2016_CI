package neuralnetwork;

import java.util.Collection;
import java.util.Iterator;
import java.util.Arrays;

public class NeurolNetwork {
	

	//public Collection <Collection <Cell>> network;
	public Cell [] [] network;
	public int [] dimension;
	//zwischenspeicher
	double output;
	
	public void NeuralNetwork( int [] size, Transfer t,Integrate in){
		int tmp=0;
		for(int i=0; i<size.length; i++){
			if( tmp < size[i]) tmp = size[i];
			//tmp = () ? 1:2;
		}
		network = new Cell [size.length] [tmp];
		for (int i =0; i< size.length; i++){
			for (int j =0; j<size[i];j++){
				network [i] [j] = new Cell (t,in,{10,1});
			}
		}
	}
	
	public void learn (Collection < Tuple> testdata){
		Iterator <Tuple> it = testdata.iterator();
		while (it.hasNext()){
			Tuple elem = it.next();
			double [] output = propagate(elem.getInput());
			double [] result = Arrays.copyOf(output, network [network.length][dimension [network.length]])
			//backpropagate (error);
		}
		
	}
	
	/*
	 * TODO: is it wise to return an array?
	 */
	private double [] propagate(double [] realinput){
		double [] cellin = realinput;
		double [] cellout = new double [network [0].length];
		for (int i = 0; i< network.length; i++){
			for (int j= 0; j < dimension[i]; j++){
				cellout [j] = network[i][j].computeOutput(cellin);
				}
			cellin = Arrays.copyOf(cellout,cellout.length);
		}
		return cellout;
	}
	
	
	
	
	
	
}
