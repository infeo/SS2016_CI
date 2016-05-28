package neuralnetwork;

import java.util.Collection;
import java.util.Iterator;
import java.util.Arrays;

public class NeurolNetwork {
	
	private Cell [] [] network;
	private int [] dimension;
	private double [] [] delta;
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
	 * TODO: initialise delta as a Zero-Matrix; 
	 * TODO:computing delta for hidden units is shitty implemented
	 * TODO: decide, wether the things schould be saved in one class or split it in others
	 */
	private void backPropagate(Tuple <double [], double []> t){
		double [] aim = t.getSnd();
		double grad;
		double tmp;
		int last = dimension.length-1;
		Cell curr;
		//change the weights of the output layer
		for(int i=0; i< dimension [last];i++){
			curr = network[last][i];
			delta [last] [i]= curr.getDerivatedVal()*(aim[i] - cellout[last][i]);
			for(int j=0;j<curr.getInLength();j++){
				grad = -learningrate*delta [last] [i]*cellout[cellout.length-1][i];
				curr.fit(j, grad);
			}
		}
		
		//change the weights of the hidden layers
		//select layer
		for(int i =dimension.length-1; i>0; i--){
			//select cell
			for(int j = 0; j<dimension[i];j++){
				curr = network [i] [j];
				tmp=0;
				//compute delta
				for (int k=0; k<dimension[i+1];k++) tmp +=delta[i+1][k]* ((network [i+1][k].getWeights()) [j]);
				delta [i][j]= curr.getDerivatedVal()*tmp;
				//select weightcomponent
				for (int l =0; l<curr.getInLength(); l++ ){
					grad = -learningrate*delta[i][j]*cellout[i-1][l];
					curr.fit(l, grad);
				}
			}
		}
		
		//change the weights of the input layer
		for(int j = 0; j<dimension[0];j++){
			curr = network [0] [j];
			tmp=0;
			//compute delta
			for (int k=0; k<dimension[1];k++) tmp +=delta[1][k]* ((network [1][k].getWeights()) [j]);
			delta [0][j]= curr.getDerivatedVal()*tmp;
			//select weightcomponent
			for (int l =0; l<curr.getInLength(); l++ ){
				grad = -learningrate*delta[0][j]*t.getFst()[l];
				curr.fit(l, grad);
			}
		}
	}
	
	
	/*
	 * TODO: Check if the division is no integer division!
	 */
	public double measureMeanError(Collection <Tuple <double [],double []>> testdata){
		double errsum=0;
		int errcount=0;
		Iterator <Tuple <double [], double []>> it = testdata.iterator();
		Tuple <double [], double []> elem;
		while (it.hasNext()){
			elem = it.next();
			errcount++;
			errsum+=measureError(elem);
		}
		
		return errsum / (2* (double) errcount);
	}
	
	
	private double measureError(Tuple <double [],double []> t){
		propagate(t.getFst());
		int last = dimension.length-1;
		double err = 0;
		double [] should = t.getSnd();
		for(int i=0; i<dimension[last];i++){
			err += Math.pow((cellout[last][i] - should[i]),2);
		}
		return err;
	}
	
	
	public void stepByStep( Collection <Tuple <double [],double []>> testdata){
		Iterator <Tuple <double [], double []>> it = testdata.iterator();
		Tuple <double [], double []> elem;
		double err;
		while (it.hasNext()){
			elem = it.next();
			err= measureError(elem);
			
			//notify or update graph with error
		}
	}
	
	
	public void showQuality(){
		
	}
	
}
	
	

