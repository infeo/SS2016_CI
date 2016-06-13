package basics;


public abstract class AbstractNeuron {
	protected int dimInput;
	protected int dimOutput;
	protected double [] weights;
	protected Transfer trans;
	protected Integrate inte;
	
	
	public abstract double computeOutput (double [] input);
	
	public int getDimInput() {
		return dimInput;
	}
	public void setDimInput(int dimInput) {
		this.dimInput = dimInput;
	}
	public double[] getWeights() {
		return weights;
	}
	public void setWeights(double[] weights) {
		this.weights = weights;
	}
	public Transfer getTrans() {
		return trans;
	}
	public void setTrans(Transfer trans) {
		this.trans = trans;
	}
	public Integrate getInte() {
		return inte;
	}
	public void setInte(Integrate inte) {
		this.inte = inte;
	}
	
	public void fitWeights(double [] fit){
		for(int i=0; i<weights.length; i++){
			weights[i]+=fit[i];
		}
	}
}
