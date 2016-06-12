package neuralnetwork;

import java.util.Collection;
import java.util.Iterator;

import basics.Integrate;
import basics.Transfer;

import java.util.Arrays;

public class NeuralNetwork {

	private Cell[][] network;
	private int[] dimension;
	private double[][] delta;
	// private double [] [] cellderiv;
	private double[][] cellout;
	private double learningrate;
	private int maxdepth;

	public NeuralNetwork(int[] dim, int depth, double rate) {
		int layers = dim.length;
		network = new Cell[layers][depth];
		cellout = new double[layers][depth];
		delta = new double[layers][depth];
		dimension = Arrays.copyOf(dim, layers);
		learningrate = rate;
		maxdepth = depth;
	}

	public void setLayer(int dim, int layer, Integrate i, Transfer t) {
		for (int k = 0; k < dimension[layer]; k++) {
			network[layer][k] = new Cell(t, i, dim);
		}
	}

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
		double[] cellin = Arrays.copyOf(realinput, maxdepth);
		// double [] cellout = new double [network [0].length];
		for (int i = 0; i < network.length; i++) {
			for (int j = 0; j < dimension[i]; j++) {
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
		int last = dimension.length - 1;
		Cell curr;
		// change the weights of the output layer
		for (int i = 0; i < dimension[last]; i++) {
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
		for (int i = dimension.length - 2; i > 0; i--) {
			// select cell
			for (int j = 0; j < dimension[i]; j++) {
				curr = network[i][j];
				tmp = 0;
				// compute delta
				for (int k = 0; k < dimension[i + 1]; k++)
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
		for (int j = 0; j < dimension[0]; j++) {
			curr = network[0][j];
			tmp = 0;
			// compute delta
			for (int k = 0; k < dimension[1]; k++)
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
		int last = dimension.length - 1;
		double err = 0;
		double[] should = t.getSnd();
		for (int i = 0; i < dimension[last]; i++) {
			err += Math.pow((should[i] - cellout[last][i]), 2);
		}
		return err;
	}

	public double[] getResult() {
		int tmp = cellout.length - 1;
		double[] res = Arrays.copyOf(cellout[tmp], dimension[tmp]);
		return res;
	}

}
