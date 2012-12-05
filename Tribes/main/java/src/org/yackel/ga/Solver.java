package org.yackel.ga;

import java.util.ArrayList;
import java.util.List;

public class Solver {

	private static final int POP_SIZE = 20;
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

		printPopulation(population);

		while(time < 100) {
//      System.out.println("\nGeneration " + time + "\n");
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
		for(int i=0; i<POP_SIZE; i++) {
			population[i] = new Genotype();
			population[i].gene = RandomGenerator.nextLong(BIT_LENGTH);
			population[i].generation = time;
		}
	}

	private void evaluate() {
		cummFitness = 0;
		for(Genotype genotype : population) {
			genotype.fitness = evalFitness(genotype.gene);
			cummFitness += genotype.fitness;
			if(genotype.fitness > mostFit.fitness) {
        mostFit = genotype;
      }
		}
	}

	private double evalFitness(long gene) {
		double x = convertGene(gene);
		return x*Math.sin(10.0*Math.PI*x) + 1.0;
	}

	private double convertGene(long gene) {
		double x = -1.0 + gene*3.0/(Math.pow(2, 22) - 1);
		return x;
	}

	private void selectNewPop() {
		newPopulation = new Genotype[POP_SIZE];
		newPopulation[0] = mostFit;
		for(int i=1; i<POP_SIZE; i++) {
			double random = Math.random();
			double perChange = 0.0;
			for(Genotype genotype : population) {
				perChange += genotype.fitness/cummFitness;
				if(random < perChange) {
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
	  for(int i=0; i<xoverIndexes.size(); i += 2) {
	    Genotype gene1 = newPopulation[xoverIndexes.get(i)];
      Genotype gene2 = newPopulation[xoverIndexes.get(i+1)];
      Genotype[] newGenes = doCrossover(gene1, gene2);
      newPopulation[xoverIndexes.get(i)] = newGenes[0];
      newPopulation[xoverIndexes.get(i+1)] = newGenes[1];
	  }
	}

  private List<Integer> selectXoverGenes() {
    List<Integer> xoverIndexes = new ArrayList<Integer >();
		for(int i=1; i<POP_SIZE; i++) {
			if(Math.random() > perXover) {
				xoverIndexes.add(i);
			}
		}
		if(xoverIndexes.size() % 2 != 0) {
      if(Math.random() < .5) {
        int indexToAdd = (int) (Math.random() * 20.0);
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

    return genes;
  }

  long doSwap(long gene1, long gene2, int swapBit) {
    long temp = gene1 >>> swapBit;
    return (gene2 & ((1 << (BIT_LENGTH - swapBit)) - 1)) | (temp << swapBit);
  }

	private void mutation() {
    for(Genotype genotype : newPopulation) {
      for(int i=1; i<BIT_LENGTH; i++) {
        if(Math.random() < perMutation) {
          genotype.gene ^= 1L << i;
        }
      }
    }
	}

	private void printPopulation(Genotype[] population) {
//		for(Genotype genotype : population) {
//			System.out.println(genotype.id + " : " + Long.toBinaryString(genotype.gene) + " : " + convertGene(genotype.gene) + " : " + genotype.fitness);
//		}
		System.out.println("cumm fit: " + cummFitness + "\tmost fit: " + convertGene(mostFit.gene) + "\t" + mostFit.fitness);
	}
}
