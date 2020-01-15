

import java.util.ArrayList;
import java.util.Collections;

/**
 * 
 * @author Nils Aland, Endris Hinckel, Holger Kloetzner
 * 
 * Diese Spielstrategie fuer das Spiel HolsDerGeier baut auf zwei Strategien auf:
 * Die erste Strategie weist den Punktekarten statisch Handkarten zu und spielt diese aus.
 * Ab Erreichen des einstellbaren Grenzwertes wird in die zweite Strategie uebergegangen.
 * Die zweite Strategie errechnet auf Grundlage der vorgegebenen Anzahl der Kombinationen
 * die niedrigsten zum Gewinn benoetigten Punktekarten und versucht diese zu gewinnen. 
 *
 */
public class S953539 extends HolsDerGeierSpieler {

	/**
	 * Grenzwert, ab welcher Punktzahl in Strategie 2 gewechselt werden soll
	 * (Punkte in das Positive umgerechnet - Punktemaximum liegt bei 70)
	 */
	private final int GRENZWERT = 24;
	
	/**
	 * Laenge der Kombinationen fuer zweite Strategie
	 */
	private final int KOMBINATIONEN = 3;
	
	/**
	 * Fuer Gewinn benoetigte Punktzahl
	 */
	private final int GEWINNPUNKTZAHL = 36;
	
	/**
	 * Unsere Handkarten
	 */
	private ArrayList<Integer> eigeneKarten = new ArrayList<Integer>();

	/**
	 * Gegner Handkarten
	 */
	private ArrayList<Integer> gegnerKarten = new ArrayList<Integer>();

	/**
	 * Kartenstapel Punktekarten
	 */
	private ArrayList<Integer> punkteKarten = new ArrayList<Integer>();

	/**
	 * Eigener Punktestand
	 */
	private int eigenePunkte = 0;

	/**
	 * Karte die wir in der letzten Runde ausgespielt haben
	 */
	private int letzterEigenerZug = 0;

	/**
	 * Letzte Punktekarte zur Berechnung der Punkteverteilung in
	 * der jeweils naechsten Runde
	 */
	private int letztePunkte = 0;

	/**
	 * Zwischenspeicher fuer Punkte, die nach einem
	 * Unentschieden nicht verteilt wurden
	 */
	private int unentschiedenPunkte = 0;

	/**
	 * Flag, ob in der aktuellen Runde ein Vorzeichenwechsel
	 * (durch Umrechnung der Punkte in das Positive) stattgefunden hat
	 */
	private boolean vorzeichenwechsel = false;
	
	/**
	 * Flag, ob in der letzten Runde ein Vorzeichenwechsel stattgefunden hat
	 */
	private boolean vorzeichenwechselLetzte = false;
	
	/**
	 * Flag, ob in der vorletzten Runde ein Vorzeichenwechsel stattgefunden hat
	 */
	private boolean vorzeichenwechselVorletzte = false;

	/**
	 * Flag zur Unterscheidung ob schon eine Runde gespielt wurde
	 */
	private boolean gestartet = false;

	/**
	 * Zuruecksetzen der Punkte- und Kartenspeicher (sowie Hilfsvariablen)
	 * um ein neues Spiel zu beginnen
	 */
	@Override
	public void reset() {
		eigeneKarten.clear();
		gegnerKarten.clear();
		letzterEigenerZug = 0;
		letztePunkte = 0;
		eigenePunkte = 0;
		unentschiedenPunkte = 0;
		
		// Flag, dass Spiel noch nicht gestartet wurde, setzen
		gestartet = false;
		vorzeichenwechsel = false;
		vorzeichenwechselLetzte = false;
		vorzeichenwechselVorletzte = false;
		
		// Handkarten auffuellen
		for (int i=1; i<=15; i++) {
			eigeneKarten.add(i);
			gegnerKarten.add(i);
		}

		punkteKarten.clear();

		// Punktekarten auffuellen
		for (int i=1; i<=10; i++) {
			punkteKarten.add(i);
		}
		for (int i=1; i<=5; i++) {
			punkteKarten.add(i);
		}
		
		// Punktekarten sortieren (von niedrig nach hoch)
		Collections.sort(punkteKarten);
	}

