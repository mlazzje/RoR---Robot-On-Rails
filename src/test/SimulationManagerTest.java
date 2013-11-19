package test;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ror.core.SimulationManager;
import junit.framework.TestCase;

public class SimulationManagerTest extends TestCase {

	private SimulationManager simulationManager;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	protected void setUp() throws Exception {
		super.setUp();
		simulationManager = new SimulationManager();
	}

	@After
	protected void tearDown() throws Exception {
		super.tearDown();
		simulationManager = null;
	}

	@Test
	public void testGetUptime() {
		simulationManager.getUptime();
		if (simulationManager.getUptime() == null)
			fail();
	}

	@Test
	public void testRun() {
		simulationManager.setSpeed((float) 1);
		
		Thread t = new Thread(new Runnable() {
	         public void run()
	         {
	        	 simulationManager.run();
	         }
		});
		t.start();
		try {
			Thread.sleep(3000); // sleep 3 secondes
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		simulationManager.setPause();
		try {
			Thread.sleep(3000); // sleep 3 secondes
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		simulationManager.setPlay();
		try {
			Thread.sleep(3000); // sleep 3 secondes
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		simulationManager.setStop();
	}

	@Test
	public void testGetMap() {
		if(simulationManager.getMap()==null)
			fail();
	}

	@Test
	public void testUpdateIndicators() {
		simulationManager.updateIndicators();
	}

	@Test
	public void testSetStop() {
		simulationManager.setStop();
	}

	@Test
	public void testSetPause() {
		simulationManager.setPause();
	}

	@Test
	public void testSetPlay() {
		simulationManager.setPlay();
	}

	@Test
	public void testUpdate() {
		simulationManager.update(null, null);
	}

	@Test
	public void testSetiAlgStore() {
		simulationManager.setiAlgStore(null);
	}

	@Test
	public void testSetiAlgMove() {
		simulationManager.setiAlgMove(null);
	}

	@Test
	public void testSetiAlgDestocking() {
		simulationManager.setiAlgDestocking(null);
	}

	@Test
	public void testSetRandomMode() {
		simulationManager.setRandomMode();
	}

	@Test
	public void testSetFile() {
		simulationManager.setFile(null);
	}

	@Test
	public void testSetSpeed() {
		simulationManager.setSpeed(null);
	}

	@Test
	public void testSetNbRobot() {
		simulationManager.setNbRobot(null);
	}

	@Test
	public void testSetEndSimulation() {
		simulationManager.setEndSimulation();
	}

	@Test
	public void testGetRobots() {
		if(simulationManager.getRobots()==null)
			fail();
	}

}
