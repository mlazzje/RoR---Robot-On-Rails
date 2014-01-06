package test.ror.core.actions;

import static org.junit.Assert.*;

import org.junit.Test;

import ror.core.Column;
import ror.core.Drawer;
import ror.core.Product;
import ror.core.Rail;
import ror.core.Robot;
import ror.core.actions.DestockingAction;

public class DestockingActionTest {

	@Test
	public void test() {
		Product testProduct = new Product("");
		DestockingAction testAction = new DestockingAction(0, null, testProduct);
		Rail testRail = new Rail(null,1,null, null, null, null);
		Robot testRobot = new Robot(testRail,1,null);
		Column testCol = new Column(null, null, null, null, testRail);
		Drawer testDrawer = new Drawer(testCol, null);
		
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
