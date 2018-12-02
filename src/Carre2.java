

public class Carre2 {
	MachineTrace m;double posX;double posY; double d;
	//constructeur
	Carre2(MachineTrace m,double X,double Y){
		this.m=m;
		posX=X;
		posY=Y;		
	}
	
	void fixerTaille(double e){
		
		d=e;
				
	}
	
	void dessiner(){
		int l=4;
		m.placer(posX,posY);
		m.baisser();
		for(int i=0;i<4;i++) {
			m.avancer(d);
			m.tournerGauche(90);
			//System.out.print("trait "+i+" " );
		}
		m.lever();
		//System.out.println();
		
	}
	void orienter(int angle){
		m.orienter(angle);
	}
	
}