	/**
	 * Methode zum Spielen der Karte und Auswahl der Strategie
	 * 
	 * @param naechsteKarte aktuelle Punktekarte, um die gespielt wird
	 */
	@Override
	public int gibKarte(int naechsteKarte) {
		int letzterGegnerZug = this.letzterZug();
		// letzte vom Gegner gespielte Karte aus Gegner Hand entfernen
		removeFromList(gegnerKarten, letzterGegnerZug);

		// Beginn der Taktik
		// Die Karte, die wir spielen wollen
		int karte = 0;

		// Umrechnen der Punkte in das Positive
		naechsteKarte = setVorzeichenwechsel(naechsteKarte);

		// Aufruf der Punkteverteilungsmethode und der Grenzwertmethode
		verteilePunkte(naechsteKarte, letzterGegnerZug);
		Integer verbleibendePunkte = isGrenzwert();

		// Strategie 2 ausfuehren wenn vebleibendePunkte nicht null
		if (verbleibendePunkte != null) {
			karte = strategie2(verbleibendePunkte, naechsteKarte);
		} else {//ansonsten Strategie 1 ausfuehren
			karte = strategie1(naechsteKarte);
		}
		// Ende der Taktik

		// aktuelle Punktekarte von Kartenstapel entfernen
		removeFromList(punkteKarten, naechsteKarte);

		// jetzt von uns gespielte Karte aus Hand entfernen
		removeFromList(eigeneKarten, karte);
		letzterEigenerZug = karte;
		return karte;
	}

	/**
	 * Methode zum entfernen einer gelegten Karte aus dem entsprechenden Array
	 * 
	 * @param list Name der Arraylist
	 * @param value Wert welcher entfert werden soll
	 * @return true wenn entfernt, oder false falls keine Karte mehr vorhanden
	 */
	private boolean removeFromList(ArrayList<Integer> list, int value) {
		int index = list.indexOf(value);
		if (index >= 0) {
			list.remove(index);
			return true;
		}
		return false;
	}

	/**
	 * Punkteverteilung mit in das Positive umgerechneten Punkten
	 * 
	 * @param naechsteKarte vom Spielleiter gelegte Punktekarte
	 * @param letzterGegnerZug letzte vom Gegner gelegte Karte
	 */
	private void verteilePunkte(int naechsteKarte, int letzterGegnerZug) {
		// Pruefung ob erste Runde oder nicht (da Punkteverteilung erst ab 2. Runde moeglich)
		if (gestartet) {
			// 1. Fall: MeinZug ungleich GegnerZug
			if (letzterEigenerZug != letzterGegnerZug) {
				// 1.1. Fall: Unentschieden vorausgegangen
				if (unentschiedenPunkte != 0) {
					// 1.1.1. Fall: MeinZug groesser GegnerZug (ich gewinne)
					if (letzterEigenerZug > letzterGegnerZug) {
						// 1.1.1.1. Fall: Gleiche Vorzeichen (bei Unentschieden
						// und darauffolgender Runde)
						if (vorzeichenwechselVorletzte == vorzeichenwechselLetzte) {
							eigenePunkte += unentschiedenPunkte;
							eigenePunkte += letztePunkte;
						} else {// 1.1.1.2. Fall: Verschiedene Vorzeichen
							eigenePunkte += punkteVorzeichenwechsel(true, unentschiedenPunkte, letztePunkte);
							punkteVorzeichenwechsel(false, unentschiedenPunkte, letztePunkte);
						}
					} else {// 1.1.2. Fall: MeinZug kleiner GegnerZug (Gegner gewinnt)
						// 1.1.2.1. Fall: Gleiche Vorzeichen
						if (vorzeichenwechselVorletzte == vorzeichenwechselLetzte) {
						} else {// 1.1.2.2. Fall: Verschiedene Vorzeichen
							punkteVorzeichenwechsel(true, unentschiedenPunkte, letztePunkte);
							eigenePunkte += punkteVorzeichenwechsel(false, unentschiedenPunkte, letztePunkte);
						}
					}
				} else {// 1.2. Fall: kein Unentschieden vorausgegangen
					// Gewinnen wir...
					if (letzterEigenerZug > letzterGegnerZug) {
						eigenePunkte += letztePunkte;
					} else {// ...oder gewinnt der Gegner?
						
					}
				}
				unentschiedenPunkte = 0;
			} else {// 2. Fall: Punkte zusammenrechnen bei Unentschieden
				unentschiedenPunkte += letztePunkte;
			}
		} else {// Nach erster Runde auf true
			gestartet = true;
		}
		// Aktuelle Karte fuer naechste Berechnung zwischenspeichern
		letztePunkte = naechsteKarte;
	}

