package selfOrganizingMap;

public class OneDimNonCyclic implements TopologyFunction {

	public double dist(int winner, int curr) {
		return Math.abs(curr - winner);
	}

	public int getDimension() {
		return 1;
	}

}
