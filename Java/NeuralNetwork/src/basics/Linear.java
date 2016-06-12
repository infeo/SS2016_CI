package basics;

public class Linear implements Transfer {

	@Override
	public double transit(double val) {
		return val;
	}

	@Override
	public double derivative(double val) {
		return 1;
	}

}
