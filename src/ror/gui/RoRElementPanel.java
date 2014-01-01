package ror.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;

import javax.imageio.ImageIO;
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

/**
 * @author RoR
 * 
 */
public class RoRElementPanel extends JLabel implements MouseListener, Observer {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * size
	 */
	private static int size = 32;
	/**
	 * RoRElement
	 */
	private RoRElement rorElement;
	/**
	 * railEmpty
	 */
	private static ImageIcon railEmpty;
	/**
	 * railDB
	 */
	private static ImageIcon railDB;
	/**
	 * railGB
	 */
	private static ImageIcon railGB;
	/**
	 * railDH
	 */
	private static ImageIcon railDH;
	/**
	 * railGH
	 */
	private static ImageIcon railGH;
	/**
	 * railDGB
	 */
	private static ImageIcon railDGB;
	/**
	 * railDGH
	 */
	private static ImageIcon railDGH;
	/**
	 * railDHB
	 */
	private static ImageIcon railDHB;
	/**
	 * railGHB
	 */
	private static ImageIcon railGHB;
	/**
	 * railH
	 */
	private static ImageIcon railH;
	/**
	 * railV
	 */
	private static ImageIcon railV;
	/**
	 * column
	 */
	private static ImageIcon[] column;
	/**
	 * output spot
	 */
	private static ImageIcon output;
	/**
	 * input spot
	 */
	private static ImageIcon input;
	/**
	 * outputFill
	 */
	private static ImageIcon outputFill;
	/**
	 * inputFill
	 */
	private static ImageIcon inputFill;
	/**
	 * robot
	 */
	private static ImageIcon robot;
	/**
	 * roboti
	 */
	private static ImageIcon roboti;

