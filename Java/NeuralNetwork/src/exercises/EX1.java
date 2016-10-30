package exercises;

import java.util.Collection;
import java.util.Map.Entry;

import deprecated.NeuralNetwork;
import functions.Fermi;
import functions.Integrate;
import functions.Linear;
import functions.Sum;
import functions.Transfer;
import io.DATParser;

public class EX1 {
	
	public static void main(String [] args){
		if(args.length != 3){
			System.out.println("3 parameters expected:\n path the string to the DAT-file\n eta - the learningrate of the network\n iterations -  the count of iterations the training set should be learned");
		}
		
		String path = args[0];
		Double learningrate = Double.valueOf(args[1]);
		Integer iterations = Integer.valueOf(args[2]);
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
	
	
	/**
	 * this method is a wrapper for the neural network output to make it binary
	 * @param out the intern output of the neural network (consisting of doubles values between 0 and 1
	 * @return Array of Integer-values 0 or 1
	 */
	public static int[] makeOutputBinary(double [] out){
		int [] result = new int[out.length];
		for(int i=0;i<out.length;i++){
			result[i]=out[i]>=0.5?1:0;
		}
		return result;
	}
}
