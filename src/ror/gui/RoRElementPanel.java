package ror.gui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Iterator;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.UIManager;

import ror.core.Column;
import ror.core.Drawer;
import ror.core.Input;
import ror.core.Output;
import ror.core.Product;
import ror.core.Rail;
import ror.core.RoRElement;
import ror.core.Robot;

public class RoRElementPanel extends JLabel implements MouseListener {

    private static final long serialVersionUID = 1L;
    private RoRElement rorElement;
    private static ImageIcon railEmpty = new ImageIcon(new ImageIcon(RoRElementPanel.class.getResource("/ressources/empty.png")).getImage().getScaledInstance(32, 32, Image.SCALE_DEFAULT));
    private static ImageIcon railDB = new ImageIcon(new ImageIcon(RoRElementPanel.class.getResource("/ressources/rail-db.png")).getImage().getScaledInstance(32, 32, Image.SCALE_DEFAULT));
    private static ImageIcon railGB = new ImageIcon(new ImageIcon(RoRElementPanel.class.getResource("/ressources/rail-gb.png")).getImage().getScaledInstance(32, 32, Image.SCALE_DEFAULT));
    private static ImageIcon railDH = new ImageIcon(new ImageIcon(RoRElementPanel.class.getResource("/ressources/rail-dh.png")).getImage().getScaledInstance(32, 32, Image.SCALE_DEFAULT));
    private static ImageIcon railGH = new ImageIcon(new ImageIcon(RoRElementPanel.class.getResource("/ressources/rail-gh.png")).getImage().getScaledInstance(32, 32, Image.SCALE_DEFAULT));
    private static ImageIcon railDGB = new ImageIcon(new ImageIcon(RoRElementPanel.class.getResource("/ressources/rail-dgb.png")).getImage().getScaledInstance(32, 32, Image.SCALE_DEFAULT));
    private static ImageIcon railDGH = new ImageIcon(new ImageIcon(RoRElementPanel.class.getResource("/ressources/rail-dgh.png")).getImage().getScaledInstance(32, 32, Image.SCALE_DEFAULT));
    private static ImageIcon railDHB = new ImageIcon(new ImageIcon(RoRElementPanel.class.getResource("/ressources/rail-dhb.png")).getImage().getScaledInstance(32, 32, Image.SCALE_DEFAULT));
    private static ImageIcon railGHB = new ImageIcon(new ImageIcon(RoRElementPanel.class.getResource("/ressources/rail-ghb.png")).getImage().getScaledInstance(32, 32, Image.SCALE_DEFAULT));
    private static ImageIcon railH = new ImageIcon(new ImageIcon(RoRElementPanel.class.getResource("/ressources/rail-h.png")).getImage().getScaledInstance(32, 32, Image.SCALE_DEFAULT));
    private static ImageIcon railV = new ImageIcon(new ImageIcon(RoRElementPanel.class.getResource("/ressources/rail-v.png")).getImage().getScaledInstance(32, 32, Image.SCALE_DEFAULT));
    private static ImageIcon[] column = { new ImageIcon(new ImageIcon(RoRElementPanel.class.getResource("/ressources/column-0.png")).getImage().getScaledInstance(32, 32, Image.SCALE_DEFAULT)), new ImageIcon(new ImageIcon(RoRElementPanel.class.getResource("/ressources/column-1.png")).getImage().getScaledInstance(32, 32, Image.SCALE_DEFAULT)), new ImageIcon(new ImageIcon(RoRElementPanel.class.getResource("/ressources/column-2.png")).getImage().getScaledInstance(32, 32, Image.SCALE_DEFAULT)), new ImageIcon(new ImageIcon(RoRElementPanel.class.getResource("/ressources/column-3.png")).getImage().getScaledInstance(32, 32, Image.SCALE_DEFAULT)), new ImageIcon(new ImageIcon(RoRElementPanel.class.getResource("/ressources/column-4.png")).getImage().getScaledInstance(32, 32, Image.SCALE_DEFAULT)), new ImageIcon(new ImageIcon(RoRElementPanel.class.getResource("/ressources/column-5.png")).getImage().getScaledInstance(32, 32, Image.SCALE_DEFAULT)), new ImageIcon(new ImageIcon(RoRElementPanel.class.getResource("/ressources/column-6.png")).getImage().getScaledInstance(32, 32, Image.SCALE_DEFAULT)), new ImageIcon(new ImageIcon(RoRElementPanel.class.getResource("/ressources/column-7.png")).getImage().getScaledInstance(32, 32, Image.SCALE_DEFAULT)), new ImageIcon(new ImageIcon(RoRElementPanel.class.getResource("/ressources/column-8.png")).getImage().getScaledInstance(32, 32, Image.SCALE_DEFAULT)), new ImageIcon(new ImageIcon(RoRElementPanel.class.getResource("/ressources/column-9.png")).getImage().getScaledInstance(32, 32, Image.SCALE_DEFAULT)), new ImageIcon(new ImageIcon(RoRElementPanel.class.getResource("/ressources/column-10.png")).getImage().getScaledInstance(32, 32, Image.SCALE_DEFAULT)) };
    private static ImageIcon output = new ImageIcon(new ImageIcon(RoRElementPanel.class.getResource("/ressources/output.png")).getImage().getScaledInstance(32, 32, Image.SCALE_DEFAULT));
    private static ImageIcon input = new ImageIcon(new ImageIcon(RoRElementPanel.class.getResource("/ressources/input.png")).getImage().getScaledInstance(32, 32, Image.SCALE_DEFAULT));
    private static ImageIcon outputFill = new ImageIcon(new ImageIcon(RoRElementPanel.class.getResource("/ressources/output-fill.png")).getImage().getScaledInstance(32, 32, Image.SCALE_DEFAULT));
    private static ImageIcon inputFill = new ImageIcon(new ImageIcon(RoRElementPanel.class.getResource("/ressources/input-fill.png")).getImage().getScaledInstance(32, 32, Image.SCALE_DEFAULT));
    private static ImageIcon robot = new ImageIcon(new ImageIcon(RoRElementPanel.class.getResource("/ressources/robot.png")).getImage().getScaledInstance(16, 16, Image.SCALE_DEFAULT));
    private static ImageIcon roboti = new ImageIcon(new ImageIcon(RoRElementPanel.class.getResource("/ressources/robot-i.png")).getImage().getScaledInstance(16, 16, Image.SCALE_DEFAULT));

