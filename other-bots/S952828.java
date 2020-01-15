

import java.util.ArrayList;
import java.util.Random;

/**
 * @author Timo Schuller, Stefan Haverkamp, Ali Demirci
 * @version (17.01.2012)
 */
public class S952828 extends HolsDerGeierSpieler {
	// Anlegen der Arrays für meine Karten, die Gegnerkarten, die Mittelkarten
	// und die Strategie.
	private ArrayList<Integer> meineKarten = new ArrayList<Integer>();
	private ArrayList<Integer> gegnerKarten = new ArrayList<Integer>();
	private ArrayList<Integer> mittelKarten = new ArrayList<Integer>();
	private ArrayList<Integer> meineStrategie = new ArrayList<Integer>();

	int werteKarte, k15_gespieltBei, k14_gespieltBei, merkeStichkarte = 0;

	// =================================================================================================================================
	// =================================================================================================================================
	//
	// Hier beginnt die Methode reset()
	//
	// =================================================================================================================================
	// =================================================================================================================================

	public void reset() {
			meineStrategie.clear();
			meineStrategie.trimToSize();
		// Prüft, ob bereits eine Runde gespielt wurde
		if (mittelKarten.size() == 16)
			strategieBerechnung();
		// Leert die Arrays
		meineKarten.clear();
		meineKarten.trimToSize();
		gegnerKarten.clear();
		gegnerKarten.trimToSize();
		mittelKarten.clear();
		mittelKarten.trimToSize();
	}

	// =================================================================================================================================
	// =================================================================================================================================
	//
	// Hier beginnt die Methode gibKarte()
	//
	// =================================================================================================================================
	// =================================================================================================================================
	public int gibKarte(int naechsteKarte) {
		//System.out.println("Es geht um die Karte: " + naechsteKarte);
		// Da wir von letzterZug() in der ersten Runde 1002 erhalten, adden wir
		// an die "0te" Position des Arrays auch Phantasiezahlen.
		if (meineKarten.size() == 0)
			meineKarten.add(98);
		if (mittelKarten.size() == 0)
			mittelKarten.add(1001);
		// Fügt die Mittelkarte und die Gegnerkarte den entsprechenden Arrays
		// hinzu.
		mittelKarten.add(naechsteKarte);
		gegnerKarten.add(letzterZug());
		// Fügt den letzten Zug des Gegners eines Spiels dem Gegner-Array hinzu.
		if (mittelKarten.size() == 16)
			zugDesGegners();
		// Ermitteln, ob der letzte Zug unentschieden war.
		if (gegnerKarten.get(mittelKarten.size() - 2) == meineKarten
				.get(mittelKarten.size() - 2)) {
			werteKarte = merkeStichkarte + naechsteKarte
					+ mittelKarten.get(mittelKarten.size() - 1);
			merkeStichkarte = werteKarte;
		} else {
			werteKarte = naechsteKarte;
			merkeStichkarte = 0;
		}
		// strat=0 ist die erste Runde des Spiels
		if (meineStrategie.size() == 0) {
			if (naechsteKarte > 0)
				naechsteKarte = naechsteKarte + 5;
			else
				naechsteKarte = naechsteKarte + 6;
			meineKarten.add(naechsteKarte);
			return naechsteKarte;
			// Der else Teil wird ab der zweiten Runde & bis zum Ende des
			// Spiels ausgeführt
		} else {
			// Entscheidet anhand der Wertekarte, was getan werden soll.
			switch (werteKarte) {
			case 10:
				naechsteKarte = meineStrategie.get(0);
				naechsteKarte = kartensuche(naechsteKarte);
				break;
			case 9:
				naechsteKarte = meineStrategie.get(1);
				naechsteKarte = kartensuche(naechsteKarte);
				break;
			case 8:
				naechsteKarte = meineStrategie.get(2);
				naechsteKarte = kartensuche(naechsteKarte);
				break;
			case 7:
				naechsteKarte = meineStrategie.get(3);
				naechsteKarte = kartensuche(naechsteKarte);
				break;
			case -4:
				naechsteKarte = meineStrategie.get(5);
				naechsteKarte = kartensuche(naechsteKarte);
				break;
			case -5:
				naechsteKarte = meineStrategie.get(4);
				naechsteKarte = kartensuche(naechsteKarte);
				break;
			default:
				naechsteKarte = kartensuche(werteKarte);
				break;
			}
			meineKarten.add(naechsteKarte);
			return naechsteKarte;
		}
	}

	// =================================================================================================================================
	// =================================================================================================================================
	//
	// Hier beginnt die Methode strategieBerechnung()
	//
	// =================================================================================================================================
	// =================================================================================================================================

