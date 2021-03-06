package neuralnetwork_simple;


import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import basics.Tuple;
import deprecated.Cell;
import functions.Integrate;
import functions.Transfer;


public class RecurrentNN {

		private Cell[][] network;
		private int[] dimensions;
		private double[][] delta;
		private double[][] cellout;
		//indicates wich layer is (complete) recurrent
		private boolean [] recurrent;
		private double learningrate;
		private int maxdepth;

		public RecurrentNN(int[] dim, int depth, double rate, boolean [] rec) {
			int layers = dim.length;
			network = new Cell[layers][depth];
			cellout = new double[layers][depth];
			delta = new double[layers][depth];
			dimensions = Arrays.copyOf(dim, layers);
			recurrent = Arrays.copyOf(rec,layers);
			learningrate = rate;
			maxdepth = depth;
		}

		/*
		 * IN: dimension of the input from the ancestor-layer, the integrate and transfer function 
		 * Recurrent-safe: YES		 
		 */
		public void setLayer(int dim, int layer, Integrate i, Transfer t) {
			for (int k = 0; k < dimensions[layer]; k++) {
				if(recurrent[layer]){
					network[layer][k] = new Cell(t,i,dim+dimensions[layer]);
				}
				else{
					network[layer][k] = new Cell(t, i, dim);
				}
					
			}
		}

		/*
		 * TODO: implement BPPT
		 * Recurrent-safe: NO
		 */
		public void learn(Collection<Tuple<double[], double[]>> testdata) {
			Iterator<Tuple<double[], double[]>> it = testdata.iterator();
			Tuple<double[], double[]> elem;
			while (it.hasNext()) {
				elem = it.next();
				propagate(elem.getFst());
				backPropagate(elem);
			}

		}

		public void propagate(double[] realinput) {
			double[] cellin = Arrays.copyOf(realinput, 2*maxdepth);
			// double [] cellout = new double [network [0].length];
			for (int i = 0; i < network.length; i++) {
				int size = dimensions[i];
				for (int j = 0; j < size; j++) {
					if(recurrent[i]){
						//add the last output to the input
						for(int k=0; k< size;k++) cellin[size+k] = cellout[i][j];
					}	
						cellout[i][j] = network[i][j].computeOutput(cellin);	
				}
				cellin = Arrays.copyOf(cellout[i], cellout[i].length);
			}
		}

		/*
		 * TODO: initialise delta as a Zero-Matrix; TODO:computing delta for hidden
		 * units is shitty implemented TODO: decide, wether the things should be
		 * saved in one class or split it in others
		 */
		private void backPropagate(Tuple<double[], double[]> t) {
			double[] aim = Arrays.copyOf(t.getSnd(), t.getSnd().length);
			double[] start = Arrays.copyOf(t.getFst(), t.getFst().length);
			double grad;
			double tmp;
			int last = dimensions.length - 1;
			Cell curr;
			// change the weights of the output layer
			for (int i = 0; i < dimensions[last]; i++) {
				curr = network[last][i];
				delta[last][i] = curr.getDerivatedVal() * (aim[i] - cellout[last][i]);
				// fit the bias
				grad = learningrate * delta[last][i];
				curr.fit(0, grad);
				// fit the other weightcomponents
				for (int j = 0; j < curr.getInLength(); j++) {
					grad = learningrate * delta[last][i] * cellout[last - 1][j];
					curr.fit(j + 1, grad);
				}
			}

			// change the weights of the hidden layers
			// select layer
			for (int i = dimensions.length - 2; i > 0; i--) {
				// select cell
				for (int j = 0; j < dimensions[i]; j++) {
					curr = network[i][j];
					tmp = 0;
					// compute delta
					for (int k = 0; k < dimensions[i + 1]; k++)
						tmp += delta[i + 1][k] * ((network[i + 1][k].getWeights())[j]);
					delta[i][j] = curr.getDerivatedVal() * tmp;
					// fit the bias
					grad = learningrate * delta[i][j];
					curr.fit(0, grad);
					// fit the other weightcomponents
					for (int l = 0; l < curr.getInLength(); l++) {
						grad = learningrate * delta[i][j] * cellout[i - 1][l];
						curr.fit(l + 1, grad);
					}
				}
			}

			// change the weights of the input layer
			for (int j = 0; j < dimensions[0]; j++) {
				curr = network[0][j];
				tmp = 0;
				// compute delta
				for (int k = 0; k < dimensions[1]; k++)
					tmp += delta[1][k] * ((network[1][k].getWeights())[j]);
				delta[0][j] = curr.getDerivatedVal() * tmp;
				// fit the bias
				grad = learningrate * delta[0][j];
				curr.fit(0, grad);
				// fit the other weightcomponents
				for (int l = 0; l < curr.getInLength(); l++) {
					grad = learningrate * delta[0][j] * start[l];
					curr.fit(l + 1, grad);
				}
			}
		}

		public void stepBackPropagate(Tuple<double[], double[]> t, int layer) {
			double[] aim = t.getSnd();
			Cell curr;
			double grad;
			double tmp;
			if (layer == dimensions.length - 1) {
				for (int i = 0; i < dimensions[layer]; i++) {
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
				for (int j = 0; j < dimensions[0]; j++) {
					curr = network[0][j];
					tmp = 0;
					// compute delta
					for (int k = 0; k < dimensions[1]; k++)
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
				for (int j = 0; j < dimensions[layer]; j++) {
					curr = network[layer][j];
					tmp = 0;
					// compute delta
					for (int k = 0; k < dimensions[layer + 1]; k++)
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

		public double measureMeanError(Collection<Tuple<double[], double[]>> testdata) {
			double errsum = 0;
			int errcount = 0;
			Iterator<Tuple<double[], double[]>> it = testdata.iterator();
			Tuple<double[], double[]> elem;
			while (it.hasNext()) {
				elem = it.next();
				errcount++;
				errsum += measureError(elem);
			}

			return errsum / (2 * (double) errcount);
		}

		public double measureError(Tuple<double[], double[]> t) {
			propagate(t.getFst());
			int last = dimensions.length - 1;
			double err = 0;
			double[] should = t.getSnd();
			for (int i = 0; i < dimensions[last]; i++) {
				err += Math.pow((should[i] - cellout[last][i]), 2);
			}
			return err;
		}

		public double[] getResult() {
			int tmp = cellout.length - 1;
			double[] res = Arrays.copyOf(cellout[tmp], dimensions[tmp]);
			return res;
		}

}