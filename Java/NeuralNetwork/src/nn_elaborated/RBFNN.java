package nn_elaborated;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Random;

import functions.Integrate;
import functions.Transfer;
/**
 * 
 * @author Alf
 */
public class RBFNN {

	/**
	 * the network
	 */
	private ArrayList<Cell []> network;
	
	/**
	 * a vector indicating for each layer the number of neurons
	 * dim[0] contains the input dimension and dim[dim.length-1] the output dimension
	 */
	private int[] dimensions;
	
	/**
	 * the delta of each neuron
	 */
	private ArrayList<double []> delta;
	
	/**
	 * the output of each neuron
	 */
	private ArrayList<double []> cellout;
	
	/**
	 * the learning rate
	 */
	private double [] learningrates;
	
	/**
	 * the layer, which contains the maximum number of neurons
	 */
	private int maxdepth;
	
	/**
	 * the seed for the PRNG
	 */
	private long seed;
	
	/**
	 * initialises a neural net
	 * @param dim array of numbers, which indicate the count of neuron in the corresponding layer. Th first number is the input layer and tha last is the output layer
	 * @param hiddenlayers a check to note that the dimension vector contains also the input layer.
	 * @param maxdepth	maximum number of neurons in one layer
	 * @param seed the seed for the pseudo random number generator
	 */
	public RBFNN(int[] dim, int hiddenlayers, int maxdepth, long seed) {
		int layers = dim.length;
		if(hiddenlayers != layers-2)
			throw new IllegalArgumentException("Check of understanding input failed");
		network = new ArrayList<Cell []>(dim.length);
		cellout = new ArrayList<double []>(dim.length);
		delta = new ArrayList<double []>(dim.length);
		
		for(int i=0; i<dim.length;i++){
			network.add(i, new Cell [dim[i]]);
			cellout.add(i, new double [dim[i]]);
			delta.add(i, new double [dim[i]]);
		}
		
		dimensions = Arrays.copyOf(dim, layers);
		this.learningrates = new double[dim.length];
		this.maxdepth = maxdepth;
		this.seed =seed;
	}

	
	/**
	 * initializes a layer of the network with the wished neuron
	 * @param layer the desired layer
	 * @param rate learningrate of this layer
	 * @param i	the integration function of each neuron in the layer
	 * @param t	the transfer function of each neuron in the layer
	 * @param weightRange the range to set the weights
	 * @throws ArrayIndexOutOfBoundsException if the layer is bigger than the size of the neural network
	 */
	public void setLayer(int layer, double rate, Integrate i, Transfer t, double weightRange) throws ArrayIndexOutOfBoundsException{
		learningrates[layer]=rate;
		for (int k = 0; k < dimensions[layer]; k++) {
			network.get(layer)[k] = layer==0? new Cell() :new Cell(t, i, dimensions[layer-1],seed, weightRange);
		}
	}
	
	public void setRBFLayer(Integrate i, Transfer [] ts, double[][] centers){
		learningrates[1]=0;
		for (int k = 0; k < dimensions[1]; k++) {
			network.get(1)[k] = new Cell(ts[k], i, centers[k]);
		}
	}

	/**
	 * learn patterns via backpropagation and the specified data set
	 * @param testdata	the data set, which should be learned
	 */
	public void learn(Collection<? extends Entry<double[], double[]>> testdata) {
		Iterator<? extends Entry<double[], double[]>> it = testdata.iterator();
		Entry<double[], double[]> elem;
		while (it.hasNext()) {
			elem = it.next();
			propagate(elem.getKey());
			backPropagate(elem.getValue());
		}

	}

	/**
	 * computes the output of the neuralnet for the given input
	 * @param realinput	array of length
	 */
	public void propagate(double[] realinput) throws IllegalArgumentException{
		if(realinput.length != dimensions[0])
			throw new IllegalArgumentException("Input has the wrong dimension");
		double[] out = new double [maxdepth]; 
		for (int i = 0; i < network.size(); i++) {
			for (int j = 0; j < dimensions[i]; j++) {
				out[j]= network.get(i)[j].computeOutput(i==0?realinput:cellout.get(i-1));
			}
			cellout.set(i,Arrays.copyOf(out, dimensions[i]));
		}
	}

	/**
	 * the backpropagation algorithm
	 * @param aim the teacher to the current input (saved in cellout.get(0)!)
	 */
	private void backPropagate(double[] aim) {
		//the vector to fit the weights of a neuron, +1 comes from the bias
		double [] fit =  new double [maxdepth+1];
		
		//the sum over all deltas of the next layer
		double sumDelta;
		//the current layer, on which backpropagation is applied, and the next (=current+1) one
		Cell [] currL, nextL;
		//the current neuron
		Cell currN;
		//the current layer of deltas & the next  (=current+1) one
		double [] currD, nextD;
		//the current layer of outputs and the previous one 
		double [] currO, prevO;
		
		
		// change the weights of the output layer
		//number of output layer
		int last = dimensions.length - 1;
		currL=network.get(last);
		currD= delta.get(last);
		currO= cellout.get(last);
		prevO = cellout.get(last-1); //note the -1 !!
		for (int i = 0; i < dimensions[last]; i++) {
			currN = currL[i];
			//compute the delta for i-th neuron
			currD[i] =currN.getDerivatedVal() * (aim[i] - currO[i]);
			
			// fit the bias
			fit[0] = learningrates[last] * currD[i];
			// fit the other weightcomponents
			for (int j = 0; j < dimensions[last-1]; j++) {
				fit[j+1] = learningrates[last] * currD[i] * prevO[j];
			}
			currN.fitWeights(fit);
		}
	}

	
	public void learnSinglePattern(Entry<double[], double[]> p){
		this.propagate(p.getKey());
		this.backPropagate(p.getValue());
	}
	
	public double measureMeanError(Collection<? extends Entry<double[], double[]>> testdata) {
		double errsum = 0;
		int errcount = 0;
		Iterator<? extends Entry<double[], double[]>> it = testdata.iterator();
		Entry<double[], double[]> elem;
		while (it.hasNext()) {
			elem = it.next();
			errcount++;
			errsum += measureError(elem);
		}

		return errsum / (double) errcount;
	}

	public double measureError(Entry<double[], double[]> t) {
		propagate(t.getKey());
		int last = dimensions.length - 1;
		double err = 0;
		double[] should = t.getValue();
		double[] is = cellout.get(last);
		for (int i = 0; i < dimensions[last]; i++) {
			err += Math.pow((should[i] - is[i]), 2);
		}
		return 0.5*err;
	}

	public double[] getResult() {
		int last = dimensions.length-1;
		double[] res = Arrays.copyOf(cellout.get(last), dimensions[last]);
		return res;
	}
}