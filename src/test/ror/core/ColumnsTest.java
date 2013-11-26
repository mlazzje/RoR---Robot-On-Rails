package test.ror.core;

import static org.junit.Assert.*;

import org.junit.Test;

import ror.core.Cabinet;
import ror.core.Column;
import ror.core.Rail;

public class ColumnsTest {

	@Test
	public void test() {
		Cabinet testColumnCabinet = new Cabinet();
		Cabinet testColumnCabinet2 = new Cabinet();
		Rail testColumnRail = new Rail(0, 0, null, null, null, null);
		Rail testColumnRail2 = new Rail(1, 1, null, null, null, null);
		int positionInCabinet = 1;
		int x=0;
		int y=0;
		Column columnTest = new Column(testColumnCabinet, 0, 0, positionInCabinet, testColumnRail);
		Column columnTestNull = new Column(null, 0, 0, 0, null);
		
		// Test getters
		assertTrue(columnTest.getPositionInCabinet()==positionInCabinet);
		assertFalse(columnTest.getPositionInCabinet()!=positionInCabinet);
		
		assertTrue(columnTest.getAccess().equals(testColumnRail));
		assertFalse(columnTest.getAccess().equals(testColumnRail2));
		
		assertTrue(columnTest.getCabinet().equals(testColumnCabinet));
		assertFalse(columnTest.getCabinet().equals(testColumnCabinet2));
		
		// Test setters
		columnTestNull.setAccess(testColumnRail);
		assertTrue(columnTest.getAccess().equals(testColumnRail));
		assertFalse(columnTest.getAccess().equals(testColumnRail2));
		
		columnTestNull.setCabinet(testColumnCabinet);
		assertTrue(columnTest.getCabinet().equals(testColumnCabinet));
		assertFalse(columnTest.getCabinet().equals(testColumnCabinet2));
		
		columnTestNull.setPositionInCabinet(positionInCabinet);
		assertTrue(columnTest.getPositionInCabinet()==positionInCabinet);
		assertFalse(columnTest.getPositionInCabinet()!=positionInCabinet);
	}

}
