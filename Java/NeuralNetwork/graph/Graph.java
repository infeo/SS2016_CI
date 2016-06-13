package graph;

import java.util.Set;
import java.util.TreeSet;
import java.util.HashMap;
import java.io.IOException;
import java.io.Writer;

public class Graph implements InterfaceGraph {

	
	/**
	 * Adjazenzlisten aller Knoten
	 * HashMap: Key:   Startknoten (verhindert Duplikate)
	 *          Value: Adj.Liste des Startknotens
	 * einzelne Adj.liste:
	 * HashMap: Key:   Endknoten
	 *          Value: Kantengewicht
	 */
	public HashMap<Integer, HashMap<Integer, Double>> adjLists;
	
	/**
	 * Set mit Terminalen
	 */
	public Set<Integer> terminals;
	
	/**
	 * Set mit Knoten
	 */
	public Set<Integer> vertices;
	
	/**
	 * V = Anzahl Knoten, E = Anzahl Kanten
	 * Long erlauben uns bis zu ~9.000.000.000.000.000.000 Kanten 
	 * => max. ~3.000.000.000 Knoten bei vollständigem Graphen
	 */
	public int V;
	public long E;
	   
	
	/**
	 * Konstruktor für leeren Graphen
	 */
	public Graph(){
	    V = 0;
	    E = 0;
        terminals = new TreeSet<Integer>();
        vertices = new TreeSet<Integer>();
        adjLists = new HashMap<Integer, HashMap<Integer, Double>>();
	}
	
	/**
	 * Konstruktor für Graphen mit V vielen Knoten, nummeriert von 0 bis V-1
	 * @param V = Anzahl Knoten
	 * @throws IllegalArgumentException, falls V < 0
	 */
	public Graph(int V) {
		if(V < 0)
			throw new IllegalArgumentException("Anzahl Knoten muss nichtnegativ sein");
		this.V = V;
		E = 0;
        terminals = new TreeSet<Integer>();
        vertices = new TreeSet<Integer>();

		// Initiiere V viele Adj.Listen und Knoten
        adjLists = new HashMap<Integer, HashMap<Integer, Double>>();
        for (int i = 1; i <= V; i++){
            adjLists.put(i, new HashMap<Integer, Double>());
            vertices.add(i);
        }
	}
	   
    /**
     * Konstruktor für Graphen mit V vielen Knoten und Terminalmenge terminals
     * @param V = Anzahl Knoten
     * @param terminals = Menge der Terminale
     * @throws IllegalArgumentException, falls V < 0 oder ein Terminal nicht in V
     */
    public Graph(int V, Set<Integer> terminals) {
        if(V < 0)
            throw new IllegalArgumentException("Anzahl Knoten muss nichtnegativ sein");
        for(int i : terminals)
            if(i < 1 || V < i)
                throw new IllegalArgumentException("Terminale müssen in Knotenmenge des Graphen liegen");
            
        this.V = V;
        E = 0;
        this.terminals = new TreeSet<Integer>();
        this.terminals = (TreeSet<Integer>) terminals;
        vertices = new TreeSet<Integer>();

        // Initiiere V viele Adj.Listen und Knoten
        adjLists = new HashMap<Integer, HashMap<Integer, Double>>();
        for (int i = 1; i <= V; i++){
            adjLists.put(i, new HashMap<Integer, Double>());
            vertices.add(i);
        }
    }

    /**
     * Konstruktor für Graphen mit V vielen Knoten und Terminalmenge terminals
     * @param vertices = Menge Knoten
     * @param terminals = Menge der Terminale
     * @throws IllegalArgumentException, falls V < 0 oder ein Terminal nicht in V
     */
    public Graph(Set<Integer> vertices, Set<Integer> terminals) {
        if(V < 0)
            throw new IllegalArgumentException("Anzahl Knoten muss nichtnegativ sein");
        if(terminals == null)
            terminals = new TreeSet<Integer>();
        for(int i : terminals)
            if(!vertices.contains(i))
                throw new IllegalArgumentException("Terminale müssen in Knotenmenge des Graphen liegen");
            
        this.V = vertices.size();
        E = 0;
        this.terminals = new TreeSet<Integer>();
        this.terminals = (TreeSet<Integer>) terminals;
        this.vertices = new TreeSet<Integer>();
        this.vertices = (TreeSet<Integer>) vertices;

        // Initiiere eine Adj.Liste für jeden Knoten
        adjLists = new HashMap<Integer, HashMap<Integer, Double>>();
        for (int i : vertices){
            adjLists.put(i, new HashMap<Integer, Double>());
        }
    }
    

    /**
     * Setzt Menge der Terminale, falls zulässig
     * @param terminals = Menge der Terminale
     */
    public void setTerminals(Set<Integer> terminals){
        if(vertices.containsAll(terminals)){
            this.terminals = new TreeSet<Integer>();
            this.terminals = terminals;
        }
        else
            throw new IllegalArgumentException("Terminale müssen in Knotenmenge liegen");
    }
    
