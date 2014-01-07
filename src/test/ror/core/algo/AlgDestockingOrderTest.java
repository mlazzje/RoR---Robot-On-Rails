package test.ror.core.algo;

import java.util.ArrayList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ror.core.algo.AlgDestockingFifo;
import ror.core.algo.AlgDestockingOrder;

public class AlgDestockingOrderTest {

    AlgDestockingOrder algDestockingOrder ;
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
	algDestockingOrder = new AlgDestockingOrder();
	ArrayList<String> stocks = new ArrayList<String>();
	stocks.add("a");
	stocks.add("a");
	stocks.add("b");
	stocks.add("c");
	
	
	ArrayList<String> test = new ArrayList<String>();
	test.add("a");
	test.add("b");
	
	ArrayList<String> test2 = new ArrayList<String>();
	test2.add("a");
	test2.add("a");
	
	ArrayList<String> test3 = new ArrayList<String>();
	test3.add("a");
	test3.add("f");
	System.out.println(stocks);
	System.out.println(test2);
	System.out.println(AlgDestockingFifo.containsAllWithDoublon(stocks, test2));
	stocks.remove("a");
	System.out.println(stocks);

	System.out.println(AlgDestockingFifo.containsAllWithDoublon(stocks, test2));
	
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testGetActions() {
	//fail("Not yet implemented");
    }

}
