class carres {
	public static void main(String[] args) {
		MachineTrace m = new MachineTrace(400, 400);
		final int l = 3, c = 5, d = 30;
		// On calcule largeur et hauteur en tenant compte des espaces et du fait que le
		// carre est centré sur sa position
		final int largeurFigure = 2 * (c - 1) * d;//2*(3-1)*30=120
		final int hauteurFigure = 2 * (l - 1) * d;//2*(5-1)*30=240

		for (int i = 0; i < l; i++)
			for (int j = 0; j < c; j++) {
				//j * 2 * d - largeurFigure / 2=-120;-60;0;i * 2 * d - hauteurFigure / 2=-240;-180
				Carre2 carre = new Carre2(m, j * 2 * d - largeurFigure / 2, i * 2 * d - hauteurFigure / 2);
				carre.fixerTaille(d);
				carre.dessiner();
			}
	}
}
