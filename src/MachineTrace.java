
/* MachineTrace - Encore une nouvelle version (� but p�dagogique) de la c�l�bre tortue LOGO
 * Copyright (C) 2018 Guillaume Huard
 * 
 * Ce programme est libre, vous pouvez le redistribuer et/ou le
 * modifier selon les termes de la Licence Publique G�n�rale GNU publi�e par la
 * Free Software Foundation (version 2 ou bien toute autre version ult�rieure
 * choisie par vous).
 * 
 * Ce programme est distribu� car potentiellement utile, mais SANS
 * AUCUNE GARANTIE, ni explicite ni implicite, y compris les garanties de
 * commercialisation ou d'adaptation dans un but sp�cifique. Reportez-vous � la
 * Licence Publique G�n�rale GNU pour plus de d�tails.
 * 
 * Vous devez avoir re�u une copie de la Licence Publique G�n�rale
 * GNU en m�me temps que ce programme ; si ce n'est pas le cas, �crivez � la Free
 * Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307,
 * �tats-Unis.
 * 
 * Contact:
 *          Guillaume.Huard@imag.fr
 *          Laboratoire LIG
 *          700 avenue centrale
 *          Domaine universitaire
 *          38401 Saint Martin d'H�res
 */

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * Classe permettant d'ouvrir une fen�tre graphique et de dessiner dans cette
 * fen�tre de mani�re simple. Le dessin se fait � l'aide d'une plume orient�e
 * qu'il est possible de lever/baisser et de d�placer. La fen�tre repr�sente une
 * portion du plan dans laquelle :
 * <ul>
 * <li>(0,0) est au centre</li>
 * <li>l'orientation de la plume est un angle en degr�s avec 0� � droite, 90� en
 * haut et 180� � gauche</li>
 * </ul>
 * La plume d�marre :
 * <ul>
 * <li>en position (0,0), c'est � dire au centre de la fen�tre</li>
 * <li>avec l'angle 90, c'est � dire orient�e vers le haut</li>
 * </ul>
 * Tout d�placement avec la plume baiss�e dessine dans la fen�tre.
 * 
 * @author Guillaume Huard
 * @version 29/06/2018
 */
public class MachineTrace {

	class Etat {
		final Color[] colors = { Color.BLACK, Color.WHITE, Color.YELLOW, Color.RED, Color.GREEN, Color.BLUE };
		java.util.List<Command> commands;
		double angle;
		Point2D position;
		Dimension d;
		volatile boolean enBas;
		Color color;
		volatile int colorIndex;
		volatile boolean pointeur;

		Etat(int l, int h) {
			commands = new java.util.ArrayList<Command>();
			angle = 0;
			position = new Point2D.Double();
			d = new Dimension(l, h);
			enBas = false;
			colorIndex = MachineTrace.NOIR;
			color = colors[colorIndex];
			pointeur = true;
		}

		synchronized Etat recuperer() {
			Etat resultat = new Etat(d.width, d.height);
			java.util.List<Command> tmp = resultat.commands;
			resultat.commands = commands;
			commands = tmp;
			resultat.angle = angle;
			resultat.position.setLocation(position.getX(), position.getY());
			resultat.enBas = enBas;
			resultat.colorIndex = colorIndex;
			resultat.color = color;
			resultat.pointeur = pointeur;
			return resultat;
		}

		synchronized void avancer(double L) {
			double x = position.getX();
			double y = position.getY();

			x += L * Math.cos(angle);
			y += L * Math.sin(angle);
			placer(x, y);
		}

		synchronized void tourner(double a) {
			angle += toRadians(a);
		}

		synchronized void orienter(double a) {
			angle = toRadians(a);
		}

		protected double toRadians(double a) {
			return a * 2 * Math.PI / 360.0;
		}

		protected int convert(double d) {
			return (int) Math.round(d);
		}

		protected int convertX(double x) {
			return convert(x) + d.width / 2;
		}

		protected int convertY(double y) {
			return -convert(y) + d.height / 2;
		}

		synchronized void placer(double x, double y) {
			if (enBas) {
				int x1 = convertX(position.getX());
				int y1 = convertY(position.getY());
				int x2 = convertX(x);
				int y2 = convertY(y);
				Line l = new Line(x1, y1, x2, y2, color);
				commands.add(l);
			}
			position.setLocation(x, y);
		}

