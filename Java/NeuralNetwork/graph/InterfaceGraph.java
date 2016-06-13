package graph;

import java.io.IOException;
import java.io.Writer;

public interface InterfaceGraph {
	
    /**
     * Gibt den Graphen in Form der Dimacs Instanzen in der Konsole aus
     */
    public void printGraph();
    
    /**
     * Gibt den Graphen in Form der Dimacs Instanzen durch writer aus
     * @throws IOException
     */
    public void printGraph(Writer writer) throws IOException;
    
    /** F�gt Knoten v dem Graphen hinzu
     * @param v = hinzuzuf�gender Knoten
     * @return true, falls Knotenmenge ver�ndert wurde
     *         false, sonst
     */
    public boolean addVert(int v);  
    
    /** L�scht Knoten v aus dem Graphen, falls existent
     * @param v = zu l�schender Knoten
     * @return true, falls Knotenmenge ver�ndert wurde
     *         false, sonst
     */
    public boolean remVert(int v);
    
	/**
	 * F�gt Kante von Knoten start zu end mit Gewicht weight hinzu, 
	 * falls noch keine Kante von start zu end existiert oder die einzuf�gende g�nstiger ist
	 * @param start = Startknoten der Kante
	 * @param end = Endknoten der Kante
	 * @param weight = Gewicht der Kante
	 * @return true, falls Kantenmenge ver�ndert wurde
	 *         false, sonst
	 * @throws IllegalArgumentException, falls Kantengewicht < 0
	 * @throws IndexOutOfBoundsException, falls start oder ziel nicht in Knotenmenge enthalten
	 */
	public boolean addEdge(int start, int end, double weight);

    /**
     * F�gt Kante e hinzu,
     * falls noch keine Kante mit gleichen Knoten existiert oder die einzuf�gende g�nstiger ist
     * @param e = hinzuzuf�gende Kante
     * @return true, falls Kantenmenge ver�ndert wurde
     *         false, sonst
     * @throws IllegalArgumentException, falls Kantengewicht < 0
     * @throws IndexOutOfBoundsException, falls e.start oder e.ziel nicht in Knotenmenge enthalten
     */
    public boolean addEdge(Edge e);
    
	/**
	 * L�scht Kante von Knoten start zu end mit Gewicht weight, falls existent
	 * @param start = Startknoten der Kante
	 * @param end = Endknoten der Kante
	 * @param weight = Gewicht der Kante
	 * @return true, falls Kantenmenge ver�ndert wurde
	 *         false, sonst
	 * @throws IndexOutOfBoundsException, falls start oder ziel nicht in Knotenmenge enthalten
	 */
	public boolean remEdge(int start, int end, double weight);

	/**
	 * L�scht Kante e aus dem Graphen, falls existent
	 * @param e = zu l�schende Kante
	 * @return true, falls Kantenmenge ver�ndert wurde
	 *         false, sonst
	 * @throws IndexOutOfBoundsException, falls e.start oder e.ziel nicht in Knotenmenge enthalten
	 */
	public boolean remEdge(Edge e);
	
	/**
	 * Gibt gesamte Kantenmenge aus
	 * Um �ber alle Kanten zu iterieren, nutze folgende Syntax:
	 * for (Edge e : G.getEdges()) {}
	 * @return Iterable mit allen Kanten des Graphen
	 */
	public Iterable<Edge> getEdges();
	
	/**
	 * Pr�ft, ob Knoten in Knotenmenge liegt
	 * @param v = zu pr�fender Knoten
	 * @throws IndexOutOfBoundsException, falls Knoten nicht im Graphen liegt
	 */
	public boolean isVert(int v);
	
	/**
	 * Gibt den Grad (ausgehender Kanten) eines Knoten aus
	 * @param v = Knoten dessen Grad ausgegeben werden soll
	 * @return +Grad von Knoten v
	 * @throws IllegalArgumentException, falls Knoten v nicht in Knotenmenge liegt
	 */
	public int degreeOut(int v);
	
	/**
     * Gibt den Grad (eingehender Kanten) eines Knoten aus
     * @param v = Knoten dessen Grad ausgegeben werden soll
     * @return -Grad von Knoten v
     * @throws IllegalArgumentException, falls Knoten v nicht in Knotenmenge liegt
     */
    public int degreeIn(int v);
}
