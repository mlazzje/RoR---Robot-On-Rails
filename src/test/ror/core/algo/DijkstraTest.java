package test.ror.core.algo;

import java.util.ArrayList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ror.core.Rail;
import ror.core.algo.Dijkstra;

public class DijkstraTest {

    private Dijkstra dijkstra;
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testDijkstra() {
	ArrayList<Rail> rails = new ArrayList<Rail>();
	Rail r3 = new Rail(0, 2, null, null, null, null);
	Rail r2 = new Rail(0, 1, null, r3, null, null);
	Rail r = new Rail(0, 0, null, r2, null, null);
	rails.add(r);
	rails.add(r2);
	rails.add(r3);

	dijkstra = new Dijkstra(rails);
	System.out.println("res : "+dijkstra.getPath(r, r3));
	
	
	ArrayList<Rail> rrails = new ArrayList<Rail>();
	Rail rr3 = new Rail(0, 2, null, null, null, null);
	Rail rr2 = new Rail(0, 1, null, null, null, null);
	Rail rr = new Rail(0, 0, null, rr2, null, null);
	rrails.add(rr);
	rrails.add(rr2);
	rrails.add(rr3);
	
	dijkstra = new Dijkstra(rrails);
	
	ArrayList<Rail> path = (ArrayList<Rail>) dijkstra.getPath(rr, rr2);
	if(path==null)
	{
	    System.out.println("impossible d'accéder à la destination");
	}
	else
	    System.out.println(path);

	
	
    }

    @Test
    public void testComputePaths() {
	//fail("Not yet implemented");
    }

    @Test
    public void testGetShortestPathTo() {
	//fail("Not yet implemented");
    }

    @Test
    public void testGetPath() {
	//fail("Not yet implemented");
    }

}
