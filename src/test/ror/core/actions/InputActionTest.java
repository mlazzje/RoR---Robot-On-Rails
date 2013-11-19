package test.ror.core.actions;

import static org.junit.Assert.*;

import org.junit.Test;

import ror.core.Product;
import ror.core.Robot;
import ror.core.actions.InputAction;

public class InputActionTest {

	@Test
	public void test() {
		InputAction testAction = new InputAction(0, null);
		Robot testRobot = new Robot(null);
		Product testProduct = new Product();
		
		// Test de la durée
		assertTrue(testAction.getDuration() == 0);
		assertFalse(testAction.getDuration() == 10);
		
		testAction.setDuration(-10);
		assertFalse(testAction.getDuration() == -10);
		assertTrue(testAction.getDuration() == 0);
		
		testAction.setDuration(10);
		assertTrue(testAction.getDuration() == 10);
		assertFalse(testAction.getDuration() == 0);
		
		testAction.setDuration(null);
		assertTrue(testAction.getDuration() == 0);
		
		// Test du robot
		assertTrue(testAction.getRobot() == null);
		testAction.setRobot(testRobot);
		
		assertTrue(testAction.getRobot() == testRobot);
		
		// Test du product
		assertTrue(testAction.getProduct() == null);
		testAction.setProduct(testProduct);
		
		assertTrue(testAction.getProduct() == testProduct);
	}

}
