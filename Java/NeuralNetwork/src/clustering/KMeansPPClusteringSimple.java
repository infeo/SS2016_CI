package clustering;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
/**
 * Class implementing the k-means-clustering-algorithm for solving the
 * k-clustering-problem Erweiterungsideen: - Schlechte eingabe mitttels
 * evolution�ren algorithmus finden - g�ltige Instanzen folgenderma�en erzeugen:
 * Setzte zuf�lligen radius & zentrum. Setzte in diesem eine zuf�llige anzahl an
 * Punkten. widerhole
 * 
 * @author Armin Schrenk
 *
 */
public class KMeansPPClusteringSimple {

	/**
	 * Number of Clusters
	 */
	private int k;

	/**
	 * Dimesion of the Input
	 */
	private int dimension;

	/**
	 * Array, which contains the center of each cluster
	 */
	private double[][] centers;

	/**
	 * Array of Sets, which equals the Pointset of each Cluster
	 */
	private Multiset<double[]>[] clstpoints;

	/**
	 * Mapping of each Point from the input to its containing cluster
	 */
	private Map<double[], Integer> map;

	/**
	 * the input
	 */
	ArrayList<double[]> input;
	
	/**
	 * a PRNG
	 */
	Random rand = new Random(System.nanoTime());
	
	/**
	 * Initializes a Clustering with random centers between min- and maxbound
	 * @param k
	 * @param input
	 * @throws IllegalArgumentException
	 * @throws IndexOutOfBoundsException
	 */
	public KMeansPPClusteringSimple(int k, Collection<double[]> input)
			throws IllegalArgumentException, IndexOutOfBoundsException {
		if (k < 1) {
			throw new IllegalArgumentException("the Count of Clusters must be greater than 0");
		}
		if (input.isEmpty())
			throw new IllegalArgumentException("The Input set is empty!");

		this.dimension = input.iterator().next().length;
		input.forEach(elem -> {
			if (elem.length != dimension)
				throw new IllegalArgumentException("Some Elements of the Input have the wrong dimension");
		});

		this.k = k;
		this.input = new ArrayList<>();
		this.input.addAll(input);
		this.clstpoints = (Multiset<double[]>[]) new HashMultiset[k];
		this.centers = new double[k][dimension];
		for (int i = 0; i < k; i++) {
			clstpoints[i] = HashMultiset.create();
		}
		map = new HashMap<double[], Integer>();
		init();
	}

	/**
	 * This method initializes the clusters with the k-means++ method
	 */
	private boolean init() {
		//the size of the input set
		//index pointing to the new center 
		int size = input.size(),index;
		//probability interval
		double [] probInterval = new double[size];
		//vector to save the nearest center for each point
		int [] nearestCenter = new int[size];
		Arrays.fill(nearestCenter, 0);
		//distance to a clustercenter (not necessarily the nearest)
		//the picked double between 0 and 1.0
		double dist, pick;
		double min_dist = Double.POSITIVE_INFINITY;
		int clstNr = -1;
		double [] tmp;
		
		//1.Step: choose uniformly a center from the input
		centers[0] = Arrays.copyOf(input.get(rand.nextInt(size)),dimension);
		//2.repeat until k centers are chosen
		for(int i=1; i<k;i++){		
			//2.1 compute distance from each point to its closest center
			probInterval[0] = dist(input.get(0), centers[nearestCenter[0]],true);
			for(int j =1; j<size;j++){
				probInterval[j] = dist(input.get(j), centers[nearestCenter[j]],true) + probInterval[j-1];
			}
			//2.2 compute the random number and distribution to this point
			pick = rand.nextDouble();
			probInterval[0]/=probInterval[size-1];
			index=0;
			while(pick>probInterval[index]){
				index++;
				probInterval[index]/=probInterval[size-1];
			}
			
//			//2.1 select new center with special probablilty from the input
			centers[i]=Arrays.copyOf(input.get(index),dimension);;
			
			//update the nearest center vector
			for (int j=0;j<size;j++) {
				min_dist = Double.POSITIVE_INFINITY;
				clstNr = -1;
				tmp = input.get(j);
				// compute the nearest clustercenter
				for (int l = 0; l <= i; l++) {
					dist = dist(tmp, centers[l],false);
					if (dist < min_dist) {
						min_dist = dist;
						clstNr = l;
					}
				}
				nearestCenter[j]=clstNr;
			}			
		}
		return true;
	}
	
