package neuralgas;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.function.BiFunction;
import java.util.function.Function;

public class NeuralGas {

	BiFunction<double[],double[],Double> metric;
//	Graph<Neurons, Neighbours> net;
	double[][] network;
	int [] order;
	BiFunction<Integer,Integer,Double> adjacency;
	Function<Integer,Double> learnDecay;
	double learningrate_i;
	
	
	public MultiNeuralGas(BiFunction<double[],double[],Double> metric, int size, int inputdim, BiFunction<Integer,Integer,Double> adjaceny, double learningrate){
		this.metric = metric;
		this.adjacency=adjaceny;
		network = new double[size][inputdim];
		this.learningrate_i = learningrate;
	}
	
	/**
	 * 
	 * @param data
	 * @param iterations
	 */
	public void learn(Collection<double[]> data, int iterations){
		double[][] feedback;
		for(int i=0; i<iterations;i++){
			for(double [] elem: data){
				feedback = applyPattern(elem);
				learnPattern(elem, feedback,i);
			}
//			shuffle(data);
		}
		
	}
	
	
	/**
	 * 
	 * @param x
	 * @param feedback
	 * @return
	 */
	public double[][] applyPattern(double[] x){
		//init feedback vector
		double[][] feedback = new double [network.length][2];
		for(int i=0;i<network.length;i++){
			feedback[i][0]=i;
			feedback[i][1]=-1.0;
		}
		
		//compute the distances
		for(int i =0; i<network.length;i++){
			feedback[i][1]=metric.apply(x,network[i]);
		}
		
		//sort the feedback vector
		Comparator<double[]> c = new Comparator<double[]>() {

			public int compare(double[] o1, double[] o2) {
				if (o1[1] < o2[1])
					return -1;
				else if (o1[1] == o2[1])
					return 0;
				else
					return 1;
			}

		};
		
		Arrays.sort(feedback,c);
		
		return feedback;
	}
	
	/**
	 * 
	 * @param elem
	 * @param sortedFeedback
	 * @param iteration
	 */
	public void learnPattern(double[] elem, double[][] sortedFeedback, int iteration){
		int size = network.length;
		int dim = network[0].length;
		
		double currLearningrate = learnDecay.apply(iteration);
		
		for(int i=0; i<size;i++){
			int currNeuron = (int) sortedFeedback[i][0];
			for(int j=0;j<dim;j++){
				network[currNeuron][j]+= currLearningrate*adjacency.apply(i, iteration)*(elem[j]-network[currNeuron][j]);
			}
		}
	}
	
	
	public void quickAndDirty(Collection<double[]> data, int iterations){
		int size = network.length;
		int dim = network[0].length;
		
		double shrinkage= 1.0;

		double[][] diff = new double[size][dim];
		double[][] dist = new double[size][2];
		for (int i = 0; i < size; i++) {
			dist[i][0] = i;
			dist[i][1] = 0;
		}

		Comparator<double[]> c = new Comparator<double[]>() {

			public int compare(double[] o1, double[] o2) {
				if (o1[1] < o2[1])
					return -1;
				else if (o1[1] == o2[1])
					return 0;
				else
					return 1;
			}

		};
		for (int l = 0; l < iterations; l++) {

			double range = 2*Math.pow(l*shrinkage,2);
			
			//using linear interpolation
			double learningrate_c =learningrate_i-learningrate_i/iterations*l;
			for (double[] elem : data) {

				// computes the difference of the input to the network
				for (int i = 0; i < size; i++) {
					for (int j = 0; j < dim; j++) {
						diff[i][j] = elem[j] - network[i][j];
					}
				}

				// computes the length of each difference (= the distance from x
				// to center_i)
				int winner = -1;
				double minDist = Double.POSITIVE_INFINITY;
				for (int i = 0; i < size; i++) {
					dist[i][0] = i;
					for (int j = 0; j < dim; j++) {
						dist[i][1] += Math.pow(diff[i][j], 2);
					}
					dist[i][1] = Math.sqrt(dist[i][1]);
					if (dist[i][1] < minDist) {
						minDist = dist[i][1];
						winner = i;
					}
				}

				// learn
				Arrays.sort(dist, c);
				double adjacencyVal = 0;
				int curr_center = -1;
				//we iterate over a list!!
				for (int i = 0; i < size; i++) {
					curr_center = (int) dist[i][0];
					adjacencyVal = Math.exp(-Math.pow(i, 2) / range);
					for (int j = 0; j < dim; j++) {
						network[curr_center][j]+=learningrate_c*adjacencyVal*diff[curr_center][j];
					}
				}

			}
			
//			shuffle(data);
		}
	}
}