		synchronized void changeCouleur(int couleur) {
			color = colors[couleur];
			colorIndex = couleur;
		}

		synchronized void effacerTout() {
			commands.add(new Erase());
		}

		void dessinePointeur(int[] pointeurX, int[] pointeurY) {
			double taille = 5;
			double x = position.getX();
			double y = position.getY();

			pointeurX[0] = convertX(x);
			pointeurY[0] = convertY(y);
			pointeurX[1] = convertX(x + taille * Math.cos(angle + Math.PI * 3 / 4));
			pointeurY[1] = convertY(y + taille * Math.sin(angle + Math.PI * 3 / 4));
			pointeurX[2] = convertX(x + 2 * taille * Math.cos(angle));
			pointeurY[2] = convertY(y + 2 * taille * Math.sin(angle));
			pointeurX[3] = convertX(x + taille * Math.cos(angle - Math.PI * 3 / 4));
			pointeurY[3] = convertY(y + taille * Math.sin(angle - Math.PI * 3 / 4));
		}
	}

	class SwingComponent extends JComponent implements Runnable {
		private static final long serialVersionUID = 5663965186068436350L;
		Etat etatGlobal;
		int width, height;
		BufferedImage image;
		Graphics2D dessin;
		int[] pointeurX, pointeurY;

		SwingComponent(Etat e) {
			etatGlobal = e;
			image = null;
			pointeurX = new int[4];
			pointeurY = new int[4];
		}

		public void paintComponent(Graphics g) {
			Graphics2D drawable = (Graphics2D) g;

			width = getSize().width;
			height = getSize().height;

			if (image == null) {
				image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
				dessin = image.createGraphics();
				dessin.setBackground(Color.WHITE);
				dessin.clearRect(0, 0, width, height);
			}

			Etat etat = etatGlobal.recuperer();
			// Image en cours
			Iterator<Command> it = etat.commands.iterator();
			while (it.hasNext()) {
				Command c = it.next();
				if (c != null) {
					c.draw(dessin, width, height);
				}
			}
			drawable.drawImage(image, 0, 0, null);

			// Pointeur
			if (etat.pointeur) {
				etat.dessinePointeur(pointeurX, pointeurY);
				if (etat.enBas) {
					drawable.setPaint(etat.color);
				} else {
					drawable.setPaint(Color.WHITE);
				}
				drawable.fillPolygon(pointeurX, pointeurY, 4);
				drawable.setPaint(Color.BLACK);
				drawable.drawPolygon(pointeurX, pointeurY, 4);
			}
		}

		@Override
		public void run() {
			repaint();
		}
	}

	abstract class Command {
		abstract void draw(Graphics2D drawable, int width, int height);
	}

	class Line extends Command {

		int x1, y1, x2, y2;
		Color col;

		Line(int a, int b, int c, int d, Color co) {
			x1 = a;
			y1 = b;
			x2 = c;
			y2 = d;
			col = co;
		}

		@Override
		void draw(Graphics2D drawable, int width, int height) {
			drawable.setPaint(col);
			drawable.drawLine(x1, y1, x2, y2);
		}
	}

	class Erase extends Command {

		@Override
		void draw(Graphics2D drawable, int width, int height) {
			drawable.setBackground(Color.WHITE);
			drawable.clearRect(0, 0, width, height);
		}
	}

	final static int NOIR = 0;
	final static int BLANC = 1;
	final static int JAUNE = 2;
	final static int ROUGE = 3;
	final static int VERT = 4;
	final static int BLEU = 5;

	Runnable refresh;
	Etat etat;
	JFrame frame;
	boolean autoRefresh;
	int autoWait;

	/**
	 * Permet d'attendre.
	 * 
	 * @param ms
	 *            Le nombre de millisecondes � attendre
	 */
	public void attendre(int ms) {
		if (ms > 0) {
			try {
				Thread.sleep(ms);
			} catch (Exception e) {
			}
			;
		}
	}

	/**
	 * Ajoute une attente automatique apr�s chaque commande.
	 * 
	 * @param ms
	 *            Le nombre de millisecondes � attendre
	 */
	public void attenteAutomatique(int ms) {
		autoWait = ms;
	}

	/**
	 * Active ou desactive le rafraichissement automatique
	 * 
	 * @param r
	 *            true pour activer, false pour desactiver
	 */
	public void rafraichissementAutomatique(boolean r) {
		autoRefresh = r;
	}

