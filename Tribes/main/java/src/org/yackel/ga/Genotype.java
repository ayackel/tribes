package org.yackel.ga;

public class Genotype {

	private static long nextId = 1;

	long id = nextId++;
	long gene;
	double fitness;
	int generation;
}
