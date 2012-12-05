package org.yackel.ga;

public class Genotype {

	private static long nextId = 0;

	long id = nextId++;
	long gene;
	double fitness;
	int generation;

	long parentId1, parentId2;
	int xoverCutPoint;
	boolean mutated = false;

	public double convertGene() {
		double x = -1.0 + gene * 3.0 / (Math.pow(2, 22) - 1);
		return x;
	}

	static {
		System.out.println();
	}

	@Override
	public String toString() {
		return id + (mutated ? "*" : "") + "-" + parentId1 + "," + parentId2 + (xoverCutPoint > 0 ? "@" + xoverCutPoint : "")
				+ " : " + Long.toBinaryString(gene) + " : " + convertGene()
				+ " : " + fitness;
	}

}
