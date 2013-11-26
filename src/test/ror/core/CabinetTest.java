package test.ror.core;

import static org.junit.Assert.*;

import org.junit.Test;

import ror.core.Cabinet;
import ror.core.Column;

public class CabinetTest {

    @Test
    public void test() {

	Cabinet cabinetTest = new Cabinet();
	Column columnTest = new Column(null, 0, 0, null, null);
	Column columnTest2 = new Column(null, 1, 0, null, null);

	assertTrue(cabinetTest.getColumn().isEmpty());
	cabinetTest.addColumn(columnTest);
	assertTrue(cabinetTest.getColumn().contains(columnTest));
	cabinetTest.addColumn(columnTest2);
	assertTrue(cabinetTest.getColumn().contains(columnTest));
	assertTrue(cabinetTest.getColumn().contains(columnTest2));
    }

}
