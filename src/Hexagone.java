
public class Hexagone extends Forme{
 Hexagone(MachineTrace m, double X, double Y){
	 super(m,X,Y);
 }
 void dessiner(){
		super.dessiner();
		
		for(int i=0;i<6;i++) {
			m.avancer(d);
			m.tournerGauche(60);
			//System.out.print("trait "+i+" " );
		}
		//m.lever();
		//System.out.println();
		
	}
}
