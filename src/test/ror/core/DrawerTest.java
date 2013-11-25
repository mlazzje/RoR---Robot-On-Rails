package test.ror.core;

import static org.junit.Assert.*;

import org.junit.Test;

import ror.core.Cabinet;
import ror.core.Column;
import ror.core.Drawer;
import ror.core.Product;
import ror.core.Rail;

public class DrawerTest {

	@Test
	public void test() {
		Cabinet testDrawerColumnCabinet = new Cabinet();
		Rail testDrawerColumnRail = new Rail(0, 0, null, null, null, null);
		Column testDrawerColumn = new Column(testDrawerColumnCabinet, 0, 0, 0, testDrawerColumnRail);
		Drawer testDrawer = new Drawer(testDrawerColumn, 0);
		
		// Test Column
		assertTrue(testDrawer.getColumn().equals(testDrawerColumn));
		assertFalse(testDrawer.getColumn().getPositionInCabinet()!=testDrawerColumn.getPositionInCabinet());
		
		// Test Column
		Column testDrawerColumn2 = new Column(testDrawerColumnCabinet, 0, 0, 0, testDrawerColumnRail);
		testDrawer.setColumn(testDrawerColumn2);
		assertTrue(testDrawer.getColumn().equals(testDrawerColumn2));
		
		// Test products
		Product testDrawerProduct = new Product("Test");
		testDrawer.setProduct(testDrawerProduct);
		assertTrue(testDrawer.getProduct().equals(testDrawerProduct));
	}

}
