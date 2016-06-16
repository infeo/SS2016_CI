package selfOrganizingMap;

import java.util.Iterator;
import java.util.concurrent.ThreadLocalRandom;
import java.util.Collection;

import basics.Integrate;
import basics.Linear;

public class SOM {

	private int dimInput;
	private int dimSOM;
	private SOMNeuron[] neurons;
	private int numNeurons;
	private double learningrate;
	private TopologyFunction topo;

	/*
	 * unfinished
	 */
	public SOM(int dimIn, int numN, double learn, Integrate inte, TopologyFunction func) {
		dimInput = dimIn;
		dimSOM = func.getDimension();
		numNeurons = numN;
		learningrate = learn;
		topo = func;
		// init SOMNeurons
		neurons = new SOMNeuron[numN];
		for (int i = 0; i < numN; i++) {
			// double rand [] = ThreadLocalRandom.current().
			// neurons[i] = new SOMNeuron(dimIn,/*random center*/,inte,new
			// Linear());
		}
	}

	/*
	 * Initialise the Self-Organizing-Map with the chosen centers. The order of
	 * the Neurons is determined by the iterator of the Collection
	 * 
	 * @param dimIn the Dimension of the Inputspace
	 * 
	 * @param numN Number of Neurons ins the SOM
	 * 
	 * @param learn LearningRate of the net (currently a constant)
	 * 
	 * @param inte The Distance function/norm ||.|| (how similar are the input
	 * vector and the center of the current neuron)
	 * 
	 * @param func The Topology Function-Object, which defines the SOM-Dimension
	 * and can compute the distance between two neurons
	 * 
	 * @param centers The predefined centers of the neurons. Note, that the
	 * iterators of the collections defines the order of Neurons in the
	 * Inputspace
	 */
	public SOM(int dimIn, int numN, double learn, Integrate inte, TopologyFunction func, Collection<double[]> centers) {
		dimInput = dimIn;
		dimSOM = func.getDimension();
		numNeurons = numN;
		learningrate = learn;
		topo = func;
		// init SOMNeurons
		neurons = new SOMNeuron[numN];
		Iterator<double[]> it = centers.iterator();
		double[] elem;
		for (int i = 0; i < numN; i++) {
			elem = it.next();
			neurons[i] = new SOMNeuron(dimIn, elem, inte, new Linear());
		}
	}

	public int computeWinner(double[] inp) {
		double minDist = Double.POSITIVE_INFINITY;
		double currDist;
		int best = -1;
		for (int i = 0; i < neurons.length; i++) {
			currDist = neurons[i].computeOutput(inp);
			if (minDist > currDist) {
				minDist = currDist;
				best = i;
			}
			;
		}
		return best;
	}

	public double[] getCenter(int i) {
		return neurons[i].getWeights();
	}

	public void learn(Collection<double[]> data, int cycle) {
		Iterator<double[]> it = data.iterator();
		int winner;
		while (it.hasNext()) {
			double[] elem = it.next();
			winner = this.computeWinner(elem);
			for (int i = 0; i < neurons.length; i++) {
				double[] fit = new double[elem.length];
				double[] cent = neurons[i].getWeights();
				for (int j = 0; j < elem.length; j++) {
					fit[j] = learningrate * (elem[j] - cent[j]) * quickNDirty(winner, i, cycle);
				}
				neurons[i].fitWeights(fit);
			}
		}
	}

	/*
	 * simple example for the adjacency function
	 */
	private double quickNDirty(int win, int curr, int time) {
		double dist = topo.dist(win, curr);
		double res=0;
		if (dist ==0) res=1;
		if (dist == 1) res =0.5;
		if (dist == 2) res =0.25;
		if	(dist ==3) res =0.125;
		return res;
		
	}
	
	/*
	 * more elaborated
	 */
	private double quickNDirty2(int win, int curr, int time) {
		double dist = topo.dist(win, curr);
		double res=0;

		double cut=7000; // number of iteration, where the adjacency does not matter anymore
		double decrease =((0.5-Math.log(1))/Math.log(cut+1))*Math.log(time+1)-Math.log(1);
		if (dist == 0)
			return 1;
		res=(1/Math.pow(2, dist))-decrease;
		if(res>=0) return res;
		return 0;
	}
	
}
