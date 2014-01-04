package test.ror.core;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ror.core.SimulationManager;
import ror.core.algo.AlgDestockingFifo;
import ror.core.algo.AlgDestockingOrder;
import ror.core.algo.AlgMoveAuto;
import ror.core.algo.AlgMoveEco;
import ror.core.algo.AlgMoveFast;
import ror.core.algo.AlgStoreFifo;
import ror.core.algo.AlgStoreOrder;
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
	assertTrue(simulationManager.getUptime() >= 0);
    }

    @Test
    public void testRun() {
	simulationManager.setSpeed((float) 1);

	System.out.println("Run simulation manager");
	Thread t = new Thread(new Runnable() {
	    public void run() {
		simulationManager.run();
	    }
	});

	System.out.println("Going to start");
	t.start();

	System.out.println("Sleep for a while");
	try {
	    Thread.sleep(3000); // sleep 3 secondes
	} catch (InterruptedException e) {
	    e.printStackTrace();
	}

	System.out.println("Going to pause");
	simulationManager.setPause();
	try {
	    Thread.sleep(3000); // sleep 3 secondes
	} catch (InterruptedException e) {
	    e.printStackTrace();
	}

	System.out.println("Start again");
	simulationManager.setPlay();

	System.out.println("Sleep again");
	try {
	    Thread.sleep(3000); // sleep 3 secondes
	} catch (InterruptedException e) {
	    e.printStackTrace();
	}

	System.out.println("Finally stop");
	simulationManager.setStop();
    }

    @Test
    public void testGetMap() {
	assertTrue(simulationManager.getMap() != null);
    }


    @Test
    public void testSetStop() {
	simulationManager.setStop();
	assertTrue(simulationManager.getStatus() == 0);
    }

    @Test
    public void testSetPause() {
	simulationManager.setPause();
	assertTrue(simulationManager.getStatus() == 2);
	// TODO tester l'état du simulationManager avec la méthode getState de
	// la classe Thread apres avoir attendu 1 seconde
    }

    @Test
    public void testSetPlay() {
	simulationManager.setPlay();
	assertTrue(simulationManager.getStatus() == 1);
	// TODO tester l'état du simulationManager avec la méthode getState de
	// la classe Thread apres avoir attendu 1 seconde
    }

    @Test
    public void testUpdate() {
	simulationManager.update(null, null);
    }

    @Test
    public void testSetiAlgStore() {
	AlgStoreFifo algoFifo = new AlgStoreFifo();
	AlgStoreOrder algoOrder = new AlgStoreOrder();

	simulationManager.setiAlgStore(algoFifo);
	assertTrue(simulationManager.getiAlgStore() instanceof AlgStoreFifo);

	simulationManager.setiAlgStore(algoOrder);
	assertTrue(simulationManager.getiAlgStore() instanceof AlgStoreOrder);
    }

    @Test
    public void testSetiAlgMove() {
	AlgMoveAuto algoAuto = new AlgMoveAuto();
	AlgMoveEco algoEco = new AlgMoveEco();
	AlgMoveFast algoFast = new AlgMoveFast();

	simulationManager.setiAlgMove(algoAuto);
	assertTrue(simulationManager.getiAlgMove() instanceof AlgMoveAuto);

	simulationManager.setiAlgMove(algoEco);
	assertTrue(simulationManager.getiAlgMove() instanceof AlgMoveEco);

	simulationManager.setiAlgMove(algoFast);
	assertTrue(simulationManager.getiAlgMove() instanceof AlgMoveFast);
    }

    @Test
    public void testSetiAlgDestocking() {
	AlgDestockingFifo algoFifo = new AlgDestockingFifo();
	AlgDestockingOrder algoOrder = new AlgDestockingOrder();

	simulationManager.setiAlgDestocking(algoFifo);
	assertTrue(simulationManager.getiAlgDestocking() instanceof AlgDestockingFifo);

	simulationManager.setiAlgDestocking(algoOrder);
	assertTrue(simulationManager.getiAlgDestocking() instanceof AlgDestockingOrder);
    }

    @Test
    public void testSetRandomMode() {
	//simulationManager.setRandomMode();
	assertFalse(simulationManager.getSource());
    }

    @Test
    public void testSetFile() {
	// TODO tester avec un fichier ordersource
	simulationManager.setFile(null);
    }

    @Test
    public void testSetSpeed() {
	simulationManager.setSpeed(1.0f);
	assertTrue(simulationManager.getSpeed() == 1.0f);

	simulationManager.setSpeed(10000.0f);
	assertTrue(simulationManager.getSpeed() == 10000.0f);

	simulationManager.setSpeed(-1.0f);
	assertTrue(simulationManager.getSpeed() == 0.0f);
    }

    @Test
    public void testSetNbRobot() {
	simulationManager.setNbRobot(0);
	assertTrue(simulationManager.getNbRobot() == 0);

	simulationManager.setNbRobot(3);
	assertTrue(simulationManager.getNbRobot() == 3);

	simulationManager.setNbRobot(-1);
	assertTrue(simulationManager.getNbRobot() == 0);
    }

    @Test
    public void testSetEndSimulation() {
	simulationManager.setEndSimulation();
	// TODO tester la fin de la simulation, si en mode aléatoire il n'y a de fin que quand l'utilisateur clique sur stop, sinon la fin de la simulation en mode importation de scenario est quand toutes les commandes immportées ont été expédiées
    }

    @Test
    public void testGetRobots() {
	assertTrue(simulationManager.getRobots().size() >= 0);
    }

}
