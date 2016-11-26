package exercises;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Random;

import functions.Fermi;
import functions.Gaussian;
import functions.Integrate;
import functions.Linear;
import functions.Sum;
import functions.Transfer;
import io.DATParser;
import io.GNUPlotFile;
import nn_elaborated.NeuralNetwork;

public class EX3 {
	public static NeuralNetwork nn;
	public static int iterations;
	public static Random rand = new Random(System.nanoTime());
	
	public static void main(String[] args) {
		if(args.length != 3){
			System.out.println("3 parameters expected:\n "
					+ "train_path string to the training set\n"
					+ " test_path string to the test set\n "
					+ "iterations -  the count of iterations the training set should be learned");
			return;
		}
		
		String trainpath = args[0];
		String testpath = args[1];
		iterations = Integer.valueOf(args[2]);
		ArrayList<? extends Entry<double[],double[]>> traindata = (ArrayList<? extends Entry<double[],double[]>>) DATParser.read(trainpath);
		ArrayList<? extends Entry<double[],double[]>> testdata = (ArrayList<? extends Entry<double[],double[]>>) DATParser.read(testpath);
		
		
		// generate Network
		init1();
		//init2();
		
		System.out.println("the initial Error of the network is: "+nn.measureMeanError(testdata));
		System.out.println("Learning starts now...");
		
		ArrayList<Double> errorDevel = learn(traindata);
		System.out.println("After learning the training set "+iterations+" times the mean error is: "+nn.measureMeanError(testdata));
		GNUPlotFile.write(errorDevel);
		System.out.println("Error curve written to: learning.curve");
		
		System.out.println("End");
	}

	/**
	 * initializes the neural network with an architecture to accept the training1.dat file
	 */
	public static void init1(){
		int[] dim = {4, 3, 2};
		nn = new NeuralNetwork(dim, 1, 4, System.nanoTime());
		Transfer t1 = new Linear();
		Transfer t2 = new Linear();
		Integrate inte1 = new Sum();
		Integrate inte2 = new Gaussian();
		nn.setLayer(0, 0, inte1,t1 , 1);
		nn.setLayer(1, 0.005, inte, t2, 2.0);
		nn.setLayer(2, 0.005, inte1, t2, 2.0);
	}
	
	
	/**
	 * initializes the neural network with an architecture to accept the training2.dat file
	 */
	public static void init2(){
		int[] dim = {2, 3, 1};
		nn = new NeuralNetwork(dim, 1, 3, System.nanoTime());
		Transfer t1 = new Linear();
		Transfer t2 = new Fermi();
		Integrate inte = new Sum();
		nn.setLayer(0, 0, inte,t1 , 1);
		nn.setLayer(1, 0.005, inte, t2, 2.0);
		nn.setLayer(2, 0.005, inte, t2, 2.0);
	}
	
	public static ArrayList<Double> learn(Collection<? extends Entry<double[],double[]>> data){
		ArrayList<Double> errOverTime = new ArrayList<>(iterations*data.size());
		for(int i=0;i<iterations;i++){
			Iterator<? extends Entry<double[],double[]>> it = data.iterator();
			while(it.hasNext()){
				Entry <double[],double[]> p = it.next();
				errOverTime.add(nn.measureError(p));
				nn.learnSinglePattern(p);
			}
//			shuffle(data);
		}
		return errOverTime;
	}
	
	/**
	 * NOT WORKING
	 * Shuffle algorithm by fisher-yates 
	 * @param input the Array to be shuffled
	 */
	public static void shuffle (ArrayList<?> input){
		int index;
		Object tmp;
		
		for(int i = input.size()-1; i>0; i--){
			index = rand.nextInt(i+1);
			tmp = input.get(i);
//			input.set(i,  input.get(index));
//			input.set(index, tmp);
		}
	}
}
