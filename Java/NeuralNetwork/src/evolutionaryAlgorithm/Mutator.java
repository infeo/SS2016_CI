package evolutionaryAlgorithm;

public interface Mutator<T> {

	public void mutate(T individual);
	
	public void cross(T i1, T i2);
}
