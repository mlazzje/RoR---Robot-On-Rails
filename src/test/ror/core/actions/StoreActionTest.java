package test.ror.core.actions;

import static org.junit.Assert.*;

import org.junit.Test;

import ror.core.Drawer;
import ror.core.Product;
import ror.core.Robot;
import ror.core.actions.StoreAction;

public class StoreActionTest {

	@Test
	public void test() {
		StoreAction testAction = new StoreAction(0, null, null, null);
		Robot testRobot = new Robot(null,1);
		Drawer testDrawer = new Drawer(null, null);
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
		
		// Test du drawer
		assertTrue(testAction.getDrawer() == null);
		testAction.setDrawer(testDrawer);
		
		assertTrue(testAction.getDrawer() == testDrawer);
		
		// Test du product
		assertTrue(testAction.getProduct() == null);
		testAction.setProduct(testProduct);
		
		assertTrue(testAction.getProduct() == testProduct);
	}

}
