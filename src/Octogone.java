
public class Octogone extends Forme{
	Octogone(MachineTrace m, double X, double Y){
		 super(m,X,Y);
	 }
	 void dessiner(){
			super.dessiner();
			
			for(int i=0;i<8;i++) {
				m.avancer(d);
				m.tournerGauche(45);
				//System.out.print("trait "+i+" " );
			}
			//m.lever();
			//System.out.println();
			
		}
}
