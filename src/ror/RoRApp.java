package ror;

import ror.gui.*;

public class RoRApp {
    private static UIController uiController;

    public static void main(String[] args) {
	RoRApp.uiController = new UIController();
    }

    public static UIController getUiController() {
	return RoRApp.uiController;
    }
}
