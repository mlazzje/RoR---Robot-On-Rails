package test.ror.core;

import static org.junit.Assert.*;

import org.junit.Test;

import ror.core.Output;
import ror.core.Product;
import ror.core.Rail;

public class OutputTest {

	@Test
	public void test() {
		Rail testOutputRail = new Rail(0, 0, null, null, null, null);
		Rail testOutputRail2 = new Rail(1, 0, null, null, null, null);
		int x=0;
		int y=0;
		Product p = new Product("test");
		Output outputTest = new Output(x, y, testOutputRail);
		Output outputTestNull = new Output(1, 1, null);
		
		// test getters
		assertTrue(outputTest.getAccess().equals(testOutputRail));
		assertFalse(outputTest.getAccess().equals(testOutputRail2));
		
		// test setter
		outputTestNull.setAccess(testOutputRail);
		assertTrue(outputTestNull.getAccess().equals(testOutputRail));
		assertFalse(outputTestNull.getAccess().equals(testOutputRail2));
	}

}
