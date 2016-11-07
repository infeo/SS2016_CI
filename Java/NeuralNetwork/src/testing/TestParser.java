package testing;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map.Entry;

import io.DATParser;

public class TestParser {
	
	public static String path = "E:\\Uni\\Informatik\\Master\\TNN\\E\\ex3\\training1.dat";
	public static String tuxPath = "/home/alf/Dokumente/Uni/Informatik/Master/TNN/E/ex3/training1.dat";
	
	public static void main(String [] args){
//		Collection<? extends Entry<double[],double[]>> data = DATParser.readSimple(path);
		Collection<? extends Entry<double[],double[]>> data = DATParser.read(tuxPath);
		
		for(Entry<double[],double[]> e : data){
			System.out.println("Fst="+Arrays.toString(e.getKey())+" Snd="+Arrays.toString(e.getValue()));
		}
	}
}
