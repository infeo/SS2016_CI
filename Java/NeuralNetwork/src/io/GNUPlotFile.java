package io;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

public class GNUPlotFile {
	
	public static void write(ArrayList<Double> data){
		Locale.setDefault(Locale.ENGLISH);
		try{
			File fpath = new File("learning.curve");
			FileWriter fwriter = new FileWriter(fpath);
			int i=0;
			for(double elem : data){
				fwriter.write(i+"  "+elem+"  ");
				fwriter.write("\n");
				i++;
			}
			fwriter.close();
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
}
