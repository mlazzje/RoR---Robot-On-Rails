package test.ror.core.actions;

import static org.junit.Assert.*;

import org.junit.Test;

import ror.core.Robot;
import ror.core.actions.PauseAction;

public class PauseActionTest {

	@Test
	public void test() {
		PauseAction testAction = new PauseAction(0, null, null);
		Robot testRobot = new Robot(null);
		
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
		assertTrue(testAction.getWaitingRobot() == null);
		testAction.setWaitingRobot(testRobot);
		
		assertTrue(testAction.getWaitingRobot() == testRobot);
	}

}