    public RoRElementPanel(RoRElement element) {
	super();
	Dimension dim = new Dimension(30, 30);
	this.setSize(dim);
	this.setMaximumSize(dim);
	this.setMinimumSize(dim);
	this.setPreferredSize(dim);
	this.addMouseListener(this);

	this.rorElement = element;
	// this.setBorder(BorderFactory.createLineBorder(Color.GRAY));
	reColor();
    }

    public void reColor() {
	if (rorElement instanceof Rail) {
	    Rail rail = (Rail) rorElement;
	    // Rail Simple
	    if (rail.getPreviousRail().size() == 1 && rail.getLeftRail() == null) {

		// Horizontal
		if (rail.getRightRail().getY() == rail.getPreviousRail().get(0).getY() && rail.getRightRail().getX() != rail.getPreviousRail().get(0).getX()) {
		    this.setIcon(RoRElementPanel.railH);
		}
		// Vertical
		else if (rail.getRightRail().getY() != rail.getPreviousRail().get(0).getY() && rail.getRightRail().getX() == rail.getPreviousRail().get(0).getX()) {
		    this.setIcon(RoRElementPanel.railV);
		}
		// Virages Simples
		else if (rail.getRightRail().getY() > rail.getPreviousRail().get(0).getY() && rail.getRightRail().getX() > rail.getPreviousRail().get(0).getX()) {
		    this.setIcon(RoRElementPanel.railDH);
		} else if (rail.getRightRail().getY() < rail.getPreviousRail().get(0).getY() && rail.getRightRail().getX() > rail.getPreviousRail().get(0).getX()) {
		    this.setIcon(RoRElementPanel.railGH);
		} else if (rail.getRightRail().getY() > rail.getPreviousRail().get(0).getY() && rail.getRightRail().getX() < rail.getPreviousRail().get(0).getX()) {
		    this.setIcon(RoRElementPanel.railDB);
		} else if (rail.getRightRail().getY() < rail.getPreviousRail().get(0).getY() && rail.getRightRail().getX() < rail.getPreviousRail().get(0).getX()) {
		    this.setIcon(RoRElementPanel.railGB);
		}
	    }
	    // Après Aiguillage
	    else if (rail.getPreviousRail().size() > 1) {
		if (rail.getPreviousRail().get(0).getX() > rail.getPreviousRail().get(1).getX() && rail.getPreviousRail().get(0).getY() > rail.getPreviousRail().get(1).getY()) {
		    this.setIcon(RoRElementPanel.railDHB);
		} else if (rail.getPreviousRail().get(0).getX() < rail.getPreviousRail().get(1).getX() && rail.getPreviousRail().get(0).getY() > rail.getPreviousRail().get(1).getY()) {
		    this.setIcon(RoRElementPanel.railDGH);
		} else if (rail.getPreviousRail().get(0).getX() > rail.getPreviousRail().get(1).getX() && rail.getPreviousRail().get(0).getY() < rail.getPreviousRail().get(1).getY()) {
		    this.setIcon(RoRElementPanel.railDGB);
		} else if (rail.getPreviousRail().get(0).getX() > rail.getPreviousRail().get(1).getX() && rail.getPreviousRail().get(0).getY() < rail.getPreviousRail().get(1).getY()) {
		    this.setIcon(RoRElementPanel.railGHB);
		}
	    }
	    // Avant Aiguillage
	    else if (rail.getLeftRail() != null) {
		if (rail.getRightRail().getX() > rail.getLeftRail().getX() && rail.getRightRail().getY() < rail.getLeftRail().getY()) {
		    this.setIcon(RoRElementPanel.railGHB);
		} else if (rail.getRightRail().getX() < rail.getLeftRail().getX() && rail.getRightRail().getY() < rail.getLeftRail().getY()) {
		    this.setIcon(RoRElementPanel.railDGB);
		} else if (rail.getRightRail().getX() < rail.getLeftRail().getX() && rail.getRightRail().getY() > rail.getLeftRail().getY()) {
		    this.setIcon(RoRElementPanel.railDHB);
		} else if (rail.getRightRail().getX() > rail.getLeftRail().getX() && rail.getRightRail().getY() > rail.getLeftRail().getY()) {
		    this.setIcon(RoRElementPanel.railDGH);
		}
	    }
	    JLabel robotLabel = new JLabel();
	    robotLabel.setSize(new Dimension(16, 16));
	    if (((Rail) this.getRorElement()).getRobot() != null) {
		if (rail.getRightRail().getX() != rail.getPreviousRail().get(0).getX()) {
		    robotLabel.setIcon(robot);
		} else {
		    robotLabel.setIcon(roboti);
		}
		this.add(robotLabel);
		this.repaint();
	    } else if (this.getComponentCount() > 0) // on supprime le robot sur les rails precedents
	    {
		this.removeAll();
		this.repaint();
	    }

	} else if (rorElement instanceof Output) {
	    Output output = (Output) rorElement;
	    if (output.getProductList().isEmpty()) {
		this.setIcon(RoRElementPanel.output);
	    } else {
		this.setIcon(RoRElementPanel.outputFill);
	    }
	} else if (rorElement instanceof Input) {
	    Input input = (Input) rorElement;
	    if (input.getProductList().isEmpty()) {
		this.setIcon(RoRElementPanel.input);
	    } else {
		this.setIcon(RoRElementPanel.inputFill);
	    }
	} else if (rorElement instanceof Column) {
	    Column column = (Column) rorElement;
	    int cpt = 0;
	    for(Drawer drawer : column.getDrawerList()) {
		if(drawer.getProduct() != null) {
		    cpt++;
		}
	    }
	    this.setIcon(RoRElementPanel.column[cpt]);
	} else {
	    this.setIcon(RoRElementPanel.railEmpty);
	}
    }

