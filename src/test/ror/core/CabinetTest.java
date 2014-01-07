package test.ror.core;

import org.junit.Test;

import ror.core.Cabinet;
import ror.core.Column;

public class CabinetTest {

	@Test
	public void test() {
		
		Cabinet cabinetTest = new Cabinet();
		Column columnTest = new Column(null, 0, 0, null, null);
		Column columnTest2 = new Column(null, 1, 0, null, null);
		
		cabinetTest.addColumn(columnTest);
		cabinetTest.addColumn(columnTest2);
	}

}