	/**
	 * Punktevergabe bei Vorzeichenwechsel
	 * 
	 * @param Berechnung fuer Gewinner oder Verlierer durchfuehren
	 * @param unentschiedenPunkte Punktewert bei Unentschieden
	 * @param letztePunkte Punktewert nach Unentschieden
	 * @return hoeherer Punktewert an Gewinner
	 */
	private int punkteVorzeichenwechsel(boolean gewinner, int unentschiedenPunkte, int letztePunkte) {
		int result = 0;
		if (gewinner == true) {
			result = unentschiedenPunkte > letztePunkte ? unentschiedenPunkte : letztePunkte;
		} else {
			result = unentschiedenPunkte > letztePunkte ? letztePunkte : unentschiedenPunkte;
		}
		return result;
	}

	/**
	 * Feststellen ob von vorletzter Punktekarte zu letzter Punktekarte ein
	 * Vorzeichenwechsel vorhanden ist und die aktuelle Punktekarte (falls
	 * negativ) in positiv umrechnen
	 * 
	 * @param naechsteKarte Punktekarte welche vom Spielleiter gelegt wurde
	 */
	private int setVorzeichenwechsel(int naechsteKarte) {
		vorzeichenwechselVorletzte = vorzeichenwechselLetzte;
		vorzeichenwechselLetzte = vorzeichenwechsel;
		vorzeichenwechsel = false;
		if (naechsteKarte < 0) {
			vorzeichenwechsel = true;
			naechsteKarte *= -1;
		}
		return naechsteKarte;
	}

	/**
	 * Strategie1 (Grundstrategie)
	 * Statische Zuweisung der Karten
	 * 
	 * @param naechsteKarte Punktekarte welche vom Spielleiter gelegt wurde
	 * @return Karte die von uns gelegt wird
	 */
	private int strategie1(int naechsteKarte) {
		int result = 0;
		switch (naechsteKarte) {
		case 10: {
			result = entscheideHochTief(15, 2);
			break;
		}
		case 9: {
			result = entscheideHochTief(14, 1);
			break;
		}
		case 8: {
			result = legeHoehere(15, 2);
			break;
		}
		case 7: {
			result = legeHoehere(14, 1);
			break;
		}
		case 6: {
			result = 13;
			break;
		}
		case 5: {
			result = legeHoehere(12, 11);
			break;
		}
		case 4: {
			result = legeHoehere(10, 9);
			break;
		}
		case 3: {
			result = legeHoehere(8, 7);
			break;
		}
		case 2: {
			result = legeHoehere(6, 5);
			break;
		}
		case 1: {
			result = legeHoehere(4, 3);
			break;
		}

		}
		return result;
	}

