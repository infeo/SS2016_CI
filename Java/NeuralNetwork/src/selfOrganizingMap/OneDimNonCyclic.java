package selfOrganizingMap;

public class OneDimNonCyclic implements AdjacencyFunction {

	@Override
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
