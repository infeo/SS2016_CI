package neuralnetwork;

import java.util.Collection;
import java.util.Iterator;
import java.util.Arrays;

public class NeuralNetwork {
	
	private Cell [] [] network;
	private int [] dimension;
	private double [] [] delta;
	//private double [] [] cellderiv;
	private double [] [] cellout;
	private double learningrate;
	private int maxdepth;
	
	
	public NeuralNetwork(int [] dim, int depth, double rate){
		int layers = dim.length;
		network = new Cell [layers][depth];
		cellout = new double [layers] [depth];
		delta = new double [layers] [depth];
		dimension = Arrays.copyOf(dim, layers);
		learningrate = rate;
		maxdepth=depth;
	}
	
	public void setLayer(int dim, int layer, Integrate i, Transfer t){
		for(int k =0; k <dimension [layer];k++){
			network [layer][k]= new Cell(t,i,dim);
		}
	}
	
	public void learn (Collection <Tuple <double [], double []>> testdata){
		Iterator <Tuple <double [], double []>> it = testdata.iterator();
		Tuple <double [], double []> elem;
		while (it.hasNext()){
			elem = it.next();
			propagate( elem.getFst());
			backPropagate (elem);
		}
		
	}
	
	/*
	 * TODO: is it wise to return an array? what is with the bias unit?
	 */
	public void propagate(double [] realinput){
		double [] cellin = Arrays.copyOf(realinput, maxdepth);
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
				grad = -learningrate*delta [last] [i]*cellout[last][i];
				curr.fit(j, grad);
			}
		}
		
		//change the weights of the hidden layers
		//select layer
		for(int i =dimension.length-2; i>0; i--){
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
	
	public void stepBackPropagate(Tuple <double [], double []> t, int layer){
		double [] aim = t.getSnd();
		Cell curr;
		double grad;
		double tmp;
		if (layer == dimension.length-1){
			for(int i=0; i< dimension [layer];i++){
				curr = network[layer][i];
				delta [layer] [i]= curr.getDerivatedVal()*(aim[i] - cellout[layer][i]);
				for(int j=0;j<curr.getInLength();j++){
					grad = -learningrate*delta [layer] [i]*cellout[layer][i];
					curr.fit(j, grad);
				}
			}
		}
		else if (layer == 0){
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
		else {
			for(int j = 0; j<dimension[layer];j++){
				curr = network [layer] [j];
				tmp=0;
				//compute delta
				for (int k=0; k<dimension[layer+1];k++) tmp +=delta[layer+1][k]* ((network [layer+1][k].getWeights()) [j]);
				delta [layer][j]= curr.getDerivatedVal()*tmp;
				//select weightcomponent
				for (int l =0; l<curr.getInLength(); l++ ){
					grad = -learningrate*delta[layer][j]*cellout[layer-1][l];
					curr.fit(l, grad);
				}
			}
		}
		
	}
	
	
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
	
	
	public double measureError(Tuple <double [],double []> t){
		propagate(t.getFst());
		int last = dimension.length-1;
		double err = 0;
		double [] should = t.getSnd();
		for(int i=0; i<dimension[last];i++){
			err += Math.pow((cellout[last][i] - should[i]),2);
		}
		return err;
	}
	
	public double [] getResult(){
		int tmp = cellout.length-1;
		double [] res = Arrays.copyOf(cellout[tmp],dimension[tmp]);
		return res;
	}
	
}
	
	