	/**
	 * Computes a k-mean-clustering of the given input
	 * 
	 * @param input
	 *            a set of inputs
	 * @throws UnsupportedOperationException
	 *             if the underlying data structures does not support certain
	 *             operations
	 */
	public void compute() throws UnsupportedOperationException {

		boolean hasChanged = true;
		boolean firstIteration = true;
		while (hasChanged) {
			hasChanged = false;

			// the minimal distance
			double min_dist;
			// the current distance of the current point to a cluster
			double dist;
			int clstNr;
			int old_clstNr;

			// traverse the whole input set
			for (double[] curr : input) {
				min_dist = Double.POSITIVE_INFINITY;
				clstNr = -1;
				old_clstNr = -1;

				// compute the nearest clustercenter
				for (int i = 0; i < k; i++) {
					dist = dist(curr, centers[i],false);
					if (dist < min_dist) {
						min_dist = dist;
						clstNr = i;
					}
				}
				// if something has changed
				if (firstIteration) {
					map.put(curr, clstNr);
					clstpoints[clstNr].add(curr);
					hasChanged = true;
				} else if (clstNr >= 0 && map.get(curr) != clstNr) {
					old_clstNr = map.put(curr, clstNr);
					clstpoints[clstNr].add(curr);
					clstpoints[old_clstNr].remove(curr);
					hasChanged = true;
				}
			}

			// compute the new cluster center
			// for the future: use dynamic programming
			for (int i = 0; i < k; i++) {
				// sum over all points in a cluster
				double[] newcenter = new double[dimension];
				Arrays.fill(newcenter, 0);
				for (double[] pt : clstpoints[i]) {
					for (int j = 0; j < dimension; j++)
						newcenter[j] += pt[j];
				}
				for (int j = 0; j < dimension; j++)
					newcenter[j] = newcenter[j] / clstpoints[i].size();
				centers[i] = newcenter;
			}
			firstIteration = false;
		}
	}

	public double[][] getCenters() {
		return centers;
	}
	
	public double[] getRadiants(){
		double [] radiants = new double [k];
		for(int i=0; i<k;i++){
			radiants[i]=0;
			Multiset<double[]> cluster = clstpoints[i];
			for(double[] point : cluster){
				double rad = dist(point,centers[i],false);
				if(rad> radiants[i]){
					radiants[i]=rad;
				}
			}
		}
		return radiants;
	}

	public Collection<double[]>[] getClstpoints() {
		return clstpoints;
	}

	public double getCost() {
		double c = 0;
		for (int i = 0; i < k; i++) {
			for (double[] pt : clstpoints[i]) {
					c += dist(pt,centers[i],true);
			}
		}
		return c;
	}

	/**
	 * sets the given k and compute new centers with the new bounds
	 * 
	 * @param k
	 * @param minbound
	 * @param maxbound
	 */
	public void setK(int k, int minbound, int maxbound) {
		int oldK = this.k;
		this.k = k;
		clstpoints = Arrays.copyOf(clstpoints, k);
		for (int i = 0; i < k; i++) {
			if (i >= oldK)
				clstpoints[i] = HashMultiset.create();
			else
				clstpoints[i].clear();
		}
		this.centers = new double[k][dimension];
		init();
	}

	public int getK() {
		return k;
	}
	
	private double dist(double[] a, double[] b, boolean squared) {
		double dist=0;
		for(int i=0;i<a.length;i++){
			dist+=Math.pow(b[i]-a[i],2);
		}
		return squared?dist:Math.sqrt(dist);
	}
}