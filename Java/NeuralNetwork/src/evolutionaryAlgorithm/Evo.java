package evolutionaryAlgorithm;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import basics.FastEntry;
import basics.Measure;

public class Evo {
	private int pop_size;
	private int genome_length;
	private int numElite;
	private double[] fitness;
	private double[][] population;
	private boolean[] elite;
	private double mutate_probability;
	private int numCrossover;
	private Measure<double[]> measure;
	// count of elite, which are directly transfered to the next generation
	private PriorityQueue<FastEntry> ranking;

	public Evo(int genome_length, int pop_size, int numElite, double mutate_probability, int numCrossover,
			Measure<double[]> measure) {
		if (pop_size < 1)
			throw new IllegalArgumentException("Die Populationsgr��e muss positiv sein!");
		// if(mutator == null) throw new IllegalArgumentException("Der
		// Mutationsklasse darf nicht null sein!");
		if (genome_length < 2)
			throw new IllegalArgumentException("Die Anzahl der St�tzstellen muss mindestens 2 sein!");
		if (mutate_probability >= 1 && mutate_probability <= 0)
			throw new IllegalArgumentException();
		this.pop_size = pop_size;
		this.mutate_probability = mutate_probability;
		this.genome_length = genome_length;
		this.numElite = numElite;
		this.numCrossover = numCrossover;
		this.elite = new boolean[pop_size];
		// init the measurearray
		this.fitness = new double[pop_size];
		Arrays.fill(fitness, Double.NaN);
		this.measure = measure;

		// init the population
		this.population = new double[pop_size][genome_length];

		for (int i = 0; i < pop_size; i++) {
			double[] individual = new double[genome_length];
			for (int j = 0; j < genome_length; j++) {
				individual[j] = ThreadLocalRandom.current().nextDouble();
			}
			population[i] = individual;
		}

		// init the priorityqueue to get quick acess to the elite
		ranking = new PriorityQueue<FastEntry>(pop_size, Map.Entry.comparingByKey());
	}

	public void adoptAdaptDevelop(int iterations) {
		for (int i = 1; i < iterations; i++) {
			// 1. eval
			eval();
			// 2.select & 3.reproduct
			select();
			// 4.mutate
			mutateNCross();
		}
	}

	public double[] getBest() {
		eval();
		double max = fitness[0];
		int max_index = 0;
		for (int i = 0; i < pop_size; i++)
			if (max < fitness[i]) {
				max = fitness[i];
				max_index = i;
			}
		return population[max_index];
	}

	private void eval() {
		for (int i = 0; i < pop_size; i++) {
			fitness[i] = 1 / measure.measure(population[i]);
			ranking.offer(new FastEntry(fitness[i], i));
		}
	}

	private void select() {
		Arrays.fill(elite, true);
		double[][] new_pop = new double[pop_size][genome_length];
		Random rand = new Random();
		double rest = pop_size - numElite;
		double[] sectors = new double[(int) rest];
		int[] indexToSector = new int[(int) rest];

		/*
		 * Computes the upper bounds of the sectors on the wheel of fortune and
		 * saves them in the sectors-Array. The size of a sector is proportial
		 * to its probability, so the following holds: sector[i+1]-sectors[i] =
		 * (1- (double) (rest-i) / (double) rest)*(2.0/(rest+1)
		 * 
		 * Problem: the order of the intervals is always the same! Problem: Not
		 * sure if its a real distribution
		 */
		int index = ranking.poll().getValue();
		elite[index] = false;
		sectors[0] = 0;
		indexToSector[0] = index;
		for (int i = 1; i < rest; i++) {
			index = ranking.poll().getValue();
			elite[index] = false;
			sectors[i] = sectors[i - 1] + 2 * (rest - i) / ((rest - 1) * rest);
			indexToSector[i - 1] = index;
		}

		for (int i = 0; i < sectors.length - 1; i++) {
			if (sectors[i] > sectors[i + 1])
				throw new RuntimeException("Error");
		}

		/*
		 * Selects the individuals. If the individual is an elite(indicated by
		 * the elite-Array) it's directly taken in the new population otherwise
		 * a wheel of fortune-selection is used
		 */
		for (int i = 0; i < pop_size; i++) {
			int tmp = Math.abs(Arrays.binarySearch(sectors, rand.nextDouble()));
			if (tmp == rest)
				tmp--;
			new_pop[i] = population[elite[i] ? i : indexToSector[tmp]];
		}

		ranking.clear();
		this.population = new_pop;

	}

	private void mutateNCross() {
		Random rand = new Random();
		double range;
		/*
		 * Only non-elite can be mutated. The mutation probability is a class
		 * variable. The range of the mutation is bounded by twice of the
		 * currentvalue of the gene
		 */
		for (int i = 0; i < pop_size; i++) {
			// only mutate non-elite
			if (!elite[i]) {
				for (int j = 0; j < genome_length; j++) {
					if (rand.nextDouble() < mutate_probability) {
						range = 1.5 * Math.abs(population[i][j]);
						population[i][j] = ThreadLocalRandom.current().nextDouble(-range, range + 1);
					}
				}

			}
		}

		/*
		 * Crossovermethod: Shuffle Crossover. The Count of Crossovers is
		 * regulated by the class variable numCrossover.
		 *
		 * for(int i=0; i<numCrossover;i++){ //select two non-elite
		 * 
		 * }
		 */
	}

}
