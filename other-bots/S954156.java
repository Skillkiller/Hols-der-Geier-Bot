/** Hols der Geier Strategie Alpato
*** geschrieben von
*** Alexander Mack, Paul Lampel, Tobias Kurz
**/


import java.util.ArrayList;

public class S954156 extends HolsDerGeierSpieler {

	// Kontruktor
	public S954156() {
		//stStatus wird beim erzeugen des Objects per Random festgelegt 
		this.stStatus = (int)Math.round(Math.random());
		// je nach Status wird das Array gefüllt.
		// ( [Position] Geierkartenwert / Spielkarte) 
		// [14]10/x, [13]9/x, [12]7/x, ..., [4]-5/x, [0]-1/x
		schreibeArray();
		this.zug = 1;
		this.letztePunkteKarte = 0;
		this.targetValue = 0;
	}

	// Variablen zur Speicherung der Zwischenergebnisse - Klassenvariablen
	int zug;
	int letztePunkteKarte;
	int stStatus;
	int targetValue;
	int[] cardPool = new int[15];
	ArrayList<Integer> ownCards = new ArrayList<Integer>();
	ArrayList<Integer> enemyCards = new ArrayList<Integer>();
	ArrayList<Integer> pointCards = new ArrayList<Integer>();

	// Methode zum Zurücksetzen der gespeicherten Informationen zum
	// letzten/aktuellen Spiel
	public void reset() {
		//Strategie-Switch - Wechselt von einer zur anderen Strategie
		if (enemyCards.size() != 0){	
			if (berechneErfolg() == false){
				if (stStatus == 0) 
					stStatus = 1;
				else if (stStatus == 1) 
					stStatus = 0;
			}
		}
		schreibeArray();
		this.zug = 1;
		this.ownCards.clear();
		this.enemyCards.clear();
		this.pointCards.clear();
		this.letztePunkteKarte = 0;
		this.targetValue = 0;
	}

	// Methode zum Ausgeben der nächsten zu spielenden Karte
	public int gibKarte(int naechsteKarte){
		// Variablen für Methodendurchlauf
		int gibKarte = 0; // Wert für die Rückgabe (zu legende Karte)
		int pot; // Wert der Punkte, um die gespielt wird (aktuelle Karte oder Summe mehrerer Karten bei Stich)
		boolean stich = false; // Hilfsvariable true, wenn letzte Runde ein Stich war (Vgl. letzte ownCards mit letzter enemyCards)

		if (zug > 1) {
			enemyCards.add(letzterZug());
			if (enemyCards.get(zug - 2) == ownCards.get(zug - 2))
				stich = true;
			// else stich = false;
		}

		switch (stStatus) { // Wenn stStatus ..... dann Strategie 1 oder 0 ...

		case 0: { //
			if (stich == true) {
				pot = pointCards.get(zug-2) + naechsteKarte;
				pointCards.add(pot);
				if (letztePunkteKarte <= 8 && letztePunkteKarte >= 1) {
					if (letztePunkteKarte < naechsteKarte && naechsteKarte <= 8
							&& naechsteKarte >= 1)
						targetValue = naechsteKarte;
					else
						targetValue = letztePunkteKarte;
					gibKarte = findeKarteT(targetValue + 4, 13, 1);
					break; //
				}
				/**else if (naechsteKarte <= 8 && naechsteKarte >= 1){
					gibKarte = findeKarteT(naechsteKarte + 4, 13, 1);
					break;
				}**/
			}
			else
				pot = naechsteKarte;
			
			pointCards.add(pot);
			if (naechsteKarte <= 8 && naechsteKarte >= 1)
				gibKarte = findeKarteT(naechsteKarte + 4, 13, 1);
			else if (naechsteKarte < 0)
				gibKarte = findeKarteT((naechsteKarte * (-1)) - 1, -1, -1);
			else
				gibKarte = findeKarteKT(naechsteKarte + 4, 15, 1);

			letztePunkteKarte = naechsteKarte;
			break;
		}

		default: { // Basisstrategie (Strategie 1) wenn erfolgreich
			if (stich == true)
				pot = pointCards.get(zug - 2) + naechsteKarte;
			else
				pot = naechsteKarte;

			pointCards.add(pot);

			if (pot > 10 || pot < -5) { // dann nimm größte Karte
				gibKarte = findeKarte(cardPool.length - 1, -1, -1);

			} else if (pot == 0) { // dannn nimm niedrigste Karte
				gibKarte = findeKarte(0, cardPool.length, 1);
			} else if (pot >= -5 && pot <= 10) { // dann nimm ...
				if (pot < 0) { // ... Kartenwertigkeit oder die nächst
								// niedrigere Karte
					gibKarte = findeKarte((pot * (-1)) - 1, 15, +1);
				} else { // ... Kartenwertigkeit oder die nächst höhere Karte
					gibKarte = findeKarte(pot + 4, -1, -1);
				}
			}
			break;
		}
		}
		ownCards.add(gibKarte);
		zug++;
		return gibKarte;
	}
	
