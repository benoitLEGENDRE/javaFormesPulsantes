import java.util.Random;

public class FormesPulsantes {
    final static int nbFormes = 5;
    final static int nbObjets = 40;
    final static int etapesPulsations = 10;
    final static int amplitudePulsation = 20;
    final static int delai = 100;
    
    static Forme creerForme(int type, MachineTrace m, double x, double y) {
      //choix forme
    	switch (type) {
            case 0 :
                return new Carre3(m, x, y);
            case 1 :
                return new Triangle(m, x, y);
            case 2:
                return new Cercle(m, x, y);
            case 3:
                return new Hexagone(m, x, y);
            case 4:
                return new Octogone(m, x, y);
            default :
                throw new RuntimeException("Forme Inconnue");
        }
    }
    
    public static void main(String [] args) {
    		final int largeur = 600;
    		final int hauteur = 600;
        MachineTrace m;
        Forme [] f;
        int [] tailles;
        Random r;
        
        m = new MachineTrace(largeur, hauteur);
        m.rafraichissementAutomatique(false);
        m.masquerPointeur();
        f = new Forme[nbObjets];
        tailles = new int[f.length];
        r = new Random();
        for (int i=0; i<f.length; i++) {
        		int x = r.nextInt(largeur)-largeur/2;
        		int y = r.nextInt(hauteur)-hauteur/2;
        		
            f[i] = creerForme(r.nextInt(nbFormes), m, x, y);
            tailles[i] = r.nextInt(25)+5;
        }
        
        while (true) {
            for (int j=0; j<etapesPulsations; j++) {
                m.effacerTout();
                int ajout = (int) (amplitudePulsation*Math.sin(j*Math.PI/etapesPulsations));
                for (int i=0; i<f.length; i++) {
                    f[i].fixerTaille(tailles[i]+ajout);
                    f[i].tournerGauche(2);
                    f[i].dessiner();
                }
                m.rafraichir();
                m.attendre(delai);                
            }
        }
    }
}
