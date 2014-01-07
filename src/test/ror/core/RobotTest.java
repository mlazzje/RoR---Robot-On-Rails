package test.ror.core;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ror.core.Product;
import ror.core.Rail;
import ror.core.Robot;
import ror.core.actions.InputAction;
import ror.core.actions.MoveAction;

public class RobotTest {

    private Robot robot;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
	robot = new Robot(null, null, null);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testRobot() {
	robot = new Robot(null, 4, null);
    }

    @Test
    public void testGetNumber() {
	Robot r = new Robot(null, 4, null);
	assertTrue(r.getNumber() ==4);
    }

    @Test
    public void testSetNumber() {
	robot.setNumber(1);
	assertTrue(robot.getNumber() == 1);
    }

    @Test
    public void testGetProducts() {
	assertTrue(robot.getProducts().size() == 0);
	Product p = new Product("a");
	Product p1 = new Product("b");

	robot.getProducts().add(p);
	robot.getProducts().add(p1);
    }

    @Test
    public void testGetTraveledDistance() {
	assertTrue(robot.getTraveledDistance() >= 0);
    }

    @Test
    public void testGetConsumption() {
	assertTrue(robot.getConsumption() >= 0);
    }

    @Test
    public void testGetActions() {
	assertTrue(robot.getActions() != null);
    }

    @Test
    public void testGetRail() {
	Rail r = new Rail(0, 0, null, null, null, null);
	robot = new Robot(r, 1, null);
	assertTrue(robot.getRail() == r);
    }

    @Test
    public void testGetLastActionSpaceAvailability() {
	robot = new Robot(null, null, null);
	assertTrue(robot.getLastActionSpaceAvailabilityUnsynchronized() == 10);
	robot.getActions().add(new InputAction(null, null, null));
	assertTrue(robot.getLastActionSpaceAvailabilityUnsynchronized() == 9);
    }

    @Test
    public void testGetSpeed() {
	robot.setSpeed(Robot.SPEED_1);
	assertTrue(robot.getSpeed() == Robot.SPEED_1);
	robot.setSpeed(Robot.SPEED_2);
	assertTrue(robot.getSpeed() == Robot.SPEED_2);
    }

    @Test
    public void testSetSpeed() {
	robot.setSpeed(Robot.SPEED_1);
	assertTrue(robot.getSpeed() == Robot.SPEED_1);
	robot.setSpeed(Robot.SPEED_2);
	assertTrue(robot.getSpeed() == Robot.SPEED_2);
    }

    @Test
    public void testWillMove() {
	assertTrue(robot.willMove() == false);
	MoveAction move = new MoveAction(100, null, null, null);
	robot.getActions().add(move);
	assertTrue(robot.willMove() == true);
    }

    @Test
    public void testRun() {
	// fail("Not yet implemented");
    }

    @Test
    public void testStopTimerTask() {
	robot.stopTimerTask();
    }

}
