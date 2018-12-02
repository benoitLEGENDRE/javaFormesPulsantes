

public class Carre3 extends Forme{
	Carre3(MachineTrace m, double x, double y) {
		super( m, x, y);
		// TODO Auto-generated constructor stub
	}

	void orienter(int angle){
		m.orienter(angle);
	}

	void dessiner(){
		super.dessiner();
		
		for(int i=0;i<4;i++) {
			m.avancer(d);
			m.tournerGauche(90);
			//System.out.print("trait "+i+" " );
		}
		//m.lever();
		//System.out.println();
		
	}
}
