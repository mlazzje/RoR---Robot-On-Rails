package test.ror.core;

import static org.junit.Assert.*;

import org.junit.Test;

import ror.core.Input;
import ror.core.Product;
import ror.core.Rail;

public class InputTest {

	@Test
	public void test() {
		Rail testInputRail = new Rail(0, 0, null, null, null, null);
		Rail testInputRail2 = new Rail(1, 0, null, null, null, null);
		int x=0;
		int y=0;
		Product p = new Product("test");
		Input inputTest = new Input(x, y, testInputRail);
		Input inputTestNull = new Input(1, 1, null);
		
		// test getters
		assertTrue(inputTest.getAccess().equals(testInputRail));
		assertFalse(inputTest.getAccess().equals(testInputRail2));
		
		// test setter
		inputTestNull.setAccess(testInputRail);
		assertTrue(inputTestNull.getAccess().equals(testInputRail));
		assertFalse(inputTestNull.getAccess().equals(testInputRail2));
		
		// test methods
		assertTrue(inputTestNull.addProduct(p));
		assertTrue(inputTestNull.inputProductsCount()==1);
		assertFalse(inputTestNull.inputProductsCount()==0);
		assertTrue(inputTestNull.removeProduct(p));
		assertTrue(inputTestNull.inputProductsCount()==0);
		assertFalse(inputTestNull.inputProductsCount()==1);
		assertFalse(inputTestNull.removeProduct(p));
	}

}
