package org.yackel.ga;

import java.util.ArrayList;
import java.util.List;

public class Solver {

	private static final int POP_SIZE = 10;
	private static final double perXover = 0.25;
	private static final double perMutation = 0.01;
	private static final int BIT_LENGTH = 22;

	int time = 0;
	Genotype[] population;
	Genotype[] newPopulation;
	double cummFitness;
	Genotype mostFit = new Genotype();

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new Solver().execute();
	}

	private void execute() {
		initialize();
		evaluate();

		while (time < 100) {
			System.out.println("\nGeneration " + time + "\n");
			printPopulation(population);

			time++;
			selectNewPop();
			alter();
			population = newPopulation;
			evaluate();
		}
	}

	private void initialize() {
		population = new Genotype[POP_SIZE];
		for (int i = 0; i < POP_SIZE; i++) {
			population[i] = new Genotype();
			population[i].gene = RandomGenerator.nextLong(BIT_LENGTH);
			population[i].generation = time;
		}
	}

	private void evaluate() {
		cummFitness = 0;
		for (Genotype genotype : population) {
			genotype.fitness = evalFitness(genotype);
			cummFitness += genotype.fitness;
			if (genotype.fitness > mostFit.fitness) {
				mostFit = genotype;
			}
		}
	}

	private double evalFitness(Genotype genotype) {
		double x = genotype.convertGene();
		return x * Math.sin(10.0 * Math.PI * x) + 1.0;
	}

	private void selectNewPop() {
		newPopulation = new Genotype[POP_SIZE];
		newPopulation[0] = mostFit;
		for (int i = 1; i < POP_SIZE; i++) {
			double random = Math.random();
			double perChange = 0.0;
			for (Genotype genotype : population) {
				perChange += genotype.fitness / cummFitness;
				if (random < perChange) {
					newPopulation[i] = genotype;
					break;
				}
			}
		}
	}

	private void alter() {
		crossover();
		mutation();
	}

	private void crossover() {
		List<Integer> xoverIndexes = selectXoverGenes();
		for (int i = 0; i < xoverIndexes.size(); i += 2) {
			Genotype gene1 = newPopulation[xoverIndexes.get(i)];
			Genotype gene2 = newPopulation[xoverIndexes.get(i + 1)];
			Genotype[] newGenes = doCrossover(gene1, gene2);
			newPopulation[xoverIndexes.get(i)] = newGenes[0];
			newPopulation[xoverIndexes.get(i + 1)] = newGenes[1];
		}
	}

	private List<Integer> selectXoverGenes() {
		List<Integer> xoverIndexes = new ArrayList<Integer>();
		for (int i = 1; i < POP_SIZE; i++) {
			if (Math.random() > perXover) {
				xoverIndexes.add(i);
			}
		}
		if (xoverIndexes.size() % 2 != 0) {
			if (Math.random() < .5) {
				int indexToAdd = (int) (Math.random() * POP_SIZE);
				xoverIndexes.add(indexToAdd);
			} else {
				xoverIndexes.remove(xoverIndexes.size() - 1);
			}
		}
		return xoverIndexes;
	}

	private Genotype[] doCrossover(Genotype gene1, Genotype gene2) {
		Genotype[] genes = new Genotype[2];
		genes[0] = new Genotype();
		genes[0].generation = time;
		genes[1] = new Genotype();
		genes[1].generation = time;

		int swapBit = (int) (Math.random() * BIT_LENGTH);

		genes[0].gene = doSwap(gene1.gene, gene2.gene, swapBit);
		genes[1].gene = doSwap(gene2.gene, gene1.gene, swapBit);

		genes[0].xoverCutPoint = swapBit;
		genes[1].xoverCutPoint = swapBit;
		genes[0].parentId1 = gene1.id;
		genes[0].parentId2 = gene2.id;
		genes[1].parentId1 = gene2.id;
		genes[1].parentId2 = gene1.id;

		return genes;
	}

	long doSwap(long gene1, long gene2, int swapBit) {
		long temp = gene1 >>> swapBit;
		return (gene2 & ((1 << (BIT_LENGTH - swapBit)) - 1))
				| (temp << swapBit);
	}

	private void mutation() {
		for (int i=1; i<POP_SIZE; i++) {
			Genotype genotype = newPopulation[i];
			for (int bit = 0; bit < BIT_LENGTH; bit++) {
				if (Math.random() < perMutation) {
					genotype.gene ^= 1L << bit;
					genotype.mutated = true;
				}
			}
		}
	}

	private void printPopulation(Genotype[] population) {
		for (Genotype genotype : population) {
			System.out.println(genotype);
		}
		System.out.println("cumm fit: " + cummFitness + "\tmost fit: "
				+ mostFit.convertGene() + "\t" + mostFit.fitness);
	}
}
