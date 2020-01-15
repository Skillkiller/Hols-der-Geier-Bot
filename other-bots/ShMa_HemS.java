

import java.util.ArrayList;

/**
 * Beschreiben Sie hier die Klasse Geier.
 * 
 * @author (ShMa&HemS)
 * @version (13.01.11)
 */
public class ShMa_HemS extends HolsDerGeierSpieler {
	/**
	 * /** Hier definieren Sie den Konstruktor fuer Objekte Ihrer Klasse (falls
	 * Sie einen eigenen brauchen) Geier
	 */
	private ArrayList<Integer> nochZuGewinnen = new ArrayList<Integer>();
	private ArrayList<Integer> vomGegnerNochNichtGelegt = new ArrayList<Integer>();
	private ArrayList<Integer> nochNichtGespielt = new ArrayList<Integer>();
	private ArrayList<Integer> grosseKarten = new ArrayList<Integer>();
	private ArrayList<Integer> mittlereKarten = new ArrayList<Integer>();
	private ArrayList<Integer> kleineKarten = new ArrayList<Integer>();

	public void reset() {
		nochZuGewinnen.clear();
		nochNichtGespielt.clear();
		grosseKarten.clear();
		mittlereKarten.clear();
		kleineKarten.clear();
		vomGegnerNochNichtGelegt.clear();
		for (int i = 1; i <= 15; i++)
			nochZuGewinnen.add(i);
		for (int i = 1; i <= 15; i++)
			vomGegnerNochNichtGelegt.add(i);
		for (int i = 1; i <= 15; i++)
			nochNichtGespielt.add(i);
		for (int i = 11; i <= 15; i++)
			grosseKarten.add(i);
		for (int i = 6; i <= 10; i++)
			mittlereKarten.add(i);
		for (int i = 1; i <= 5; i++)
			kleineKarten.add(i);
	}

	// kleinste Karte aus unserem Deck auswählen
	public int passivePlayer(ArrayList<Integer> aktuellerBlock) {

		for (int y : aktuellerBlock) {
			if (y != 0) {
				return y;
			}
		}
		return 0;
	}

	// größte Karte aus unserem Deck auswählen
	public int activePlayer(ArrayList<Integer> aktuellerBlock) {

		for (int i = 15; i >= 1; i--) {

			if (aktuellerBlock.contains(i)) {

				return i;
			}
		}
		return 0;

	}

	// Karte durch 0 in den Arraylisten ersetzen
	public int austauschen(int karte) {
		if (grosseKarten.contains(karte)) {
			grosseKarten.set(grosseKarten.indexOf(karte), 0);
		}
		if (mittlereKarten.contains(karte)) {
			mittlereKarten.set(mittlereKarten.indexOf(karte), 0);
		}
		if (kleineKarten.contains(karte)) {
			kleineKarten.set(kleineKarten.indexOf(karte), 0);
		}

		nochNichtGespielt.set(nochNichtGespielt.indexOf(karte), 0);
		nochZuGewinnen.set(nochZuGewinnen.indexOf(karte), 0);
		return karte;
	}

