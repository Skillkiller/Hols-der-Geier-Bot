

import java.util.ArrayList;

/**
 * Beschreiben Sie hier die Klasse Geier.
 * 
 * @author Florian Weber & Ronald Mettken
 * @version 07.01.2011
 */
public class FloRon extends HolsDerGeierSpieler {

	// ArrayListe der Karten die zu gewinnen sind
	private ArrayList<Integer> toWin = new ArrayList<Integer>();
	// ArrayListe der Karten die der Gegner noch besitzt
	private ArrayList<Integer> toPlayByOpponent = new ArrayList<Integer>();
	// ArrayList der Karten die wir noch besitzen
	private ArrayList<Integer> toPlayByMe = new ArrayList<Integer>();
	
	// Karte die es in der Runde zuvor zu gewinnen gab
	int lastCard = 0;
	
	// Karte die wir in der Runde zuvor gespielt haben
	int myLastCard = 0;
	
	// Karte die der Gegner in der Runde zuvor gespielt hat
	int oppLastCard = 0;

	// Container-Variable um die Summe der Karten bei Unentschieden zu speichern
	int sumDraw = 0;

	// Reset-Methode in der alle ArrayListen und Container-Variablen zur�ckgesetzt werden
	public void reset() {
		lastCard = 0;
		myLastCard = 0;
		oppLastCard = 0;

		toWin.clear();
		for (int i = -5; i < 11; i++) {
			if (i != 0) {
				toWin.add(i);
			}
		}
		toPlayByOpponent.clear();
		for (int i = 1; i < 16; i++) {
			toPlayByOpponent.add(i);
		}
		toPlayByMe.clear();
		for (int i = 1; i < 16; i++) {
			toPlayByMe.add(i);
		}
	}