    @Override
    public void mouseClicked(MouseEvent e) {

	if (this.getParent().getParent().getParent().getParent().getParent() instanceof RoRFrame) {
	    RoRFrame frame = (RoRFrame) this.getParent().getParent().getParent().getParent().getParent();
	    Font h1Font = new Font(UIManager.getDefaults().getFont("TabbedPane.font").getFontName(), Font.BOLD, UIManager.getDefaults().getFont("TabbedPane.font").getSize() + 2);

	    // Clic sur un rail
	    if (this.rorElement instanceof Rail) {
		Rail rail = (Rail) this.rorElement;
		if (rail.getRobot() != null) {
		    frame.setCheckedElement(rail.getRobot());
		    Robot robot = rail.getRobot();

		    frame.getInformationsPanel().removeAll();
		    frame.getInformationsPanel().setLayout(new GridLayout(13, 1));
		    JLabel title = new JLabel("Détails Robot");
		    title.setFont(h1Font);
		    title.setVerticalAlignment(JLabel.CENTER);
		    title.setHorizontalAlignment(JLabel.CENTER);
		    frame.getInformationsPanel().add(title);
		    // Gestion du cas où aucun produit n'est disponible
		    if (robot.getProducts().isEmpty()) {
			JLabel label = new JLabel("Aucun produit n'est présent");
			label.setVerticalAlignment(JLabel.CENTER);
			label.setHorizontalAlignment(JLabel.CENTER);
			frame.getInformationsPanel().add(label);
		    } else {
			Iterator<Product> it = robot.getProducts().iterator();
			while (it.hasNext()) {
			    Product next = it.next();
			    JLabel label = new JLabel(next.getName());
			    label.setVerticalAlignment(JLabel.CENTER);
			    label.setHorizontalAlignment(JLabel.CENTER);
			    frame.getInformationsPanel().add(label);
			}
		    }
		    frame.pack();
		    frame.repaint();
		}
	    }
	    // Clic sur un Input : Affichage de l'inventaire de l'Input
	    else if (this.rorElement instanceof Input) {
		frame.getInformationsPanel().removeAll();
		frame.getInformationsPanel().setLayout(new GridLayout(13, 1));
		JLabel title = new JLabel("Détails point d'entrée");
		title.setFont(h1Font);
		title.setVerticalAlignment(JLabel.CENTER);
		title.setHorizontalAlignment(JLabel.CENTER);
		frame.getInformationsPanel().add(title);
		// Gestion du cas où aucun produit n'est disponible
		if (((Input) this.rorElement).getProductList().isEmpty()) {
		    JLabel label = new JLabel("Aucun produit n'est présent");
		    label.setVerticalAlignment(JLabel.CENTER);
		    label.setHorizontalAlignment(JLabel.CENTER);
		    frame.getInformationsPanel().add(label);
		} else {
		    Iterator<Product> it = ((Input) this.rorElement).getProductList().iterator();
		    while (it.hasNext()) {
			Product next = it.next();
			JLabel label = new JLabel(next.getName());
			label.setVerticalAlignment(JLabel.CENTER);
			label.setHorizontalAlignment(JLabel.CENTER);
			frame.getInformationsPanel().add(label);
		    }
		}
		frame.setCheckedElement(this);
		frame.pack();
		frame.repaint();
	    } else if (this.rorElement instanceof Output) {
		frame.getInformationsPanel().removeAll();
		frame.getInformationsPanel().setLayout(new GridLayout(13, 1));
		JLabel title = new JLabel("Détails point de sortie");
		title.setFont(h1Font);
		title.setVerticalAlignment(JLabel.CENTER);
		title.setHorizontalAlignment(JLabel.CENTER);
		frame.getInformationsPanel().add(title);
		// Gestion du cas où aucun produit n'est disponible
		if (((Output) this.rorElement).getProductList().isEmpty()) {
		    JLabel label = new JLabel("Aucun produit n'est présent");
		    label.setVerticalAlignment(JLabel.CENTER);
		    label.setHorizontalAlignment(JLabel.CENTER);
		    frame.getInformationsPanel().add(label);
		} else {
		    Iterator<Product> it = ((Input) this.rorElement).getProductList().iterator();
		    while (it.hasNext()) {
			Product next = it.next();
			JLabel label = new JLabel(next.getName());
			label.setVerticalAlignment(JLabel.CENTER);
			label.setHorizontalAlignment(JLabel.CENTER);
			frame.getInformationsPanel().add(label);
		    }
		}
		frame.setCheckedElement(this);
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
		    label.setVerticalAlignment(JLabel.CENTER);
		    label.setHorizontalAlignment(JLabel.CENTER);
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
		frame.setCheckedElement(this);
		frame.pack();
		frame.repaint();
	    }
	}
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    public RoRElement getRorElement() {
	return rorElement;
    }

    public void setRorElement(RoRElement rorElement) {
	this.rorElement = rorElement;
    }

}
