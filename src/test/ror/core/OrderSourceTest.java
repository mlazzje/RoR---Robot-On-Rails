package test.ror.core;

import static org.junit.Assert.*;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ror.core.Order;
import ror.core.OrderSource;
import ror.core.Product;
import sun.org.mozilla.javascript.internal.ast.ForInLoop;

public class OrderSourceTest {

	private OrderSource orderSourceTest;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		orderSourceTest = new OrderSource();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetRandomProducts() {
		//fail("Not yet implemented");
	}

	@Test
	public void testGetRandomOrders() {
		//fail("Not yet implemented");
	}

	@Test
	public void testGetScenarioOrders() {
		//fail("Not yet implemented");
	}

	@Test
	public void testGetScenartioProducts() {
		//fail("Not yet implemented");
	}

	@Test
	public void testGetFile() {
		//fail("Not yet implemented");
	}

	@Test
	public void testSetFile() {
		
		File file = new File("xml/scenario-test.xml");
		System.out.println(file.getAbsolutePath());
		
		orderSourceTest.setFile(file);

		HashMap<Integer, ArrayList<Product>> products = orderSourceTest.getProducts();
		HashMap<Integer,ArrayList<Order>> orders = orderSourceTest.getOrders();

		for(Entry<Integer,  ArrayList<Product>> entry : products.entrySet()) {
		    Integer key = entry.getKey();
		    ArrayList<Product> value = entry.getValue();
		    
		    System.out.println(key.toString()+" : ");
		    for(Product product : value)
		    	System.out.println(product.getName());
		}
		
		
	}

}