	//Methode zum Füllen unseres KartenPools-Arrays je nach Strategie
	private void schreibeArray(){
		if (stStatus == 0) {
			// Arrayaufbau : [14]10/2, [13]9/1, [12]8/15, [11]7/14, ..., [1]-2/4, [0]-1/3
			for (int i = 1; i <= 13; i++)
				cardPool[i - 1] = i + 2;
			for (int i = 14; i <= 15; i++)
				cardPool[i - 1] = i - 13;
		}
		else if (stStatus == 1){
			// Arrayaufbau : [14]10/15, [13]9/14, ..., [1]-2/2, [0]-1/1
			for (int i = 1; i <= 15; i++)
				cardPool[i - 1] = i;
		}
	}

	// sucht die Wertzugehörige oder nächste verfügbare Karte für Strategie
	// default
	private int findeKarte(int start, int ende, int richtung) {
		int card = 0;
		for (int i = start; i != ende; i = i + richtung) {
			if (cardPool[i] != 0) {
				card = cardPool[i];
				cardPool[i] = 0;
				break;
			}
		}
		if (card == 0) {
			if (ende == 15)
				ende = -1;
			else
				ende = 15;
			for (int i = start; i != ende; i = i + richtung * (-1)) {
				if (cardPool[i] != 0) {
					card = cardPool[i];
					cardPool[i] = 0;
					break;
				}
			}
		}
		return card;
	}

	// sucht die Wertzugehörige oder nächst kleinere Karte im Array für
	// Strategie 0
	private int findeKarteT(int start, int ende, int richtung) {
		int card = 0;
		for (int i = start; i != ende; i = i + richtung) {
			if (cardPool[i] != 0) {
				card = cardPool[i];
				cardPool[i] = 0;
				break;
			}
		}
		if (card == 0) {
			if (ende == 13)
				ende = -1;
			else
				ende = 13;
			for (int i = start; i != ende; i = i + richtung * (-1)) {
				if (cardPool[i] != 0) {
					card = cardPool[i];
					cardPool[i] = 0;
					break;
				}
			}
		}
		if (card == 0) card = findeKarte(start, 15, 1);
		return card;
	}

	// sucht die Wertzugehörige oder kleinste verfügbare Karte für Strategie 0
	private int findeKarteKT(int start, int ende, int richtung) {
		int card = 0;
		for (int i = start; i != ende; i = i + richtung) {
			if (cardPool[i] != 0) {
				card = cardPool[i];
				cardPool[i] = 0;
				break;
			}
		}
		if (card == 0) {
			ende = start + 1;
			start = 0;
			for (int i = start; i != ende; i = i + richtung) {
				if (cardPool[i] != 0) {
					card = cardPool[i];
					cardPool[i] = 0;
					break;
				}
			}
		}
		return card;
	}
	
	//Methode zum Berechnen des Erfolgs oder Miserfolgs der letzten Runde
	public boolean berechneErfolg(){
		ArrayList<Integer> enemyPool = new ArrayList<Integer>();
		for (int i=1; i<=15; i++)
			enemyPool.add(i);
		int gesamtPunkte = 0;
		for (int i=0; i!=enemyCards.size(); i++){
			if (pointCards.get(i) < 0){ 
				if (ownCards.get(i)<enemyCards.get(i)) 
					gesamtPunkte = gesamtPunkte + pointCards.get(i);
			}
			else { 
				if (ownCards.get(i)>enemyCards.get(i)) 
					gesamtPunkte = gesamtPunkte + pointCards.get(i);
			}
			for (int y=0; y!=enemyPool.size(); y++)
				if (enemyPool.get(y) == enemyCards.get(i)){
					enemyPool.remove(y);
					break;
				} 
		}
		if (pointCards.get(pointCards.size()-1) < 0){
			if (ownCards.get(ownCards.size()-1) < enemyPool.get(enemyPool.size()-1))
				gesamtPunkte = gesamtPunkte + pointCards.get(pointCards.size()-1);
		}
		else{
			if (ownCards.get(ownCards.size()-1) > enemyPool.get(enemyPool.size()-1))
				gesamtPunkte = gesamtPunkte + pointCards.get(pointCards.size()-1);
			
		}
		if (gesamtPunkte >= 21) return true;
		else return false;
	}

}
