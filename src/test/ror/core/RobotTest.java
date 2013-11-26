package test.ror.core;

import static org.junit.Assert.*;

import org.junit.Test;

import ror.core.Rail;
import ror.core.Robot;

public class RobotTest {

    @Test
    public void test() {
	Rail railTest = new Rail(null, null, null, null, null, null);
	Robot robotTest = new Robot(railTest);
	
	assertTrue(robotTest.getRail().equals(railTest));
	
	fail("Not finished yet");
    }

}
