
public class Triangle extends Forme{
	Triangle(MachineTrace m, double x, double y){
		super( m, x, y);
	}
	void dessiner(){
		super.dessiner();
		
		for(int i=0;i<3;i++) {
			m.avancer(d);
			m.tournerGauche(120);
			//System.out.print("trait "+i+" " );
		}
		//m.lever();
		//System.out.println();
		
	}
}
