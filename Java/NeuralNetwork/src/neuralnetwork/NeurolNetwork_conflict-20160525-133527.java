package neuralnetwork;

import java.util.Collection;
import java.util.Iterator;

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
				network [i] [j] = new Cell (t,in);
			}
		}
	}
	
	public void learn (Collection < Tuple> testdata){
		Iterator <Tuple> it = testdata.iterator();
		while (it.hasNext()){
			Tuple elem = it.next();
			//double error = propagate(elem.getInput());
			//backpropagate (error);
		}
		
	}
	
	private double propagate(double []){
		for (int i = 0; i< network.length; i++){
			for (int j= 0; j < dimension[i].length; j++){
				
			}
		}
	}
	
	
	
	
	
}
