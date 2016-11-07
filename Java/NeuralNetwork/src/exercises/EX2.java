package exercises;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map.Entry;

import functions.Fermi;
import functions.Integrate;
import functions.Sum;
import functions.Transfer;
import io.DATParser;
import nn_elaborated.NeuralNetwork;

public class EX2 {

	public static NeuralNetwork nn;
	public static int iterations;
	
	public static void main(String[] args) {
		if(args.length != 4){
			System.out.println("4 parameters expected:\n "
					+ "train_path string to the training set\n"
					+ " test_path string to the test set\n "
					+ "etas - array of learningrates (for each layer one)\n "
					+ "iterations -  the count of iterations the training set should be learned");
		}
		
		String path = args[0];
		Double learningrate = Double.valueOf(args[2]);
		Integer iterations = Integer.valueOf(args[3]);
		Collection<? extends Entry<double[],double[]>> data = DATParser.read(path);
		
		// generate Network
		int[] dim = {2};
		NeuralNetwork neuron = new NeuralNetwork(dim, 2, learningrate);
		Transfer t2 = new Fermi();
		Integrate inte = new Sum();
		neuron.setLayer(5, 0, inte, t2);
		
		System.out.println("the initial Error of the network is: "+neuron.measureMeanError(data));
		System.out.println("Learning starts now...");
		for(int i=0; i< iterations; i++){
			neuron.learn(data);
		}
		System.out.println("After learning the training set "+iterations+" times the mean error is: "+neuron.measureMeanError(data));
		System.out.println("End");
	}
	
	public static void init(){

	}

	public static double[] learn(Collection<? extends Entry<double[],double[]>> data){
		double [] errOverTime = new double[iterations*data.size()];
		for(int i=0;i<iterations;i++){
			Iterator<? extends Entry<double[],double[]>> it = data.iterator();
			for(int j=0; it.hasNext();j++){
				Entry <double[],double[]> p = it.next();
				errOverTime[j] = nn.measureError(p);
				nn.learnSinglePattern(p);
			}
		}
		return errOverTime;
	}
	
	public static void eval(Collection<? extends Entry<double[],double[]>> data){
		System.out.println("Mean Error of the neural network on the testset: "+nn.measureMeanError(data));
	}
}
