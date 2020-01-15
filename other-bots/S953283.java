//Bot von Tobias Rücker, Tim Mühlhausen, Moritz Feuerbach



import java.util.ArrayList;

public class S953283 extends HolsDerGeierSpieler {

	private int letzterZug;
	private int zugNr;
	private int tempPunkte;

	private ArrayList<Integer> kartenEigen;
	private ArrayList<Integer> kartenGegner;

	/**
	 * Ruft reset() auf, um Werte zu setzen
	 */
	public S953283() {
		reset();
	}

	/**
	 * Setzt den Spieler zurück
	 */
	public void reset() {
		// Karten-Arrays
		kartenEigen = new ArrayList<Integer>();
		kartenGegner = new ArrayList<Integer>();
		for (int i = 1; i < 16; i++) {
			kartenEigen.add(i);
			kartenGegner.add(i);
		}
		
		letzterZug = 1;
		zugNr = 0;

		tempPunkte = 0;
	}

	/**
	 * Spielzug von seiten des Spielers
	 * 
	 * @param punkte
	 *            Punkte, um die gespielt werden
	 * @return Gespielte Karte
	 */
	public int gibKarte(int punkte) {

		//Bei Unendschieden im letzten Zug werden Punkte addiert
		if (letzterZug == letzterZug()) {
			punkte += tempPunkte;
		}

		// Erster Zug
		if (zugNr == 0) {
			tempPunkte = punkte;
			kartenEigen.remove(0);
			return ++zugNr; // Zugnummer wird auf 1 erhöht und dann
							// zurückgegeben
		}

		// Züge abspeichern
		kartenGegner.remove(kartenGegner.indexOf(letzterZug()));
		tempPunkte = punkte;

		// Zugnummer erhöhen
		zugNr++;

		// Zu legende Karte ermitteln und zurück geben
		int ret = welcheKarte(punkte);
		letzterZug=ret;
		return ret;
	}

	/**
	 * Ermittelt, welche Karte gelegt werden muss
	 * @param punkte Aktuell in der Mitte liegende Punkte
	 * @return zu legende Karte
	 */
	private int welcheKarte(int punkte) {
		if (punkte > 7) 													// 10 - 8 (von oben abschmeißen)
			return kartenEigen.remove(kartenEigen.size() - 1);
		else {
			if (punkte > -2 && punkte < 6)									// -1 - 5 (von unten abschmeißen)
				return kartenEigen.remove(0);
			else {
				if (punkte > -5 && punkte < -1) { 							// -4 - -2
					for (int i = 9; i > 0; i--) {
						if (kartenEigen.contains(i)) {
							int ind = kartenEigen.indexOf(new Integer(i));
							return kartenEigen.remove(ind);
						}
					}
					return kartenEigen.remove(0);
				} else {
					for (int i = 12; i > 0; i--) { 							// -5, 6, 7
						if (kartenEigen.contains(i)) {
							int ind = kartenEigen.indexOf(new Integer(i));
							return kartenEigen.remove(ind);
						}
					}
					return kartenEigen.remove(0);
				}
			}
		}
	}
}
