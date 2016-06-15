package selfOrganizingMap;

public interface TopologyFunction {

	public double dist(int winner, int curr);

	public int getDimension();
}
