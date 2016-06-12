package selfOrganizingMap;

import java.util.HashSet;

import basics.Integrate;
import basics.Linear;

public class SOM {
	
	private int dimInput;
	private int dimSOM;
	private HashSet<SOMNeuron> net;
	private int numNeurons;
	
	
	public SOM(int dimIn, int dimS, int numN, Integrate inte){
		dimInput = dimIn;
		dimSOM = dimS;
		numNeurons = numN;
		//init SOMNeurons
		net = new HashSet();
		for(int i=0; i<numN;i++){
			
			net.add(new SOMNeuron(dimIn,/*random*/,inte,new Linear()));
		}
	}
}
