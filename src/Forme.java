

public class Forme {
	
	MachineTrace m;	double posX,  posY,d;
	//Constructor
	Forme (MachineTrace m,double x,double  y ){
		this.m=m;
		posX=x;
		posY=y;
	}
	
	
	
	void dessiner(){
		m.lever();
		m.placer(posX,posY);
		m.baisser();
	}
	//appel tourner gauche
	void tournerGauche(double angle) {
		m.tournerGauche(angle);
	}

	void fixerTaille(double e){
		d=e;				
	}
}
