package ror.gui;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import ror.core.Column;
import ror.core.Input;
import ror.core.Output;
import ror.core.Rail;
import ror.core.RoRElement;

public class RoRElementPanel extends JPanel implements MouseListener {

    private RoRElement rorElement;

    public RoRElementPanel(RoRElement element) {
	super();
	Dimension dim = new Dimension(16, 16);
	this.setSize(dim);
	this.setMaximumSize(dim);
	this.setMinimumSize(dim);
	this.setPreferredSize(dim);
	this.addMouseListener(this);

	this.rorElement = element;
	this.setBorder(BorderFactory.createLineBorder(Color.GRAY));
	reColor();
    }

    public void reColor() {
	if (rorElement instanceof Rail) {
	    this.setBackground(Color.blue);
	} else if (rorElement instanceof Output) {
	    this.setBackground(Color.yellow);
	} else if (rorElement instanceof Input) {
	    this.setBackground(Color.green);
	} else if (rorElement instanceof Column) {
	    this.setBackground(Color.red);
	} else {
	    this.setBackground(Color.white);
	}
    }

    @Override
    public void mouseClicked(MouseEvent e) {

	if (this.rorElement instanceof Column) {
	    RoRFrame frame = (RoRFrame) this.getParent().getParent().getParent().getParent().getParent();
	    frame.reColor();
	    Column column = (Column) this.rorElement;
	    Rail accessRail = column.getAccess();
	    JPanel parent = (JPanel) this.getParent();

	    if (accessRail != null) {
		JPanel accessRailPanel = (JPanel) parent.getComponent(accessRail.getX() + (accessRail.getY() * frame.getUiController().getSimulationManager().getMap()[0].length));
		accessRailPanel.setBackground(Color.green);
	    }
	}
	if (this.rorElement instanceof Input) {
	    RoRFrame frame = (RoRFrame) this.getParent().getParent().getParent().getParent().getParent();
	    frame.reColor();
	    Input input = (Input) this.rorElement;
	    Rail accessRail = input.getAccess();
	    JPanel parent = (JPanel) this.getParent();

	    if (accessRail != null) {
		JPanel accessRailPanel = (JPanel) parent.getComponent(accessRail.getX() + (accessRail.getY() * frame.getUiController().getSimulationManager().getMap()[0].length));
		accessRailPanel.setBackground(Color.green);
	    }
	}
	
	if (this.rorElement instanceof Output) {
	    RoRFrame frame = (RoRFrame) this.getParent().getParent().getParent().getParent().getParent();
	    frame.reColor();
	    Output output = (Output) this.rorElement;
	    Rail accessRail = output.getAccess();
	    JPanel parent = (JPanel) this.getParent();

	    if (accessRail != null) {
		JPanel accessRailPanel = (JPanel) parent.getComponent(accessRail.getX() + (accessRail.getY() * frame.getUiController().getSimulationManager().getMap()[0].length));
		accessRailPanel.setBackground(Color.green);
	    }
	}
	
	
	if (this.rorElement instanceof Rail) {

	    RoRFrame frame = (RoRFrame) this.getParent().getParent().getParent().getParent().getParent();

	    frame.reColor();
	    Rail rail = (Rail) this.rorElement;
	    ArrayList<Rail> previousRails = rail.getPreviousRail();
	    Rail rightRail = rail.getRightRail();
	    Rail leftRail = rail.getLeftRail();
	    JPanel parent = (JPanel) this.getParent();

	    // JPanel previousRailPanel = (JPanel)
	    // (parent.getComponentAt(previousRail.getX()*r.width,
	    // previousRail.getY()*r.height));
	    for(Rail pr : previousRails)
	    {
		JPanel previousRailPanel = (JPanel) parent.getComponent(pr.getX() + (pr.getY() * frame.getUiController().getSimulationManager().getMap()[0].length));
		previousRailPanel.setBackground(Color.gray);	    
	    }

	    JPanel rightRailPanel = (JPanel) parent.getComponent(rightRail.getX() + (rightRail.getY() * frame.getUiController().getSimulationManager().getMap()[0].length));

	    if (leftRail != null) {
		JPanel leftRailPanel = (JPanel) parent.getComponent(leftRail.getX() + (leftRail.getY() * frame.getUiController().getSimulationManager().getMap()[0].length));
		leftRailPanel.setBackground(Color.green);
		this.setBackground(new Color(255, 102, 0));
	    }

	    this.setBackground(new Color(255, 102, 0));
	    rightRailPanel.setBackground(new Color(102, 204, 0));
	    System.out.println(rail.getX().toString() + "," + rail.getY().toString());

	}
    }

    @Override
    public void mousePressed(MouseEvent e) {
	// TODO Auto-generated method stub

    }

    @Override
    public void mouseReleased(MouseEvent e) {
	// TODO Auto-generated method stub

    }

    @Override
    public void mouseEntered(MouseEvent e) {
	// TODO Auto-generated method stub

    }

    @Override
    public void mouseExited(MouseEvent e) {
	// TODO Auto-generated method stub

    }
}