	/**
	 * Methode zum bestimmen ob hoehere oder tiefere Karte gelegt werden soll
	 * mit Ueberpruefung ob Gegner diese Karte selbst besitzt um Gleichstand zu
	 * verhindern
	 * 
	 * @param hoch hoeher zugeordnete Karte fuer entsprechende Punktekarte
	 * @param tief tiefer zugeordnete Karte fuer entsprechende Punktekarte
	 * @return Karte die gelegt werden soll
	 */
	private int entscheideHochTief(int hoch, int tief) {
		if (gegnerKarten.contains(hoch) || !eigeneKarten.contains(hoch)) {
			return tief;
		} else {
			return hoch;
		}
	}

	/**
	 * Methode zum bestimmen ob hoehere oder tiefere Karte gelegt werden soll
	 * (hoehere wird immer dann gelegt, wenn noch vorhanden)
	 * 
	 * @param hoch hoeher zugeordnete Karte fuer entsprechende Punktekarte
	 * @param tief tiefer zugeordnete Karte fuer entsprechende Punktekarte
	 * @return Karte die gelegt werden soll
	 */
	private int legeHoehere(int hoch, int tief) {
		if (eigeneKarten.contains(hoch)) {
			return hoch;
		} else {
			return tief;
		}
	}

	/**
	 * Strategie2 (Absicherungsstrategie)
	 * Wird aufgerufen sobald der Grenzwert erreicht wurde.
	 * Berechnet die niedrigst benoetigte Kombination noch vorhandener Punktekarten
	 * zum Gewinnen und teilt dementsprechend unsere Handkarten zu.
	 * 
	 * Wenn die aktuelle Punktekarte eine der Karten ist, die zum Sieg benoetigt
	 * wird, wird eine der Hoehe der Punktekarte entsprechende Handkarte
	 * gespielt; ansonsten eine niedrige Handkarte (der Hoehe der nicht gewollten
	 * Punktekarte entsprechend).
	 * 
	 * Es werden also auch innerhalb der Kombinationen hoehere Handkarten fuer
	 * hoehere Punktekarten gespielt.
	 * 
	 * Reichen weniger Karten zum Sieg aus, als die Kombination enthaelt, wird
	 * trotzdem die groeßte moegliche Anstrengung zum Gewinnen der ausreichenden
	 * Punktekarten erzielt; dabei aber versucht, moeglichst niedrige
	 * Punktekarten zu Gewinnen.
	 * 
	 * @param verbleibendePunkte Punkte die wir zum Gewinnen benoetigen
	 * @param aktuellePunktekarte aktuell liegende Punktekarte vom Spielleiter
	 * @return Karte die ausgespielt werden soll
	 */
	private int strategie2(int verbleibendePunkte, int aktuellePunktekarte) {
		// Eigene Handkarten sortieren um sicherzustellen, dass fuer nicht
		// gewollte (da fuer Sieg nicht benoetigte) Punktekarten eine Handkarte
		// ausgegeben wird, die der Wertigkeit (niedrigste Handkarte fuer
		// niedrigste Punktekarte) entspricht
		Collections.sort(eigeneKarten);

		// Niedrige Handkarte aus Rangfolge auswaehlen
		int indexPunktekarte = punkteKarten.indexOf(new Integer(aktuellePunktekarte));
		int result = eigeneKarten.get(indexPunktekarte);

		// Sortierung umkehren, um fuer hohe Punktekarten entsprechend hohe
		// gewollte (da fuer Sieg benoetigte) Karten zu legen (nach Rangfolge)
		Collections.reverse(eigeneKarten);

		// Benoetigte Kombination zum Sieg berechnen
		// Wird jede Runde neu berechnet, falls sich Aenderungen ergeben haben
		// (z.B. gewollte Punktekarte wurde nicht gewonnen)
		ArrayList<Integer> indexes = getKombination(verbleibendePunkte);

		// Indexstellen der Kombination umkehren, um fuer hohe Punktekarten
		// hohe Handkarten zu spielen
		Collections.reverse(indexes);

		// Ist die aktuelle Punktekarte eine Karte, die fuer den Sieg benoetigt wird?
		// Dazu alle Indexstellen der Kombination durchgehen...
		for (int i=0; i<indexes.size(); i++) {
			int inhalt = punkteKarten.get(indexes.get(i));
			// ...und bei Treffer jeweilige Karte ausspielen
			if (inhalt == aktuellePunktekarte) {
				result = eigeneKarten.get(indexes.get(i));
				break;
			}
		}

		return result;
	}

