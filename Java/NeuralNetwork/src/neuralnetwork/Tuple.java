package neuralnetwork;

public class Tuple<X, Y> {

	X fst;
	Y snd;

	public Tuple(X f, Y s) {
		fst = f;
		snd = s;
	}

	public X getFst() {
		return fst;
	}

	public void setFst(X fst) {
		this.fst = fst;
	}

	public Y getSnd() {
		return snd;
	}

	public void setSnd(Y snd) {
		this.snd = snd;
	}

}