	// Karten auswählen
	public int waehleKarte(int naechsteKarte) {

		int sumKleineKarten = 0;
		int sumMittlereKarten = 0;
		int sumGrosseKarten = 0;
		// Nachfolgend wird überprüft ob in den drei Pools noch Karten vorhanden
		// sind
		for (int z : kleineKarten) {
			sumKleineKarten = sumKleineKarten + z;
		}
		for (int z : mittlereKarten) {
			sumMittlereKarten = sumMittlereKarten + z;
		}
		for (int z : grosseKarten) {
			sumGrosseKarten = sumGrosseKarten + z;
		}
		int sumGegner = 0;
		for (int z : vomGegnerNochNichtGelegt) {
			sumGegner = sumGegner + z;
		}
		// grosser Pool
		if (naechsteKarte >= 7 || naechsteKarte == -5) {

			int zähler = 1;
			int max = grosseKarten.get(grosseKarten.size() - 1);

			int maxGegner = vomGegnerNochNichtGelegt
					.get(vomGegnerNochNichtGelegt.size() - 1);
			while (max == 0) {
				max = grosseKarten.get(grosseKarten.size() - zähler);
				zähler++;
				if (zähler > 5) {
					if (sumKleineKarten != 0) {
						return austauschen(passivePlayer(kleineKarten));
					} else {
						if (sumMittlereKarten != 0) {
							return austauschen(passivePlayer(mittlereKarten));
						}
						return austauschen(passivePlayer(grosseKarten));
					}
				}
			}
			while (maxGegner == 0) {
				maxGegner = vomGegnerNochNichtGelegt
						.get(vomGegnerNochNichtGelegt.size() - zähler);
				zähler++;
				if (zähler > 15) {
					maxGegner = sumGegner;

				}
			}
			for (int i = 15; i >= 11; i--) {
				//System.out.println("Max großer pool wir" + max);
				//System.out.println("MaxGegner großer pool wir" + maxGegner);
				if (maxGegner == max) {

					return austauschen(max);
				}
				if (maxGegner > max) {
					if (sumKleineKarten != 0) {
						return austauschen(passivePlayer(kleineKarten));
					} else {
						if (sumMittlereKarten != 0) {
							return austauschen(passivePlayer(mittlereKarten));
						}
						return austauschen(passivePlayer(grosseKarten));
					}

				}
				if (maxGegner < max) {

					// Differenz der abgefragten Karte mit der höchsten Karte
					// des Gegners
					int z = max - maxGegner;
					for (int c = 1; c <= z; c++) {
						if (nochNichtGespielt.contains(maxGegner + c)) {
							return austauschen(maxGegner + c);
						}

					}
				}
			}
		}
		// mittlerer Pool
		if (naechsteKarte >= 4 && naechsteKarte < 7 || naechsteKarte == -4
				|| naechsteKarte == -3) {

			int zähler = 1;
			int max = mittlereKarten.get(mittlereKarten.size() - 1);
			int maxGegner = vomGegnerNochNichtGelegt
					.get(vomGegnerNochNichtGelegt.size() - 1);
			while (max == 0) {
				max = mittlereKarten.get(mittlereKarten.size() - zähler);
				zähler++;
				if (zähler > 5) {
					if (sumKleineKarten != 0) {
						return austauschen(passivePlayer(kleineKarten));
					} else {
						if (sumMittlereKarten != 0) {
							return austauschen(passivePlayer(mittlereKarten));
						}
						return austauschen(passivePlayer(grosseKarten));
					}
				}
			}
			while (maxGegner == 0 || maxGegner > 10) {
				maxGegner = vomGegnerNochNichtGelegt
						.get(vomGegnerNochNichtGelegt.size() - zähler);
				zähler++;
				if (zähler > 15) {
					while (maxGegner == 0) {
						max = activePlayer(nochNichtGespielt);

						int z = max - maxGegner;
						if (z < 0) {
							if (sumKleineKarten != 0) {
								return austauschen(passivePlayer(kleineKarten));
							} else {
								if (sumMittlereKarten != 0) {
									return austauschen(passivePlayer(mittlereKarten));
								}
							}
						}
						for (int c = 1; c <= z; c++) {
							if (nochNichtGespielt.contains(maxGegner + c)) {
								if (!grosseKarten.contains(maxGegner + c)) {
									return austauschen(maxGegner + c);
								}
							}
						}

					}

					if (sumKleineKarten != 0) {
						return austauschen(passivePlayer(kleineKarten));
					} else {
						if (sumMittlereKarten != 0) {
							return austauschen(passivePlayer(mittlereKarten));
						}
						return austauschen(passivePlayer(grosseKarten));
					}
				}
			}

			for (int i = 10; i >= 6; i--) {
				//System.out.println("Max mittlerer pool wir" + max);
				//System.out.println("MaxGegner mittlerer pool wir" + maxGegner);
				if (maxGegner == max) {
					return austauschen(max);
				}
				if (maxGegner > max) {

					if (naechsteKarte == -4 || naechsteKarte == 6) {
						max = activePlayer(nochNichtGespielt);
						if (grosseKarten.contains(max) && max < 14) {
							return austauschen(max);
						} else {
							if (sumMittlereKarten != 0)
								return austauschen(activePlayer(mittlereKarten));
							if (sumKleineKarten != 0) {
								return austauschen(activePlayer(kleineKarten));
							}
							return austauschen(passivePlayer(nochNichtGespielt));
						}
					} else {
						if (sumKleineKarten != 0) {
							return austauschen(passivePlayer(kleineKarten));
						} else {
							if (sumMittlereKarten != 0) {
								return austauschen(passivePlayer(mittlereKarten));
							}
							return austauschen(passivePlayer(grosseKarten));
						}
					}
				}
				if (maxGegner < max) {
					// Differenz der abgefragten Karte mit der höchsten Karte
					// des Gegners
					int z = max - maxGegner;
					if (z < 0) {
						if (sumKleineKarten != 0) {
							return austauschen(passivePlayer(kleineKarten));
						} else {
							if (sumMittlereKarten != 0) {
								return austauschen(passivePlayer(mittlereKarten));
							}
						}
					}
					for (int c = 1; c <= z; c++) {
						if (nochNichtGespielt.contains(maxGegner + c)) {
							if (!grosseKarten.contains(maxGegner + c)) {
								return austauschen(maxGegner + c);
							}
						}
					}
				}
			}
		}
		// kleinerer Pool
		if (naechsteKarte >= 1 && naechsteKarte < 4 || naechsteKarte == -1
				|| naechsteKarte == -2) {

			int zähler = 1;
			int zähler2 = 1;
			int max = kleineKarten.get(kleineKarten.size() - 1);
			int maxGegner = vomGegnerNochNichtGelegt
					.get(vomGegnerNochNichtGelegt.size() - 1);
			while (max == 0) {
				max = kleineKarten.get(kleineKarten.size() - zähler);
				zähler++;
				if (zähler > 5) {
					if (sumKleineKarten != 0) {
						return austauschen(passivePlayer(kleineKarten));
					} else {
						if (sumMittlereKarten != 0) {
							return austauschen(passivePlayer(mittlereKarten));
						}
						return austauschen(passivePlayer(grosseKarten));
					}
				}
			}
			while (maxGegner == 0 || maxGegner > 5) {
				maxGegner = vomGegnerNochNichtGelegt
						.get(vomGegnerNochNichtGelegt.size() - zähler2);
				zähler2++;
				if (zähler2 > 15) {
					if (sumKleineKarten != 0) {
						return austauschen(passivePlayer(kleineKarten));
					} else {
						if (sumMittlereKarten != 0) {
							return austauschen(passivePlayer(mittlereKarten));
						}
						return austauschen(passivePlayer(grosseKarten));
					}
				}

			}
			for (int i = 5; i >= 1; i--) {
				//System.out.println("Max kleinerer pool wir" + max);
				//System.out.println("MaxGegner kleinerer pool wir" + maxGegner);
				if (maxGegner == max) {
					return austauschen(max);
				}
				if (maxGegner > max) {

					if (sumKleineKarten != 0) {
						return austauschen(passivePlayer(kleineKarten));
					} else {
						if (sumMittlereKarten != 0) {
							return austauschen(passivePlayer(mittlereKarten));
						}
						return austauschen(passivePlayer(grosseKarten));
					}
				}
				if (maxGegner < max) {
					// Differenz der abgefragten Karte mit der höchsten Karte
					// des Gegners
					int z = max - maxGegner;
					if (z < 0) {
						if (sumKleineKarten != 0) {
							return austauschen(passivePlayer(kleineKarten));
						} else {
							if (sumMittlereKarten != 0) {
								return austauschen(passivePlayer(mittlereKarten));
							}
						}
					}
					for (int c = 1; c <= z; c++) {
						if (nochNichtGespielt.contains(maxGegner + c)) {
							if (!grosseKarten.contains(maxGegner + c)) {
								return austauschen(maxGegner + c);
							}
						}
					}
				}
			}
		}
		return 0;
	}

	public int gibKarte(int naechsteKarte) {
		int ret = -99;
		int letzteKarteGegner = letzterZug();

		// letzter Zug
		if (letzteKarteGegner > 0) {
			vomGegnerNochNichtGelegt.set(
					vomGegnerNochNichtGelegt.indexOf(letzteKarteGegner), 0);
		}
		// Spiel let`s go
		ret = waehleKarte(naechsteKarte);
		return ret;
	}
}