	protected void commit() {
		if (etat.pointeur || etat.enBas) {
			if (autoRefresh) {
				rafraichir();
			}
			attendre(autoWait);
		}
	}

	/**
	 * L�ve la plume.
	 */
	public void lever() {
		etat.enBas = false;
		commit();
	}

	/**
	 * Baisse la plume.
	 */
	public void baisser() {
		etat.enBas = true;
		commit();
	}

	/**
	 * Fait avancer la plume.
	 * 
	 * @param L
	 *            Distance � parcourir (en pixels).
	 */
	public void avancer(double L) {
		etat.avancer(L);
		commit();
	}

	/**
	 * Fait reculer la plume.
	 * 
	 * @param L
	 *            Distance � parcourir (en pixels).
	 */
	public void reculer(double L) {
		etat.avancer(-L);
		commit();
	}

	/**
	 * Tourne la plume vers la gauche.
	 * 
	 * @param a
	 *            L'angle de rotation en degr�s.
	 */
	public void tournerGauche(double a) {
		etat.tourner(a);
		commit();
	}

	/**
	 * Tourne la plume vers la droite.
	 * 
	 * @param a
	 *            L'angle de rotation en degr�s.
	 */
	public void tournerDroite(double a) {
		etat.tourner(-a);
		commit();
	}

	/**
	 * Oriente la plume de mani�re absolue.
	 * 
	 * @param a
	 *            Nouvel angle de la plume
	 */
	public void orienter(double a) {
		etat.orienter(a);
		commit();
	}

	/**
	 * Place la plume au point donn�. Attention, ceci est un d�placement : si la
	 * plume est baiss�e elle dessinera jusqu'au point destination.
	 * 
	 * @param x
	 *            Abscisse du point destination
	 * @param y
	 *            Ordonn�e du point destination
	 */
	public void placer(double x, double y) {
		etat.placer(x, y);
		commit();
	}

	/**
	 * Change la couleur de la plume. La couleur est un entier entre 0 et 5 ou peut
	 * �tre choisie parmi :
	 * <ul>
	 * <li>MachineTrace.NOIR</li>
	 * <li>MachineTrace.BLANC</li>
	 * <li>MachineTrace.JAUNE</li>
	 * <li>MachineTrace.ROUGE</li>
	 * <li>MachineTrace.VERT</li>
	 * <li>MachineTrace.BLEU</li>
	 * </ul>
	 * 
	 * @param couleur
	 *            La nouvelle couleur
	 */
	public void changeCouleur(int couleur) {
		etat.changeCouleur(couleur);
		commit();
	}

	/**
	 * Renvoie la couleur courante de la plume.
	 * 
	 * @return couleur courante de la plume.
	 */
	public int couleurCourante() {
		return etat.colorIndex;
	}

	/**
	 * Masque le pointeur qui repr�sente la plume.
	 */
	public void masquerPointeur() {
		etat.pointeur = false;
		commit();
	}

	/**
	 * Montre le pointeur qui repr�sente la plume.
	 */
	public void montrerPointeur() {
		etat.pointeur = true;
		commit();
	}

	/**
	 * Efface le contenu de la fen�tre.
	 */
	public void effacerTout() {
		etat.effacerTout();
		commit();
	}

	/**
	 * Met � jour l'affichage (inutile sauf si le rafra�chissement automatique est
	 * desactiv�).
	 */
	public void rafraichir() {
		refresh.run();
	}

	/**
	 * Cr�e une nouvelle fen�tre de dessin.
	 * 
	 * @param l
	 *            Largeur de la fen�tre
	 * @param h
	 *            Hauteur de la fen�tre
	 */
	public MachineTrace(final int l, final int h) {
		autoRefresh = true;
		autoWait = 0;
		etat = creerEtat(l, h);
		final SwingComponent sc = new SwingComponent(etat);
		final MachineTrace mt = this;
		refresh = sc;
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				mt.frame = new JFrame("Machine trace");
				sc.setPreferredSize(new Dimension(l, h));
				mt.frame.add(sc);
				mt.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				mt.frame.pack();
				mt.frame.setVisible(true);
			}
		});
	}

	protected Etat creerEtat(int l, int h) {
		return new Etat(l, h);
	}

	/**
	 * Ferme la fen�tre de dessin.
	 */
	public void fermer() {
		final MachineTrace mt = this;
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				mt.frame.setVisible(false);
				mt.frame.dispose();
			}
		});
	}
}