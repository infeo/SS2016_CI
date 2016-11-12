package deprecated;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.function.BiFunction;

/**
 * 
 * @author Alf
 *
 */
public class NNFunky {

	/**
	 * 
	 */
	private BiFunction<double[],double[],double[]>[][] network;
	
	/**
	 * 
	 */
	private double [][][] weights;
	
	/**
	 * 
	 */
	private double[][] derivatedVal;
	
	/**
	 * 
	 */
	private int[] dimension;
	
	/**
	 * 
	 */
	private double[][] delta;
	
	/**
	 * 
	 */
	private double[][] cellout;
	
	/**
	 * 
	 */
	private double learningrate;
	
	/**
	 * 
	 */
	private int maxdepth;

	/**
	 * 
	 * @param dim
	 * @param depth
	 * @param rate
	 */
	public NNFunky(int[] dim, int depth, double rate) {
		int layers = dim.length;
		network = new BiFunction[layers][depth];
		cellout = new double[layers][depth];
		delta = new double[layers][depth];
		dimension = Arrays.copyOf(dim, layers);
		learningrate = rate;
		maxdepth = depth;
	}

	/**
	 * 
	 * @param dim
	 * @param layer
	 * @param fac
	 */
	public void setLayer(int dim, int layer, FunctionFactory fac) {
		for (int k = 0; k < dimension[layer]; k++) {
			network[layer][k] = fac.get();
		}
	}

	/**
	 * 
	 * @param testdata
	 */
	public void learn(Collection<? extends Entry<double[], double[]>> testdata) {
		Iterator<? extends Entry<double[], double[]>> it = testdata.iterator();
		Entry<double[], double[]> elem;
		while (it.hasNext()) {
			elem = it.next();
			propagate(elem.getKey());
			backPropagate(elem);
		}

	}

	/**
	 * 
	 * @param realinput
	 */
	public void propagate(double[] realinput) {
		double[] cellin = Arrays.copyOf(realinput, maxdepth>realinput.length?maxdepth:realinput.length);
		double[] tmp;
		for (int i = 0; i < network.length; i++) {
			for (int j = 0; j < dimension[i]; j++) {
				tmp = network[i][j].apply(cellin, weights[i][j]);
				cellout[i][j]= tmp[0];
				derivatedVal[i][j]=tmp[1];
			}
			cellin = Arrays.copyOf(cellout[i], cellout[i].length);
		}
	}

	/**
	 * 
	 * @param t
	 */
	private void backPropagate(Entry<double[], double[]> t) {
		double[] aim = Arrays.copyOf(t.getValue(), t.getValue().length);
		double[] start = Arrays.copyOf(t.getKey(), t.getKey().length);
		double grad;
		double tmp;
		int last = dimension.length - 1;
		// change the weights of the output layer
		for (int i = 0; i < dimension[last]; i++) {
			delta[last][i] = derivatedVal[last][i] * (aim[i] - cellout[last][i]);
			// fit the bias
			grad = learningrate * delta[last][i];
			curr.fit(0, grad);
			// fit the other weightcomponents
			for (int j = 0; j < curr.getInLength(); j++) {
				grad = learningrate * delta[last][i] * (last==0?start[j]:cellout[last - 1][j]);
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
		if(dimension.length>1){
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
	}

	public void stepBackPropagate(Entry<double[], double[]> t, int layer) {
		double[] aim = t.getValue();
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
					grad = learningrate * delta[0][j] * t.getKey()[l];
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
		int last = dimension.length - 1;
		double err = 0;
		double[] should = t.getValue();
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

	
	/**
	 * 
	 */
	private void numberCrunching(double[] keep, double[] add){
		for(int i=0;i<keep.length;i++){
			keep[i]+=add[i];
		}
	}
}
