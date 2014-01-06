package test.ror.core.actions;

import static org.junit.Assert.*;

import org.junit.Test;

import ror.core.Product;
import ror.core.Rail;
import ror.core.Robot;
import ror.core.actions.OutputAction;

public class OutputActionTest {

	@Test
	public void test() {
		OutputAction testAction = new OutputAction(0, null, null);
		Rail testRail = new Rail(null,1,null, null, null, null);
		Robot testRobot = new Robot(testRail, 1,null);
		Product testProduct = new Product(null);
		
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
