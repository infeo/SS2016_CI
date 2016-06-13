package selfOrganizingMap;

public class OneDimNonCyclic implements AdjacencyFunction {

	/*
	 * Computes the Neighbours. Convention: if the method returns (-1), there is no neighbour 
	 * (non-Javadoc)
	 * @see selfOrganizingMap.AdjacencyFunction#computeNeighbours(int, int)
	 */
	public int[] computeNeighbours(int i,int max) {
		int [] arr = new int [2];
		if(max > i ){
			arr [0]= i--;
			arr[1]= i++;
		}
		else {
			arr [0] = i--;
			arr [1] = -1;
		}
		return arr;
	}
	
	public int getDimension(){
		return 1;
	}

}
