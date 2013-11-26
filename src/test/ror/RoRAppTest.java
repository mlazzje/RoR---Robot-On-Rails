package test.ror;

import static org.junit.Assert.*;

import org.junit.Test;

import ror.RoRApp;
import ror.gui.UIController;

public class RoRAppTest {

    @Test
    public void test() {
	RoRApp.main(null);
	System.out.println(RoRApp.getUiController());
	assertTrue(RoRApp.getUiController() instanceof UIController);
    }

}
