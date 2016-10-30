package io;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;
import java.util.Scanner;
import java.util.Map.Entry;

import basics.NeuralEntry;

public class WeightParser {
	public static ArrayList<double[][]> read(String path){
		Locale.setDefault(Locale.ENGLISH);
		ArrayList<double[][]> weights = null;
		try {
			File fpath = new File(path);
			
			
			Scanner scannerFile = new Scanner(fpath);
			if(!scannerFile.hasNext()){
				throw new IllegalArgumentException("Wrong file format");
			}
			
			//read the number of layers
			int layers = scannerFile.nextInt();
			weights = new ArrayList<>(layers);
			
			//read the number of neurons per layer
			int [] neuronsPerLayer = new int[layers];
			for(int i=0;i<layers;i++){
				neuronsPerLayer[i]= scannerFile.nextInt();
			}
			
			//read for each layer the weights of the neurons
			//the input layer
			double [][] tmp = new double[neuronsPerLayer[0]][1];
			for(int j=0; j<neuronsPerLayer[0];j++){
				tmp[j][0] = scannerFile.nextDouble();
				//ignore the semicolon
				scannerFile.next();
			}
			weights.add(tmp);
			
			
			//the rest
			for(int i=1;i<layers;i++){
				tmp = new double[neuronsPerLayer[i]][neuronsPerLayer[i-1]];
				for(int j=0; j<neuronsPerLayer[i];j++){
					for(int k=0;k<neuronsPerLayer[i-1];j++){
						tmp[j][k]=scannerFile.nextDouble();
					}
					//ignore the semicolon
					scannerFile.next();
				}
				weights.add(tmp);
			}
			
			if (scannerFile.hasNext()) {
				System.err.println("WARNING: Unused input in file");
			}
			scannerFile.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.err.println("File not found");
		}

		return weights;
	
}
}
