
public class Cercle extends Forme{
	Cercle(MachineTrace m, double x, double y){
		super(m,x,y);
	}
	void dessiner(){
		super.dessiner();
		
		for(int i=0;i<360;i++) {
			m.avancer(d/(2*20*3.14));
			m.tournerGauche(1);
			
		}
		
		
	}
}
