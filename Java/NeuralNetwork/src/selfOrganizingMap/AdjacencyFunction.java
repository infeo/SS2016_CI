package selfOrganizingMap;

public interface AdjacencyFunction {
	
	public int [] computeNeighbours(int neuron, int max);
	public int getDimension();
}
