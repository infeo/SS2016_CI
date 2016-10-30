package io;

import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.Scanner;

import basics.NeuralEntry;

public class DATParser {

	public static Collection<? extends Entry<double[],double[]>> read(String path){
			Locale.setDefault(Locale.ENGLISH);
			Collection<NeuralEntry> data = new ArrayList<>();
			try {
				File fpath = new File(path);
				int inputDimension=0, outputDimension=0;
				
				//read one line to determine the input
				String s = new Scanner(fpath).nextLine();
				boolean whiteSpaceDetected = false, flag =true;
				for(int i=0; i<s.length();i++){
					//determine input dimension
					if(flag){
					if(Character.isWhitespace(s.charAt(i)) && !whiteSpaceDetected){
						whiteSpaceDetected = true;
					}
					else if(Character.isWhitespace(s.charAt(i)) && whiteSpaceDetected){
						flag = false;
					}
					else{
						inputDimension++;
						whiteSpaceDetected =false;
					}
					}
					else{	//determine output dimension
						if(!Character.isWhitespace(s.charAt(i))){
							outputDimension++;
						}
					}
				}
				
				Scanner scannerFile = new Scanner(fpath);
				while(scannerFile.hasNext()){
					double[] input = new double[inputDimension];
					double[] output = new double[outputDimension];
					for (int j = 0; j < inputDimension; j++) {
						input[j] = scannerFile.nextInt();
					}
					for(int j=0;j<outputDimension;j++){
						output[j]= scannerFile.nextInt();
					}
					data.add(new NeuralEntry(input,output));
				}
				if (scannerFile.hasNext()) {
					System.err.println("WARNING: Unused input in file");
				}
				scannerFile.close();

			} catch (FileNotFoundException e) {
				e.printStackTrace();
				System.err.println("File not found");
			}

			return data;
		
	}
	
}
