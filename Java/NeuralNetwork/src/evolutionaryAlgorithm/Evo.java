package evolutionaryAlgorithm;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import basics.Measure;
import basics.MyEntry;

public class Evo {
	private int pop_size;
	private int genome_length;
	private double [] fitness;
	private HashSet<double []> population;
	private Measure<double []> measure;
	private Mutator<double []> mutator;
	//count of elite, which are directly transfered to the next generation
	private int numElite;
	private double [][] elite;
	private PriorityQueue<MyEntry<Double,double[]>> ranking;
	
	public Evo(int genome_length, int pop_size, int numElite, Mutator<double[]> mutator){
		if(pop_size<1) throw new IllegalArgumentException("Die Populationsgröße muss positiv sein!");
		if(mutator == null) throw new IllegalArgumentException("Der Mutationsklasse darf nicht null sein!");
		if(genome_length<2) throw new IllegalArgumentException("Die Anzahl der Stützstellen muss mindestens 2 sein!");
		this.pop_size=pop_size;
		this.mutator = mutator;
		this.genome_length=genome_length;
		this.numElite=numElite;
		
		
		//init the measurearray
		this.fitness = new double [pop_size];
		Arrays.fill(fitness, Double.NaN);
		
		
		//init the elite array
		this.elite = new double[numElite][genome_length];
		
		
		//Generate the individuals
		population = new HashSet<double[]>();
		for(int i =0;i<pop_size;i++){
			double [] individual = new double[genome_length];
			for(int j =0; j<genome_length; j++){
				individual[j]=ThreadLocalRandom.current().nextDouble();
			}
			population.add(individual);
		}
		
		//init the priorityqueue to get quick acess to the elite
		ranking = new PriorityQueue<MyEntry<Double,double []>>(Map.Entry.comparingByKey());
	}
	
	public void adoptAdaptDevelop(int iterations){
		for(int i =1; i<iterations;i++){
			//1. eval
			eval();
			//2.select
			select();
			//3. reproduct
			//4.mutate
		}
	}
	
	private void eval(){
		int i=0;
		for(double [] individual : population){
			fitness[i]=measure.measure(individual);
			ranking.add(new MyEntry<Double,double[]>(fitness[i],individual));
			i++;
		}
	}
	
	private void select(){
		HashSet<double[]> new_pop= new HashSet<double[]>();
		for(int i=0; i<numElite;i++){
			double [] tmp = ranking.poll().getValue();
			new_pop.add(tmp);
			population.remove(tmp);
		}
		
		Random rand = new Random();
		while(!ranking.isEmpty()){
			double fit = ranking.peek().getKey();
			double [] tmp = ranking.poll().getValue();
			
		}
	}
}