	/**
	 * Grenzwertmethode: Ist die Anzahl der eigenen Punkte groesser oder gleich
	 * dem festgelegten Grenzwert?
	 * 
	 * @return fehlende Punkte zum Gewinnen oder null falls Grenzwert nicht erreicht
	 */
	private Integer isGrenzwert() {
		Integer result = null;
		if (eigenePunkte >= GRENZWERT) {
			result = GEWINNPUNKTZAHL - eigenePunkte;
		}
		return result;
	}

	/**
	 * Moeglichst kleinste Kombination (der Werte) der noch benoetigten
	 * Punktekarten zum Gewinnen errechnen.
	 * 
	 * Dabei durchlaufen mehrere fiktive Zeiger (deren Wert jeweils den Index einer
	 * Punktekarte darstellt) die Liste der noch verfuegbaren
	 * Punktekarten und ermitteln alle Teilmengen der Laenge n (Anzahl der
	 * Kombinationen) der Obermenge (Punktekarten). Punktekarten, deren Wert mit
	 * und ohne negativem Vorzeichen existiert werden dabei doppelt
	 * beruecksichtigt (mit jeweils positivem Wert).
	 * 
	 * Bsp. mit 3 Kombinationen und 6 verbleibenden Punktekarten:
	 * 
	 * Startposition:
	 * index Punktekarten 0 1 2 3 4 5
	 * zeiger[]           x x x
	 * 
	 * Von der Startposition aus wird der letzte Zeiger in jedem Durchlauf eine
	 * Stelle weiter nach rechts verschoben. Hat der Zeiger seine Endposition
	 * erreicht, wird der Zeiger davor eine Stelle nach rechts verschoben. Die Zeiger
	 * dahinter werden dann an den Stellen direkt hinter diesem Zeiger positioniert.
	 * Erst wenn alle Zeiger ihre Endposition erreicht haben, sind alle Kombinationen
	 * getestet worden.
	 * 
	 * Endposition:
	 * index Punktekarten 0 1 2 3 4 5
	 * zeiger[]                 x x x
	 * 
	 * Bei jedem Durchlauf wird geprueft, ob die aktuelle Kombination zum
	 * Gewinnen ausreicht und ob sie mit einer geringeren Punktesumme als die
	 * vorher als beste Kombination errechnete Reihenfolge auskommt.
	 * 
	 * @param punkte benoetigte Punkte zum Gewinnen
	 * @return ArrayList mit den Indexstellen der Karten zum Gewinnen in der Punktekarten-Liste
	 */
	private ArrayList<Integer> getKombination(int punkte) {

		// Indexpositionen, die nachher zurueckgegeben werden
		ArrayList<Integer> indexes = new ArrayList<Integer>();
		
		// Startwert Punktesumme der aktuellen Kombination
		int summe = 70;
		int size = punkteKarten.size();

		// Flexible Anzahl der Kombinationen (n Karten fuer Sieg testen)
		int aktKomb = KOMBINATIONEN;

		// Sind ueberhaupt noch n Kombinationen moeglich?
		// Wenn nicht, dann maximale Anzahl an Kombinationen setzen
		// (also Anzahl der restlichen Punktekarten)
		if (aktKomb > size) {
			aktKomb = size;
		}

		// Array mit Zeigern (Laenge = Anzahl der zu testenden Kombinationen)
		// auf den Index der jeweiligen Karte in den noch verbleibenden Punktekarten;
		// Diese Zeiger werden jeweils verschoben, um alle Kombinationen durchzutesten
		int[] zeiger = new int[aktKomb];

		// Initialisierung der Zeiger in Startposition (0,1,2,etc.)
		for (int i=0; i<zeiger.length; i++) {
			zeiger[i] = i;
		}

		// Schleife zum Testen aller Kombinationen, bis Abbruchbedingung
		// (alle Zeiger in Endposition) erfuellt;
		// Abbruchbedingung siehe weiter unten
		while (true) {
			// Summe der aktuellen Kombination waehrend diesem Durchlauf der Schleife
			int tempSumme = 0;

			// Werte (auf die die Zeiger "zeigen") zusammenzaehlen...
			for (int i=0; i<zeiger.length; i++) {
				tempSumme += punkteKarten.get(zeiger[i]);
			}

			// ...und testen, ob genug fuer Sieg und kleiner als im vorherigen Durchlauf
			// (falls nicht kleiner, reicht zuletzt errechnete optimale Kombination aus,
			// diese arbeitet dann mit niedrigeren Karten)
			if (tempSumme >= punkte && tempSumme < summe) {
				// Aktuelle niedrigste Summe anpassen
				summe = tempSumme;

				// Zeigerpositionen fuer Rueckgabewert uebernehmen
				indexes.clear();
				for (int z : zeiger) {
					indexes.add(z);
				}
			}

			// Abbruchbedingung aus Schleife
			// Sind alle Zeiger in Endposition, also alle Kombinationen getestet?

			// Endpositionen vorraussetzen
			boolean breakOut = true;

			// Alle Zeiger durchlaufen
			for (int z=0; z<zeiger.length; z++) {
				// Ist Zeiger in Endposition?
				if (zeiger[z] != size-aktKomb+z) {
					// wenn nicht, dann keinen Abbruch zulassen
					breakOut = false;
				}
			}

			// Ist Abbruch zugelassen? Wenn ja, Schleife abbrechen
			if (breakOut) {
				break;
			}
			// Ende der Abbruchbedingung

			// Wird der letzte Zeiger eine Stelle weiter verschoben?
			// (Das passiert nur, wenn keiner der vorherigen Zeiger eine
			// Stelle weiter geschoben wird)
			boolean verschieben = true;

			// Vorherigen Zeiger eine Stelle nach rechts verschieben, wenn
			// Endposition eines Zeigers erreicht; danach alle Zeiger dahinter
			// jeweils eine Stelle hinter verschobenem positionieren

			// Alle Zeiger durchgehen, beginnend bei letztem (bis zweiten Zeiger)
			for (int z = zeiger.length-1; z>0; z--) {
				// Hat Zeiger seine Endposition erreicht und steht er nicht
				// direkt hinter vorhergehendem Zeiger?
				// (Dann waeren beide in Endposition und der vorhergehende Zeiger wird getestet)
				if (zeiger[z] == size-(zeiger.length-z) && zeiger[z] != zeiger[z-1]+1) {
					// Vorhergehenden Zeiger eine Stelle nach rechts verschieben
					zeiger[z-1]++;
					
					// Verschieben des letzten Zeigers nicht zulassen, um aktuelle Kombination auch zu testen
					// (Test passiert zu Beginn des naechsten Schleifendurchlaufs)
					verschieben = false;

					// Alle nachfolgenden Zeiger hinter gerade verschobenen positionieren
					for (int y=z, schiebe=1; y<zeiger.length; y++, schiebe++) {
						// betrachteter Zeiger hat Stelle des weiter oben
						// verschobenen Zeigers + Zeigernummer hinter diesem
						zeiger[y] = zeiger[z-1] + schiebe;
					}
					// Wurde ein Zeiger verschoben, muessen die anderen Zeiger nicht getestet werden
					break;
				}
			}

			// Wird der letzte Zeiger verschoben (nur dann, wenn kein anderer Zeiger verschoben wurde)?
			if (verschieben) {
				zeiger[zeiger.length-1]++;
			}
		}

		// Kombination der Zeigerpositionen (Indexstellen in der punkteKarten-Liste) zurueckgeben
		return indexes;
	}
}
