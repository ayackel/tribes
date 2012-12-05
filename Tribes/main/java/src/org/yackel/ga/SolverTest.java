package org.yackel.ga;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class SolverTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testDoSwap() {
		Solver solver = new Solver();

		long swapped = solver.doSwap(Long.parseLong("110110", 2),
				Long.parseLong("011000", 2), 4);
		System.out.println(Long.toBinaryString(swapped));
		assertEquals(swapped, Long.parseLong("111000", 2));
	}

	@Test
	public void testDoSwap2() {
		Solver solver = new Solver();

		long swapped = solver.doSwap(
				Long.parseLong("100100001111100010011", 2),
				Long.parseLong("1101111100011001111111", 2), 6);
		System.out.println(Long.toBinaryString(swapped));
		assertEquals(swapped, Long.parseLong("100101101111101111111", 2));

		swapped = solver.doSwap(Long.parseLong("1101111100011001111111", 2),
				Long.parseLong("100100001111100010011", 2), 6);
		System.out.println(Long.toBinaryString(swapped));
		assertEquals(swapped, Long.parseLong("1101111101111101010011", 2));
	}
}
