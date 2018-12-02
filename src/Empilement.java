class Empilement {
	public static void main(String[] args) {
		MachineTrace m = new MachineTrace(400, 400);
		final int l = 5, d = 20;
		final int hauteurFigure = 2 * (l - 1) * d;

		for (int i = 0; i < l; i++)
			for (int j = 0; j < l-i; j++) {
				Carre carre = new Carre(m, j * 2 * d - hauteurFigure / 2, i * 2 * d - hauteurFigure / 2);
				carre.fixerTaille(d);
				carre.dessiner();
			}
	}
}
