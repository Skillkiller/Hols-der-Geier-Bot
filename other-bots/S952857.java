
// Gruppe: Christopher Teutsch / Florian Fuhr / Randy Henrich / Julian Brucherseifer



import java.util.ArrayList;

public class S952857 extends HolsDerGeierSpieler {

	//2dimensionales ArrayList fuer die KartenPools
	private ArrayList<ArrayList<Integer>> stapel = new ArrayList<ArrayList<Integer>>();

	private ArrayList<Integer> gegnerKarten = new ArrayList<Integer>();
	private ArrayList<Integer> geierKarten = new ArrayList<Integer>();
	private ArrayList<Integer> playedCards = new ArrayList<Integer>();

	//Variablendeklaration mit Anfangsinitialisation
	private int punkte = 0;
	private int punkteRechnen = 0;
	private boolean firstRound = true;
	
	public S952857() {
		reset();
	}

	//Erzeugen der Kartenpools und anschliessendes Fuellen
	private void FillArrays() {
		for (int i = 0; i < 5; i++) {
			stapel.add(new ArrayList<Integer>());
		}
		for (int i = 0; i < 3; i++) {
			stapel.get(0).add(i + 1);
			stapel.get(1).add(i + 4);
			stapel.get(2).add(i + 7);
			stapel.get(3).add(i + 10);
			stapel.get(4).add(i + 13);
		}
	}

	//Leeren aller ArrayLists und neu fuellen, erneutes Initialisieren von Globalen Varbiablen
	public void reset() {
		stapel.clear();
		playedCards.clear();
		geierKarten.clear();
		gegnerKarten.clear();
		FillArrays();
		punkteRechnen = 0;
		firstRound = true;
		super.getNummer();
	}
	
	//geerbte Methode mit Ueberpruefung auf erste Spielrunde
	public int gibKarte(int naechsteKarte) {
		geierKarten.add(naechsteKarte);
		if (!firstRound) {
			addCards();
			naechsteKarte = Gleichstand();
		}
		int unsereKarte = playCard(naechsteKarte);
		firstRound = false;
		return unsereKarte;
	}

	//Auswahl des zu spielenden Kartenpools
	private int playCard(int naechsteKarte) {
		if (naechsteKarte > 7 || naechsteKarte < -5) {
			return playPool(naechsteKarte, 4);
		}
		if (naechsteKarte >= 5 && naechsteKarte < 8) {
			return playPool(naechsteKarte, 3);
		}
		if (naechsteKarte < -2 && naechsteKarte > -6) {
			return playPool(naechsteKarte, 2);
		}
		if (naechsteKarte >= -2 && naechsteKarte < 2) {
			return playPool(naechsteKarte, 0);
		}
		if (naechsteKarte >= 2 && naechsteKarte < 5) {
			return playPool(naechsteKarte, 1);
		}
		return 0;
	}

	//erneuter Aufruf der (ueberladenen) Methode
	private int playPool(int naechsteKarte, int index) {
		return playPool(naechsteKarte, index, index);
	}

	//Methode zur Auawahl der gespielten Karte
	//Doppelter Index wird benoetigt damit der Kartenpool korrekt gewechselt werden kann
	//Abfangen von Sonderfaellen um zu verhindern das hohe Karten verschenkt werden
	private int playPool(int naechsteKarte, int index, int oldIndex) {
		if (stapel.get(index).isEmpty()) {
			return playPool(naechsteKarte, changePool(index, oldIndex), index);
		}
		int unsereKarte = stapel.get(index).get(stapel.get(index).size() - 1);
		if (naechsteKarte == 10 || naechsteKarte == 7 || naechsteKarte == 4) {
			if (!gegnerKarten.contains(unsereKarte)) {
				return lowestCard();
			}
		}
		stapel.get(index).remove(stapel.get(index).size() - 1);
		return unsereKarte;
	}

	//Abfragen eines Gleichstands durch Vergleich der zuletzt gelegten Karten 
	//mit Zwischenspeichern der Punkte fuer den Fall von mehreren Gleichstaenden hintereinander
	private int Gleichstand() {	
		if (playedCards.get(playedCards.size() - 1) == gegnerKarten
				.get(gegnerKarten.size() - 1)) {
			punkte = punkte + geierKarten.get(geierKarten.size() - 2);
			punkteRechnen = punkte + geierKarten.get(geierKarten.size() - 1);
		} else {
			punkte = 0;
			punkteRechnen = geierKarten.get(geierKarten.size() - 1);
		}
		return punkteRechnen;
	}

	//Hinzufuegen der Karten in die Arrays mit Ueberpruefung auf Spielernummer
	private void addCards() {
		playedCards.add(getHdg().letzterZug(getNummer()));
		gegnerKarten.add(getHdg().letzterZug(getNummer() == 1 ? 0 : 1));
	}
	
	//Suchen der niedrigsten Karten in allen Pools und anschliessendes Loeschen der Karte
	private int lowestCard() {
		int card = 15;
		for (int i = 0; i < stapel.size(); i++) {
			if (!stapel.get(i).isEmpty()) {
				if (stapel.get(i).get(0) < card)
					card = stapel.get(i).get(0);
			}
		}
		for (int i = 0; i < stapel.size(); i++) {
			if (stapel.get(i).contains(card))
				stapel.get(i).remove(new Integer(card));
		}
		return card;
	}

	//Wechsel des Pools in die richtige Richtung durch Vergleich der Pool-Indexe
	private int changePool(int index, int oldIndex) {
		if ((index > oldIndex || index == oldIndex) && index < 4) {
			index++;
		} else {
			index--;
		}
		return index;
	}
}