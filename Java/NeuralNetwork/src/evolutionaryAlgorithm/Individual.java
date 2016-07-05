package evolutionaryAlgorithm;

public interface Individual {
	
	public Individual replicate();
	
	public double measure();
}