	private void strategieBerechnung() {
		// suche, wo der Gegner die 15 gespielt hat
		for (int i = 1; i <= 15; i++) {
			if (gegnerKarten.get(i) == 15)
				k15_gespieltBei = mittelKarten.get(i);
		}
		// suche, wo der Gegner die 14 gespielt hat
		for (int i = 1; i <= 15; i++) {
			if (gegnerKarten.get(i) == 14)
				k14_gespieltBei = mittelKarten.get(i);
		}

		// wertet die Suche aus. GespieltBei ist die Mittelkarte, bei welcher
		// der gegner die 15 gespielt hat und GespieltBeiVz ist die Mittelkarte,
		// bei welcher der Gegner die 14 gespielt hat.
		if (k15_gespieltBei >= 6) {
			if (k15_gespieltBei == 6) {
				meineStrategie.add(15); // spiele bei 10
				meineStrategie.add(14); // spiele bei 9
				meineStrategie.add(13); // spiele bei 8
				meineStrategie.add(random(2)); // spiele bei 7
				meineStrategie.add(random(2)); // spiele bei -5
				meineStrategie.add(11); // spiele bei -4
			}
			if (k15_gespieltBei == 7) {
				meineStrategie.add(15); // spiele bei 10
				meineStrategie.add(random(4)); // spiele bei 9
				meineStrategie.add(random(4)); // spiele bei 8
				meineStrategie.add(1); // spiele bei 7
				meineStrategie.add(11); // spiele bei -5
				meineStrategie.add(random(4)); // spiele bei -4
			}
			if (k15_gespieltBei == 8) {
				meineStrategie.add(15); // spiele bei 10
				meineStrategie.add(13); // spiele bei 9
				meineStrategie.add(1); // spiele bei 8
				if (k14_gespieltBei == 7)
					meineStrategie.add(2); // spiele bei 7
				else
					meineStrategie.add(14); // spiele bei 7
				meineStrategie.add(random(3)); // spiele bei -5
				meineStrategie.add(10); // spiele bei -4
			}
			if (k15_gespieltBei == 9) {
				meineStrategie.add(15); // spiele bei 10
				meineStrategie.add(1); // spiele bei 9
				if (k14_gespieltBei == 8)
					meineStrategie.add(2); // spiele bei 8
				else
					meineStrategie.add(14); // spiele bei 8
				meineStrategie.add(random(3)); // spiele bei 7
				meineStrategie.add(random(3)); // spiele bei -5
				meineStrategie.add(random(3)); // spiele bei -4
			}
			if (k15_gespieltBei == 10) {
				meineStrategie.add(1); // spiele bei 10
				meineStrategie.add(15); // spiele bei 9
				meineStrategie.add(14); // spiele bei 8
				meineStrategie.add(random(3)); // spiele bei 7
				meineStrategie.add(random(3)); // spiele bei -5
				meineStrategie.add(random(3)); // spiele bei -4
			}
		} else {
			meineStrategie.add(random(5)); // spiele bei 10
			meineStrategie.add(random(5)); // spiele bei 9
			meineStrategie.add(random(5)); // spiele bei 8
			meineStrategie.add(random(5)); // spiele bei 7
			meineStrategie.add(random(5)); // spiele bei -5
			meineStrategie.add(random(5)); // spiele bei -4
		}

	}

	// =================================================================================================================================
	// =================================================================================================================================
	//
	// Hier beginnt die Methode random()
	//
	// =================================================================================================================================
	// =================================================================================================================================

	private int random(int i) {
		// Gibt eine Zufallszahl zwischen 10 und 10+Eingabe aus. Hierdurch soll
		// eine Dynamik geschaffen werden, die es der Gegnerischen erschwert ein
		// Muster in den Spielzügen zu erkennen.
		Random r = new Random();
		i = r.nextInt(i) + 10;
		return i;
	}

	// =================================================================================================================================
	// =================================================================================================================================
	//
	// Hier beginnt die Methode zugDesGegners()
	//
	// =================================================================================================================================
	// =================================================================================================================================

	private void zugDesGegners() {
		// Ermittelt den letzten Zug des Gegners in einem Spiel.
		for (int i = 1; i <= 15; i++) {
			if (!gegnerKarten.contains(i)) {
				gegnerKarten.add(i);

			}
		}

	}

	// =================================================================================================================================
	// =================================================================================================================================
	//
	// Hier beginnt die Methode kartensuche()
	//
	// =================================================================================================================================
	// =================================================================================================================================

	private int kartensuche(int naechsteKarte) {
		// Ermitteln der nächsten Karte.
		// Wenn diese Karte noch nicht gespielt wurde und 15=>Karte=>1, dann gib
		// diese Karte wieder zurück.
		if (!meineKarten.contains(naechsteKarte) && naechsteKarte > 0
				&& naechsteKarte < 16)
			return naechsteKarte;
		if (meineKarten.contains(naechsteKarte)
				&& (naechsteKarte > 6 || naechsteKarte < -2)) {
			for (int i = 15; i >= 1; i--) {
				if (!meineKarten.contains(i)) {
					naechsteKarte = i;
					break;
				}
			}
		} else {
			for (int i = 1; i <= 15; i++) {
				if (!meineKarten.contains(i)) {
					naechsteKarte = i;
					break;
				}

			}
		}
		return naechsteKarte;

	}

}