	// Methode die unsere gespielte Karte zur�ckgibt
	public int gibKarte(int nextCard) {
		// unsere Karte wird f�r die Erkennung von "niedrigen" Summen der zu gewinnenden Unentschieden auf -99 gesetzt
		int myCard = -99;

		// letzte Karte des Gegners l�schen um verbleibende Karten zu erhalten
		oppLastCard = letzterZug();
		if (oppLastCard > 0)
			toPlayByOpponent.remove(toPlayByOpponent.indexOf(oppLastCard));

		// ArrayListe um die zu gewinnenden Karten > 2 zu speichern
		ArrayList<Integer> tempZG = new ArrayList<Integer>();

		// ArrayListe wird mit den zu gewinnenden Karten > 2 gef�llt
		for (int j = 0; j < toWin.size(); j++) {
			if (toWin.get(j) > 2) {
				tempZG.add(toWin.get(j));
			}
		}

		// ArrayListe die die untere H�lfte der zu gewinnenden Karten > 2 speichert
		ArrayList<Integer> ersteHaelfte = new ArrayList<Integer>();
		// ArrayListe die die obere H�lfte der zu gewinnenden Karten > 2 speichert
		ArrayList<Integer> zweiteHaelfte = new ArrayList<Integer>();

		// Teilt die �brigen zu gewinnende Karten durch 2
		int count = (int) (tempZG.size() / 2);

		// F�llt beide H�lften
		for (int i = 0; i < tempZG.size(); i++) {
			if (i < count) {
				ersteHaelfte.add(tempZG.get(i));
			} else {
				zweiteHaelfte.add(tempZG.get(i));
			}
		}

		// ArrayListen zum Speichern meiner �brigen Karten in Drittel
		ArrayList<Integer> erstesDrittel = new ArrayList<Integer>();
		ArrayList<Integer> zweitesDrittel = new ArrayList<Integer>();
		ArrayList<Integer> drittesDrittel = new ArrayList<Integer>();

		// Teilt unsere �brigen Karten durch 3
		count = (int) (toPlayByMe.size() / 3);

		// F�llt die Drittel
		for (int i = 0; i < toPlayByMe.size(); i++) {
			if (i < count || i < 1) {
				erstesDrittel.add(toPlayByMe.get(i));
			} else if (i < count * 2) {
				zweitesDrittel.add(toPlayByMe.get(i));
			} else {
				drittesDrittel.add(toPlayByMe.get(i));
			}
		}
		
		// Unentschieden zur�cksetzen
		if(oppLastCard != myLastCard) {
			sumDraw = 0;
		}

		// Falls die letzte zu gewinnende Karte ausgespielt wird, spiele unsere letzte verbleibende
		if (toWin.size() == 1) {
			myCard = toPlayByMe.get(toPlayByMe.size() - 1);
		}
		// Unentschieden in der Vorrunde?
		else if (oppLastCard == myLastCard) {
			
			// Summe der Karten bei Unentschieden = 0?
			if (sumDraw == 0) {
				// Addiere die letzte Karte + die jetztige zu gewinnende um die Summe zu erhalten
				sumDraw = lastCard + nextCard;
			} else {
				// Addiere die bereits vorhandene Summe + die jetztige zu gewinnende um die Summe zu erhalten
				sumDraw = sumDraw + nextCard;
			}

			// Ist die Summe der Karten bei Unentschieden > 5?
			if (sumDraw >= 5) {
				// Spiele die H�chste
				myCard = toPlayByMe.get(toPlayByMe.size() - 1);
			} 
		}

		// Wurde bei Unentschieden keine Karte gelegt?
		if (myCard == -99) {
			// Bei Karten aus dem Bereich -2 bis 2 spiele die niedrigste Karte
			if (nextCard >= -2 && nextCard <= 2) {
				myCard = erstesDrittel.get(0);
			// Bei Karten aus dem Bereich -5 bis -3...
			} else if (nextCard >= -5 && nextCard <= -3) {
				// falls das zweite Drittel nicht leer ist, lege h�chste aus diesem
				if(!zweitesDrittel.isEmpty()) {
					myCard = zweitesDrittel.get(zweitesDrittel.size() -1);
				// falls drittes Drittel nicht leer ist, niedrigste aus diesem
				} else if (!drittesDrittel.isEmpty()) {
					myCard = drittesDrittel.get(0);
				// ansonsten h�chste aus erstem Drittel
				} else {
					count = (int) (erstesDrittel.size());
					myCard = erstesDrittel.get(erstesDrittel.size() -1);	
				}
			// alle zu gewinnenden Karten > 2
			} else {
				// Ist die zu gewinnende Karte aus der ersten H�lfte?
				if (ersteHaelfte.contains(nextCard)) {
					// Ist der Mittelwert des Gegners gr��er als 8 oder das zweite Drittel unserer Karten leer?
					if (calculateOppAvg() > 8 || zweitesDrittel.isEmpty()) {
						// spiele eine zuf�llige Karte aus dem ersten Drittel
						count = (int) (erstesDrittel.size());
						myCard = erstesDrittel.get((int) (Math.random() * count));
					} else {
						// spiele eine zuf�llige Karte aus dem zweiten Drittel
						count = (int) (zweitesDrittel.size());
						myCard = zweitesDrittel.get((int) (Math.random() * count));
					}
				// Ist die zu gewinnende Karte aus der zweiten H�lfte?
				} else if (zweiteHaelfte.contains(nextCard)) {
					// Ist die Karte des Gegners gr��er als unsere h�chste?
					if (toPlayByOpponent.get(toPlayByOpponent.size() - 1) > toPlayByMe.get(toPlayByMe.size() - 1)) {
						// Ist das zweite Drittel leer, spiele eine zuf�llige Karte aus dem ersten
						if (zweitesDrittel.isEmpty()) {
							count = (int) (erstesDrittel.size());
							myCard = erstesDrittel.get((int) (Math.random() * count));
						// ansosnten spiele zuf�llige aus dem zweiten Drittel
						} else {
							count = (int) (zweitesDrittel.size());
							myCard = zweitesDrittel.get((int) (Math.random() * count));
						}
					// ansonsten spiele unsere H�chste
					} else {
						myCard = toPlayByMe.get(toPlayByMe.size() - 1);
					}
				}
			}
		}
		
		// meine gespielte Karte aus den verbleibenden l�schen
		toPlayByMe.remove(toPlayByMe.indexOf(myCard));
		// die Geier/Mauskarte l�schen
		toWin.remove(toWin.indexOf(nextCard));
		
		// setzte lastCard f�r Berechnung/Erkennung von Unentschieden in der n�chsten Runde
		lastCard = nextCard;
		myLastCard = myCard;

		// gebe unsere gespielte Karte zur�ck
		return myCard;
	}

	// Mittelwertberechnung der verbleibenden Karten des Gegners
	public double calculateOppAvg() {
		double sum = 0;
		for (int i = 0; i < toPlayByOpponent.size(); i++) {
			sum = sum + toPlayByOpponent.get(i);
		}
		sum = sum / toPlayByOpponent.size();

		return sum;
	}
}