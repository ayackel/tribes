package org.yackel.ga;

public class RandomGenerator {

	/**
	 * State for random number generation
	 */
	private static volatile long state = xorShift64(System.nanoTime() | 0xCAFEBABE);

	/**
	 * Gets a long random value
	 * 
	 * @return Random long value based on static state
	 */
	public static final long nextLong(int bits) {
		long a = state;
		state = xorShift64(a);
		
		return a >>> (64 - bits);
	}

	/**
	 * XORShift algorithm - credit to George Marsaglia!
	 * 
	 * @param a
	 *            Initial state
	 * @return new state
	 */
	private static final long xorShift64(long a) {
		a ^= (a << 21);
		a ^= (a >>> 35);
		a ^= (a << 4);
		return a;
	}

}
