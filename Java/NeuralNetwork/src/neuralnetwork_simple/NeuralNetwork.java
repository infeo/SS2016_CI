package neuralnetwork;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map.Entry;

import functions.Integrate;
import functions.Transfer;
/**
 * 
 * @author Alf
 */
public class NeuralNetwork {

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
	private double learningrate;
	
	/**
	 * the layer, which contains the maximum number of neurons
	 */
	private int maxdepth;

	/**
	 * initialises a neural net
	 * @param dim array of numbers, which indicate the count of neuron in the corresponding layer. Th first number is the input layer and tha last is the output layer
	 * @param hiddenlayers a check to note that the dimension vector contains also the input layer.
	 * @param maxdepth	maximum number of neurons in one layer
	 * @param learningrate learningrate
	 */
	public NeuralNetwork(int[] dim, int hiddenlayers, int maxdepth, double learningrate) {
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
		this.learningrate = learningrate;
		this.maxdepth = maxdepth;
	}

	/**
	 * initializes a layer of the network with the wished neuron
	 * @param layer the desired layer
	 * @param i	the integration function of each neuron in the layer
	 * @param t	the transfer function of each neuron in the layer
	 * @throws ArrayIndexOutOfBoundsException if the layer is bigger than the size of the neural network
	 */
	public void setLayer(int layer, Integrate i, Transfer t) throws ArrayIndexOutOfBoundsException{
		for (int k = 0; k < dimensions[layer]; k++) {
			network.get(layer)[k] = layer==0? new Cell() :new Cell(t, i, dimensions[layer-1]);
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
			fit[0] = learningrate * currD[i];
			// fit the other weightcomponents
			for (int j = 0; j < dimensions[last-1]; j++) {
				fit[j+1] = learningrate * currD[i] * prevO[j];
			}
			currN.fitWeights(fit);
		}

		// change the weights of the hidden layers
		// select layer
		for (int i = dimensions.length - 2; i > 0; i--) {
			// select cell
			nextL=currL;
			currL=network.get(i);
			nextD=currD;
			currD= delta.get(i);
			currO= cellout.get(i);
			prevO = cellout.get(i-1); //note the -1 !!
			for (int j = 0; j < dimensions[i]; j++) {
				currN = currL[j];
				sumDelta = 0;
				// compute delta
				for (int k = 0; k < dimensions[i + 1]; k++)
					sumDelta += nextD[k] * (nextL[k].getWeights()[j]);
				currD[j] = currN.getDerivatedVal() * sumDelta;
				// fit the bias
				fit [0] = learningrate * currD[j];
				// fit the other weightcomponents
				for (int l = 0; l < dimensions[i-1]; l++) {
					fit[l+1] = learningrate * currD[j] * prevO[l];
				}
				currN.fitWeights(fit);
			}
		}
	}

	/*
	public void stepBackPropagate(Tuple<double[], double[]> t, int layer) {
		double[] aim = t.getSnd();
		Cell curr;
		double grad;
		double tmp;
		if (layer == dimension.length - 1) {
			for (int i = 0; i < dimension[layer]; i++) {
				// select cell
				curr = network[layer][i];
				// compute delta
				delta[layer][i] = curr.getDerivatedVal() * (aim[i] - cellout[layer][i]);
				// bias
				grad = learningrate * delta[layer][i];
				curr.fit(0, grad);
				// rest
				for (int j = 0; j < curr.getInLength(); j++) {
					grad = learningrate * delta[layer][i] * cellout[layer - 1][j];
					curr.fit(j + 1, grad);
				}
			}
		} else if (layer == 0) {
			for (int j = 0; j < dimension[0]; j++) {
				curr = network[0][j];
				tmp = 0;
				// compute delta
				for (int k = 0; k < dimension[1]; k++)
					tmp += delta[1][k] * ((network[1][k].getWeights())[j]);
				delta[0][j] = curr.getDerivatedVal() * tmp;
				grad = learningrate * delta[0][j];
				curr.fit(0, grad);
				// select weightcomponent
				for (int l = 0; l < curr.getInLength(); l++) {
					grad = learningrate * delta[0][j] * t.getFst()[l];
					curr.fit(l + 1, grad);
				}
			}
		} else {
			for (int j = 0; j < dimension[layer]; j++) {
				curr = network[layer][j];
				tmp = 0;
				// compute delta
				for (int k = 0; k < dimension[layer + 1]; k++)
					tmp += delta[layer + 1][k] * ((network[layer + 1][k].getWeights())[j]);
				delta[layer][j] = curr.getDerivatedVal() * tmp;
				// bias
				grad = learningrate * delta[layer][j];
				curr.fit(0, grad);
				// select weightcomponent
				for (int l = 0; l < curr.getInLength(); l++) {
					grad = learningrate * delta[layer][j] * cellout[layer - 1][l];
					curr.fit(l + 1, grad);
				}
			}
		}

	}
*/
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

		return errsum / (2 * (double) errcount);
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
		return err;
	}

	public double[] getResult() {
		int last = dimensions.length-1;
		double[] res = Arrays.copyOf(cellout.get(last), dimensions[last]);
		return res;
	}

}
