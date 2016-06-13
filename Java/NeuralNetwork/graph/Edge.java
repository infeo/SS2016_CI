package graph;

public class Edge implements Comparable<Edge>{

	public int start;
	public int end;
	public double weight;
	
	public Edge(int start, int end, double weight) {
		this.start = start;
		this.end = end;
		this.weight = weight;
	}
	
	
	//HashSet Version von compareTo
//	@Override
//	/**
//	 * Vergleicht Kosten der aufrufenden Kante mit Kosten von Kante e
//	 * @param e = zu vergleichende Kante
//	 * @return -1, falls aufrufende Kante kleiner
//	 *          0, falls gleich
//	 *          1, falls aufrufende Kante gr��er
//	 */
//	public int compareTo(Edge e) {
//		if(weight < e.weight)
//			return -1;
//		if(weight > e.weight)
//			return 1;
//		else 
//		    return 0;
//	}
	
	//TreeSet Version von compareTo
	//ACHTUNG: Nur diese Variante von compareTo nutzen, wenn TreeSets
	//f�r Adj.Listen benutzt werden!
	//TreeSet nutzt f�r Duplikaterkennung den Fall compareTo == 0
	@Override
	/**
	 * Vergleich aufgrund drei Kriteria:
	 * 1. Kantenkosten
	 * 2. Startknoten
	 * 3. Endknoten
	 * @param e = zu vergleichende Kante
	 * @return -1, falls aufrufende Kante kleiner, oder Kosten gleich und Startknoten kleiner, oder Kosten gleich und Startknoten gleich und Endknoten kleiner
	 *          0, falls Start-,Endknoten und Kosten gleich
	 *          1, falls aufrufende Kante gr��er, oder Kosten gleich und Startknoten gr��er, oder Kosten gleich und Startknoten gleich und Endknoten gr��er
	 */
	public int compareTo(Edge e) {
	    if(weight < e.weight)
	        return -1;
	    if(weight > e.weight)
	        return 1;

        if(start < e.start)
            return -1;
        if(start > e.start)
            return 1;
	    
	    if(end < e.end)
	        return -1;
	    if(end > e.end)
	        return 1;
        
	    
	    return 0;
	}
	
	public boolean equals(Object o){
	    if(o instanceof Edge)
	        return (start == ((Edge)o).start && end == ((Edge)o).end && weight == ((Edge)o).weight);
	    
	    return false;
	}
	
	/**
	 * Finde passende Hash Funktion f�r Kanten, da die von Java eingebaute scheinbar 
	 * auf Objektgleichheit (==) anstelle der obigen equals-Methode beruht
	 * @return HashCode f�r HashSets, um Objektgleihheit zu testen
	 */
//	public int hashCode(){
//	    return (Math.pow(2, start))*(Math.pow(3, end))*(Math.pow(5,weight));
//	}
	
}