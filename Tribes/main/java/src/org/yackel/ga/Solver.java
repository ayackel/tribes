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
		
//		while(true) {
//			time++;
			selectNewPop();
//			alter();
//			population = newPopulation;
//			evaluate(population);
//		}		

			System.out.println("\nGeneration 2\n");
			printPopulation(newPopulation);

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
		for(int i=0; i<POP_SIZE; i++) {
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
		List<Integer> xoverIndexes = new ArrayList<Integer >();
		for(int i=0; i<POP_SIZE; i++) {
			double random = Math.random();
			if(random > perXover) {
				xoverIndexes.add(i);
			}
		}		
	}

	private void mutation() {
		// TODO Auto-generated method stub
		
	}

	private void printPopulation(Genotype[] population) {
		for(Genotype genotype : population) {
			System.out.println(genotype.id + " : " + Long.toBinaryString(genotype.gene) + " : " + convertGene(genotype.gene) + " : " + genotype.fitness);
		}
	}
}
