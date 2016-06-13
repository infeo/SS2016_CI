package selfOrganizingMap;

import java.util.Iterator;
import java.util.concurrent.ThreadLocalRandom;
import java.util.Collection;

import basics.Integrate;
import basics.Linear;

public class SOM {
	
	private int dimInput;
	private int dimSOM;
	private int [] [] adjacency; 
	private SOMNeuron [] neurons;
	private int numNeurons;
	
	/*
	 * unfinished
	 */
	public SOM(int dimIn, int numN, Integrate inte, AdjacencyFunction func){
		dimInput = dimIn;
		dimSOM = func.getDimension();
		numNeurons = numN;
		//init SOMNeurons
		neurons = new SOMNeuron [numN];
		adjacency = new int [numN] [dimSOM];
		for(int i=0; i<numN;i++){
			//double rand [] = ThreadLocalRandom.current().
			//neurons[i] = new SOMNeuron(dimIn,/*random center*/,inte,new Linear());
			adjacency [i] = func.computeNeighbours(i,numN);
		}
	}
	
	/*
	 * Initialise the Self-Organizing-Map with the chosen centers. The order of the Neurons is determined by the iterator of the Collection
	 * @param dimIn the Dimension of the Inputspace
	 * @param numN Number of Neurons ins the SOM
	 * @param inte The Distance function/norm ||.|| (how similar are the input vector and the center of the current neuron) 
	 * @param func The Adjacencey Function-Object, which defines the SOM-Dimension, the count of Neighbours of each neuron and compute those neighbours
	 * @param centers The predefined centers of the neurons. Note, that the iterators of the collections defines the order of Neurons in the Inputspace
	 */
	public SOM(int dimIn, int numN, Integrate inte, AdjacencyFunction func, Collection<double []> centers ){
		dimInput = dimIn;
		dimSOM = func.getDimension();
		numNeurons = numN;
		//init SOMNeurons
		neurons = new SOMNeuron [numN];
		adjacency = new int [numN] [dimSOM];
		Iterator<double[]> it = centers.iterator();
		double [] elem;
		for(int i=0; i<numN;i++){
			elem = it.next();
			neurons[i] = new SOMNeuron(dimIn,elem,inte,new Linear());
			adjacency [i] = func.computeNeighbours(i,numN);
		}
	}
	
	public int computeWinner(double [] inp){
		double minDist=Double.MAX_VALUE;
		double currDist;
		int best=-1;
		for(int i=0; i<neurons.length;i++){
			currDist = neurons[i].computeOutput(inp);
			if(minDist > currDist){
				minDist = currDist;
				best = i;
			};
		}
		return best;
	}
	public double[] getCenter(int i){
		return neurons[i].getWeights();
	}
	
	public int[] getNeighbours(int i){
		return adjacency[i];
	}
}
