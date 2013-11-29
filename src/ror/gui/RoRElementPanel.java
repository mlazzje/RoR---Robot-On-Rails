package ror.gui;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Iterator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.UIManager;

import ror.core.Column;
import ror.core.Drawer;
import ror.core.Input;
import ror.core.Output;
import ror.core.Product;
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
	    if (((Rail) this.rorElement).getRobot() != null) {
		this.add(new RobotLabel());
	    }
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

	if (this.getParent().getParent().getParent().getParent().getParent() instanceof RoRFrame) {
	    RoRFrame frame = (RoRFrame) this.getParent().getParent().getParent().getParent().getParent();
	    Font bFont = new Font(UIManager.getDefaults().getFont("TabbedPane.font").getFontName(), Font.BOLD, UIManager.getDefaults().getFont("TabbedPane.font").getSize());
	    Font h1Font = new Font(UIManager.getDefaults().getFont("TabbedPane.font").getFontName(), Font.BOLD, UIManager.getDefaults().getFont("TabbedPane.font").getSize() + 2);

	    // Clic sur un rail
	    if (this.rorElement instanceof Rail) {

		frame.reColor();
		Rail rail = (Rail) this.rorElement;
		Rail previousRail = rail.getPrevioustRail();
		Rail rightRail = rail.getRightRail();
		Rail leftRail = rail.getLeftRail();
		JPanel parent = (JPanel) this.getParent();

		// JPanel previousRailPanel = (JPanel)
		// (parent.getComponentAt(previousRail.getX()*r.width,
		// previousRail.getY()*r.height));
		if (previousRail != null) {
		    JPanel previousRailPanel = (JPanel) parent.getComponent(previousRail.getX() + (previousRail.getY() * frame.getUiController().getSimulationManager().getMap()[0].length));
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
		System.out.println(previousRail.getX().toString() + "," + previousRail.getY().toString());
	    }
	    // Clic sur un Input : Affichage de l'inventaire de l'Input
	    else if (this.rorElement instanceof Input) {
		frame.getInformationsPanel().removeAll();
		frame.getInformationsPanel().setLayout(new GridLayout(11, 1));
		JLabel title = new JLabel("Détails point d'entrée");
		title.setFont(h1Font);
		title.setVerticalAlignment(JLabel.CENTER);
		title.setHorizontalAlignment(JLabel.CENTER);

		frame.getInformationsPanel().setLayout(new FlowLayout());
		frame.getInformationsPanel().add(title);
		// Gestion du cas où aucun produit n'est disponible
		if (((Input) this.rorElement).getProductList().isEmpty()) {
		    JLabel label = new JLabel("Aucun produit n'est présent");
		    frame.getInformationsPanel().add(label);
		} else {
		    Iterator<Product> it = ((Input) this.rorElement).getProductList().iterator();
		    while (it.hasNext()) {
			Product next = it.next();
			frame.getInformationsPanel().add(new JLabel(next.getName()));
		    }
		}
		frame.pack();
		frame.repaint();
	    } else if (this.rorElement instanceof Output) {
		frame.getInformationsPanel().removeAll();
		frame.getInformationsPanel().setLayout(new GridLayout(11, 1));
		JLabel title = new JLabel("Détails point de sortie");
		title.setFont(h1Font);
		title.setVerticalAlignment(JLabel.CENTER);
		title.setHorizontalAlignment(JLabel.CENTER);

		frame.getInformationsPanel().setLayout(new FlowLayout());
		frame.getInformationsPanel().add(title);
		// Gestion du cas où aucun produit n'est disponible
		if (((Output) this.rorElement).getProductList().isEmpty()) {
		    JLabel label = new JLabel("Aucun produit n'est présent");
		    frame.getInformationsPanel().add(label);
		} else {
		    Iterator<Product> it = ((Input) this.rorElement).getProductList().iterator();
		    while (it.hasNext()) {
			Product next = it.next();
			frame.getInformationsPanel().add(new JLabel(next.getName()));
		    }
		}
		frame.pack();
		frame.repaint();
	    } else if (this.rorElement instanceof Column) {
		frame.getInformationsPanel().removeAll();
		frame.getInformationsPanel().setLayout(new FlowLayout());
		JLabel title = new JLabel("Détails armoire");
		title.setFont(h1Font);
		title.setVerticalAlignment(JLabel.CENTER);
		title.setHorizontalAlignment(JLabel.CENTER);

		frame.getInformationsPanel().setLayout(new GridLayout(11, 1));
		frame.getInformationsPanel().add(title);
		if (((Column) this.rorElement).getDrawerList().isEmpty()) {
		    JLabel label = new JLabel("Aucun produit n'est présent");
		    frame.getInformationsPanel().add(label);
		} else {
		    Iterator<Drawer> it = ((Column) this.rorElement).getDrawerList().iterator();
		    Integer cpt = 0;
		    while (it.hasNext()) {
			Drawer next = it.next();
			if (next.getProduct() == null) {
			    JLabel label = new JLabel("Rang #" + cpt + " : Vide");
			    label.setVerticalAlignment(JLabel.CENTER);
			    label.setHorizontalAlignment(JLabel.CENTER);
			    frame.getInformationsPanel().add(label);
			} else {
			    JLabel label = new JLabel("Rang #" + cpt + " : " + next.getProduct().getName());
			    label.setVerticalAlignment(JLabel.CENTER);
			    label.setHorizontalAlignment(JLabel.CENTER);
			    frame.getInformationsPanel().add(label);
			}
			cpt++;
		    }
		}
		frame.pack();
		frame.repaint();
	    }
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