    /**
     * Füge  Knoten zu Terminalen hinzu, falls Knoten zulässig
     * @param v = hinzuzufügender Knoten
     */
    public boolean addTerminal(int v){
        isVert(v);
        return terminals.add(v);
    }
    
    
    @Override
	public void printGraph(){
	    System.out.println("Graphausgabe startet");
	    System.out.println("SECTION Graph");
	    System.out.println("Nodes " + V);
	    System.out.println("Edges " + E);
        adjLists.forEach((start, adjOfStart) ->{
            adjOfStart.forEach((end, weight) ->{
                System.out.println("E " + start + " " + end + " " + weight);
            });
        });
	    System.out.println("END\n\nSECTION Terminals");
	    System.out.println("Terminals " + terminals.size());
	    for(int i : terminals)
	        System.out.println("T " + i);
	    System.out.println("END");
	    System.out.println("Graphausgabe beendet");
	}
    
    @Override
    public void printGraph(Writer writer) throws IOException {
        System.out.println("Graphausgabe startet");
        writer.write("SECTION Graph\n");
        writer.write("Nodes " + V + "\n");
        writer.write("Edges " + E + "\n");
        adjLists.forEach((start, adjOfStart) ->{
            adjOfStart.forEach((end, weight) ->{
                try {
                    writer.write("E " + start + " " + end + " " + weight + "\n");
                } 
                catch (IOException ie) {
                    ie.printStackTrace();
                }
            });
        });
        writer.write("END\n\nSECTION Terminals\n");
        writer.write("Terminals " + terminals.size() + "\n");
        for(int i : terminals)
            writer.write("T " + i + "\n");
        writer.write("END\n");
        System.out.println("Graphausgabe beendet");
    }
    
	
	@Override
    public boolean addVert(int v){
        if(vertices.add(v)){
            V++;
            //Lege neue Adj.Liste für hinzugefügten Knoten an
            adjLists.put(v, new HashMap<Integer, Double>());
            return true;
        }
        return false;
    }
    
	@Override
    public boolean remVert(int v){
        if(vertices.remove(v)){
            V--;
            //Leere die Adj.Liste vom gelöschten Knoten und reduziere Kantenzahl E
            E -= adjLists.get(v).size();
            adjLists.get(v).clear();
            //lösche alle Kanten, die zum gelöschten Knoten führen
            adjLists.forEach(
                    (start, adjOfStart) -> {
                        //Falls gelöschter Knoten noch ein Endknoten einer Kante ausgehend von Knoten start ist,
                        //lösche Kante und dekrementiere Kantenzahl E
                        if(adjOfStart.remove(v) != null)
                            E--;
                    }
                    );
            return true;
        }
        return false;
    }
	
	
    @Override
    public boolean addEdge(Edge e) {
        return addEdge(e.start, e.end, e.weight);
    }

	@Override
	public boolean addEdge(int start, int end, double weight) {
		// Prüfe, ob Knoten im Graphen liegen
		isVert(start);
		isVert(end);
		
		// Prüfe, ob Kantengewicht zulässig ist
		if(weight < 0)
			throw new IllegalArgumentException("Kantengewichte müssen nichtnegativ sein");

        //Falls Kante mit gleichen Start-, Endknoten bereits enthalten, wähle Minimum der beiden Kosten
        if (adjLists.get(start).containsKey(end)){
            if(adjLists.get(start).get(end) > weight){
                adjLists.get(start).put(end, weight);
                return true;
            }
            return false;
        }
        //Sonst inkrementiere E und füge Kante hinzu
        else{
            adjLists.get(start).put(end, weight);
            E++;
            return true;
        }
	}
	
    @Override
    public boolean remEdge(Edge e) {
        // Prüfe, ob Knoten im Graphen liegen
        isVert(e.start);
        isVert(e.end);
        
        // Prüfe, ob Kantengewicht zulässig ist
        if(e.weight < 0)
            throw new IllegalArgumentException("Kantengewichte müssen nichtnegativ sein");

        //Falls Kante wirklich entfernt, dekrementiere Kantenzahl E
        if(adjLists.get(e.start).remove(e.end, e.weight)){
            E--;
            return true;
        }
        return false;
    }
	
	@Override
	public boolean remEdge(int start, int end, double weight) {
		// Prüfe, ob Knoten im Graphen liegen
		isVert(start);
		isVert(end);
		
		// Prüfe, ob Kantengewicht zulässig ist
		if(weight < 0)
			throw new IllegalArgumentException("Kantengewichte müssen nichtnegativ sein");
	
        //Falls Kante wirklich entfernt, dekrementiere Kantenzahl E
        if(adjLists.get(start).remove(end, weight)){
            E--;
            return true;
        }
        return false;
	}

	
	@Override
	public Iterable<Edge> getEdges() {
		Set<Edge> edges = new TreeSet<Edge>();
		for (int start : vertices)
		    adjLists.get(start).forEach((end, weight) -> edges.add(new Edge(start, end, weight)));
		return edges;
	}
	
		
	@Override
	public boolean isVert(int v){
		if (!vertices.contains(v))
			throw new IllegalArgumentException("Knoten liegt nicht in Knotenmenge");
		return true;
	}


	@Override
	public int degreeOut(int v){
	    isVert(v);
        return adjLists.get(v).size();
    }
	
    @Override
    /**
     * keine schöne Laufzeit wegen Adj.Liste. Mit Adj.Matrix wäre das schneller
     */
    public int degreeIn(int v){
        isVert(v);
        int deg = 0;
        //Durchlaufe alle Adj.Listen  
        //und erhöhe deg, falls v dort Endknoten einer Kante ist
        for(int start : vertices)
            if(adjLists.get(start).containsKey(v))
                deg++;
        return deg;
    }

}
