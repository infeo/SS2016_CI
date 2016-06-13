package selfOrganizingMap;

import java.util.Arrays;

import basics.AbstractNeuron;
import basics.Integrate;
import basics.Transfer;

public class SOMNeuron extends AbstractNeuron{
	
	public SOMNeuron(int dim, double [] cent, Integrate inte, Transfer trans){
		this.setDimInput(dim);
		weights = (Arrays.copyOf(cent, dim));
		this.setInte(inte);
		this.setTrans(trans);
	}
	
	public double computeOutput(double [] input){
		double tmp = inte.integrate (input,weights);
		return trans.transit(tmp);
	}
	
}
