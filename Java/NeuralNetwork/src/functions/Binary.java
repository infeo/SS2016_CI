package functions;

public class Binary implements Transfer {

	@Override
	public double transit(double val) {
		return val>=0?1:0;
	}

	@Override
	public double derivative(double val) {
		return 0;
	}

}