	static {
		try {
			RoRElementPanel.railEmpty = new ImageIcon((Image) RoRElementPanel.scaleImage(ImageIO.read(RoRElementPanel.class.getResource("/ressources/empty.png")), size));
			RoRElementPanel.railDB = new ImageIcon((Image) RoRElementPanel.scaleImage(ImageIO.read(RoRElementPanel.class.getResource("/ressources/rail-db.png")), size));
			RoRElementPanel.railGB = new ImageIcon((Image) RoRElementPanel.scaleImage(ImageIO.read(RoRElementPanel.class.getResource("/ressources/rail-gb.png")), size));
			RoRElementPanel.railDH = new ImageIcon((Image) RoRElementPanel.scaleImage(ImageIO.read(RoRElementPanel.class.getResource("/ressources/rail-dh.png")), size));
			RoRElementPanel.railGH = new ImageIcon((Image) RoRElementPanel.scaleImage(ImageIO.read(RoRElementPanel.class.getResource("/ressources/rail-gh.png")), size));
			RoRElementPanel.railDGB = new ImageIcon((Image) RoRElementPanel.scaleImage(ImageIO.read(RoRElementPanel.class.getResource("/ressources/rail-dgb.png")), size));
			RoRElementPanel.railDGH = new ImageIcon((Image) RoRElementPanel.scaleImage(ImageIO.read(RoRElementPanel.class.getResource("/ressources/rail-dgh.png")), size));
			RoRElementPanel.railDHB = new ImageIcon((Image) RoRElementPanel.scaleImage(ImageIO.read(RoRElementPanel.class.getResource("/ressources/rail-dhb.png")), size));
			RoRElementPanel.railGHB = new ImageIcon((Image) RoRElementPanel.scaleImage(ImageIO.read(RoRElementPanel.class.getResource("/ressources/rail-ghb.png")), size));
			RoRElementPanel.railH = new ImageIcon((Image) RoRElementPanel.scaleImage(ImageIO.read(RoRElementPanel.class.getResource("/ressources/rail-h.png")), size));
			RoRElementPanel.railV = new ImageIcon((Image) RoRElementPanel.scaleImage(ImageIO.read(RoRElementPanel.class.getResource("/ressources/rail-v.png")), size));
			RoRElementPanel.column = new ImageIcon[] { new ImageIcon((Image) RoRElementPanel.scaleImage(ImageIO.read(RoRElementPanel.class.getResource("/ressources/column-0.png")), size)), new ImageIcon((Image) RoRElementPanel.scaleImage(ImageIO.read(RoRElementPanel.class.getResource("/ressources/column-1.png")), size)), new ImageIcon((Image) RoRElementPanel.scaleImage(ImageIO.read(RoRElementPanel.class.getResource("/ressources/column-2.png")), size)), new ImageIcon((Image) RoRElementPanel.scaleImage(ImageIO.read(RoRElementPanel.class.getResource("/ressources/column-3.png")), size)), new ImageIcon((Image) RoRElementPanel.scaleImage(ImageIO.read(RoRElementPanel.class.getResource("/ressources/column-4.png")), size)), new ImageIcon((Image) RoRElementPanel.scaleImage(ImageIO.read(RoRElementPanel.class.getResource("/ressources/column-5.png")), size)), new ImageIcon((Image) RoRElementPanel.scaleImage(ImageIO.read(RoRElementPanel.class.getResource("/ressources/column-6.png")), size)), new ImageIcon((Image) RoRElementPanel.scaleImage(ImageIO.read(RoRElementPanel.class.getResource("/ressources/column-7.png")), size)), new ImageIcon((Image) RoRElementPanel.scaleImage(ImageIO.read(RoRElementPanel.class.getResource("/ressources/column-8.png")), size)), new ImageIcon((Image) RoRElementPanel.scaleImage(ImageIO.read(RoRElementPanel.class.getResource("/ressources/column-9.png")), size)), new ImageIcon((Image) RoRElementPanel.scaleImage(ImageIO.read(RoRElementPanel.class.getResource("/ressources/column-10.png")), size)) };
			RoRElementPanel.output = new ImageIcon((Image) RoRElementPanel.scaleImage(ImageIO.read(RoRElementPanel.class.getResource("/ressources/output.png")), size));
			RoRElementPanel.input = new ImageIcon((Image) RoRElementPanel.scaleImage(ImageIO.read(RoRElementPanel.class.getResource("/ressources/input.png")), size));
			RoRElementPanel.outputFill = new ImageIcon((Image) RoRElementPanel.scaleImage(ImageIO.read(RoRElementPanel.class.getResource("/ressources/output-fill.png")), size));
			RoRElementPanel.inputFill = new ImageIcon((Image) RoRElementPanel.scaleImage(ImageIO.read(RoRElementPanel.class.getResource("/ressources/input-fill.png")), size));
			RoRElementPanel.robot = new ImageIcon((Image) RoRElementPanel.scaleImage(ImageIO.read(RoRElementPanel.class.getResource("/ressources/robot.png")), size / 2));
			RoRElementPanel.roboti = new ImageIcon((Image) RoRElementPanel.scaleImage(ImageIO.read(RoRElementPanel.class.getResource("/ressources/robot-i.png")), size / 2));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Contructor RoRElementPanel
	 * 
	 * @param element
	 */
	public RoRElementPanel(RoRElement element) {
		super();
		Dimension dim = new Dimension(RoRElementPanel.size, RoRElementPanel.size);
		this.setSize(dim);
		this.setPreferredSize(dim);
		this.addMouseListener(this);
		this.setBorder(null);
		if (element != null) {
			this.rorElement = element;
			this.rorElement.addObserver(this);
		} else {
			this.setIcon(RoRElementPanel.railEmpty);
		}
		update(this.rorElement, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	public void update(Observable rorElement, Object o) {
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
			for (Drawer drawer : column.getDrawerList()) {
				if (drawer.getProduct() != null) {
					cpt++;
				}
			}
			this.setBackground(Color.blue);
			this.setIcon(RoRElementPanel.column[cpt]);
		} else {
			this.setIcon(RoRElementPanel.railEmpty);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
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
					JLabel title = new JLabel("Détails Robot #" + robot.getNumber());
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
						synchronized (robot.getProducts()) {
							Iterator<Product> it = robot.getProducts().iterator();
							while (it.hasNext()) {
								Product next = it.next();
								JLabel label = new JLabel(next.getName());
								label.setVerticalAlignment(JLabel.CENTER);
								label.setHorizontalAlignment(JLabel.CENTER);
								frame.getInformationsPanel().add(label);
							}
						}
					}
					frame.pack();
					frame.repaint();
					robot.addObserver(frame.getUiController());
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
					int cpt = 0;
					while (it.hasNext() && cpt < 11) {
						Product next = it.next();
						JLabel label = new JLabel(next.getName());
						label.setVerticalAlignment(JLabel.CENTER);
						label.setHorizontalAlignment(JLabel.CENTER);
						frame.getInformationsPanel().add(label);
						cpt++;
					}
					if (cpt >= 11) {
						JLabel label = new JLabel("+ " + ((Input) this.rorElement).getProductList().size());
						label.setVerticalAlignment(JLabel.CENTER);
						label.setHorizontalAlignment(JLabel.CENTER);
						frame.getInformationsPanel().add(label);
					}
				}
				frame.setCheckedElement(this.rorElement);
				frame.pack();
				frame.repaint();
				this.rorElement.addObserver(frame.getUiController());
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
					Iterator<Product> it = ((Output) this.rorElement).getProductList().iterator();
				    int cpt = 0;
					while (it.hasNext() && cpt < 11) {
						Product next = it.next();
						JLabel label = new JLabel(next.getName());
						label.setVerticalAlignment(JLabel.CENTER);
						label.setHorizontalAlignment(JLabel.CENTER);
						frame.getInformationsPanel().add(label);
						cpt++;
					}
					if(cpt >= 11) {
						JLabel label = new JLabel("+ "+((Output) this.rorElement).getProductList().size());
						label.setVerticalAlignment(JLabel.CENTER);
						label.setHorizontalAlignment(JLabel.CENTER);
						frame.getInformationsPanel().add(label);
					}
				}
				frame.setCheckedElement(this.rorElement);
				frame.pack();
				frame.repaint();
				this.rorElement.addObserver(frame.getUiController());
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
				frame.setCheckedElement(this.rorElement);
				frame.pack();
				frame.repaint();
				this.rorElement.addObserver(frame.getUiController());
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	@Override
	public void mousePressed(MouseEvent e) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseReleased(MouseEvent e) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseEntered(MouseEvent e) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseExited(MouseEvent e) {
	}

	/**
	 * @return rorElement
	 */
	public RoRElement getRorElement() {
		return rorElement;
	}

	/**
	 * @param rorElement
	 */
	public void setRorElement(RoRElement rorElement) {
		this.rorElement = rorElement;
	}

	/**
	 * @param image
	 * @param size
	 * @return BufferedImage
	 */
	public static BufferedImage scaleImage(BufferedImage image, int size) {
		BufferedImage bi = new BufferedImage(size, size, BufferedImage.TRANSLUCENT);
		Graphics2D g2d = (Graphics2D) bi.createGraphics();
		g2d.addRenderingHints(new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY));
		g2d.drawImage(image, 0, 0, size, size, null);
		g2d.dispose();
		return bi;
	}

}
