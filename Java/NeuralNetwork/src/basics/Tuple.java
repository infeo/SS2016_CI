package basics;

import java.util.Map.Entry;

public class Tuple<X, Y> implements Entry<X,Y> {

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

	@Override
	public X getKey() {
		return getFst();
	}

	@Override
	public Y getValue() {
		return getSnd();
	}

	@Override
	public Y setValue(Y value) {
		Y tmp = getSnd();
		setSnd(value);
		return tmp;
	}

}
