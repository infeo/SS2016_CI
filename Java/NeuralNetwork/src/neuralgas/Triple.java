package neuralgas;

public class Triple<X,Y,Z> {

	private X first;
	private Y second;
	private Z third;
	
	public Triple(X first, Y second, Z third){
		this.first =first;
		this.second =second;
		this.third =third;
	}
	
	public X getFirst() {
		return first;
	}
	
	public void setFirst(X first) {
		this.first = first;
	}
	
	public Y getSecond() {
		return second;
	}
	
	public void setSecond(Y second) {
		this.second = second;
	}
	
	public Z getThird() {
		return third;
	}
	
	public void setThird(Z third) {
		this.third = third;
	}
	
	
	
}
