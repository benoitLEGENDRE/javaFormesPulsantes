
public class carresEmboites {
	public static void main(String[] args) {
		MachineTrace m = new MachineTrace(800, 800);
		final int n=20 ;
		final int hauteurFigure =600;
		System.out.println("hauteur= "+hauteurFigure);
		double X=hauteurFigure/2;
		double Y=hauteurFigure/2;
		double taille=hauteurFigure;
		for (int i=0;i<n;i+=2) {
			Carre carre = new Carre(m, -X , - Y);
			carre.fixerTaille(taille);
			carre.orienter(0);
			carre.dessiner();
			taille=taille/2;
			X=X/2;
			Y=Y/2;
		}
		X=hauteurFigure;
		double TailleBiais=hauteurFigure*Math.sqrt(2)/2;
		for (int i=0;i<n;i+=2) {
			X=X/2;
			System.out.println("i= "+i+" X= "+X);
		    Y=0;
		    Carre carre2 = new Carre(m, -X , - Y);
		    System.out.println("i= "+i+" taille= "+ TailleBiais);
			carre2.fixerTaille(TailleBiais);
			carre2.orienter(-45);
			carre2.dessiner();
			TailleBiais= TailleBiais/(2);
		}
	 

	}
}

