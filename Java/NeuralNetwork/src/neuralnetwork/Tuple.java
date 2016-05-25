package neuralnetwork;

public class Tuple {
	
	double [] input;
	double result;
	
	public Tuple (double [] i, double r){
		input = i;
		result = r;
	}

	public double[] getInput() {
		return input;
	}

	public void setInput(double[] input) {
		this.input = input;
	}

	public double getResult() {
		return result;
	}

	public void setResult(double result) {
		this.result = result;
	}
	
	
	
}
