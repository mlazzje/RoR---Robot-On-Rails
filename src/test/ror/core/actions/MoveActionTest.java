package test.ror.core.actions;

import static org.junit.Assert.*;

import org.junit.Test;

import ror.core.Rail;
import ror.core.Robot;
import ror.core.actions.MoveAction;

public class MoveActionTest {

	@Test
	public void test() {
		MoveAction testAction = new MoveAction(0, null, null, null);
		Robot testRobot = new Robot(null,1,null);
		Rail testRail1 = new Rail(null, null, null, null, null, null);
		Rail testRail2 = new Rail(null, null, null, null, null, null);
		
		// Test de la dur�e
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
		
		// Test du next
		assertTrue(testAction.getNext() == null);
		testAction.setNext(testRail1);
		
		assertTrue(testAction.getNext() == testRail1);

		// Test du previous
		assertTrue(testAction.getPrevious() == null);
		testAction.setPrevious(testRail2);
		
		assertTrue(testAction.getPrevious() == testRail2);
	}

}
