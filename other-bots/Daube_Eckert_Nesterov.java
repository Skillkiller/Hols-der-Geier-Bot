// Package Player einbinden


// ArrayLists einbinden
import java.util.ArrayList;

// Start Klasse 
public class Daube_Eckert_Nesterov extends HolsDerGeierSpieler {

	// Initialisieren der Klassen-Variablen
	private ArrayList<Integer> spielKartenGespielt=new ArrayList<Integer>();
	private ArrayList<Integer> meineKarten=new ArrayList<Integer>();
	private ArrayList<Integer> zufall=new ArrayList<Integer>();

	// Reset
	public void reset(){
		// Leere die ArrayListen
		spielKartenGespielt.clear();
		meineKarten.clear();
		zufall.clear();
		// belege meine Karten 
		for (int i=1;i<=15;i++){
			meineKarten.add(i);
		}
		// belege den Zufallsarray
		for (int i=1;i<=7;i++){
			zufall.add(i);
		}
	}

	// GibKarte
	public int gibKarte(int naechsteKarte){

		// Initilisierung der Variablen
		int spieleKarte = -99;
		double zufallsZahl;
		int zufallsBereich;
		boolean leer = false;

		// Kartenauswahls
		switch (naechsteKarte){

		case 10:
			if (meineKarten.contains(15)){
				// Bei der 10 spiele die 15
				spieleKarte = 15;
			}
			else{
				leer = true;
			}
			break;
		case -5:
			if (meineKarten.contains(10)){
				// Bei der -5 spiele die 10
				spieleKarte = 10;
			}
			else{
				leer = true;
			}
			break;
		case -4:
			if (meineKarten.contains(9)){
				// bei der -4 spiele die 9
				spieleKarte = 9;
			}
			else{
				leer = true;
			}
			break;
		case 2:
			if (meineKarten.contains(8)){
				// bei der 2 spiele die 8
				spieleKarte = 8;
			}
			else{
				leer = true;
			}
			break;
		case 4:
		case 5:
		case 6:
			// Bei 4,5,6 spiele eine Zufallszahl von 11-13
			double zufallsZahlHoch;
			if(meineKarten.contains(11)||meineKarten.contains(12)||meineKarten.contains(13)){
				// 0 <= Zufahlszahl < 3
				do{
					zufallsZahlHoch = Math.random()*3;
					// addiere 11 zu der Zufallszahl
					spieleKarte = (int) zufallsZahlHoch + 11;
				}
				while(!meineKarten.contains(spieleKarte));
			}
			else{
				leer = true;
			}
			break;
		default:
			leer = true;
		}
		// Switch-Zahl nicht vorhanden
		if(leer)
		{
			// große Karte sicher gewinnen
			if (naechsteKarte > 7 && !(meineKarten.contains(15)) ){
				spieleKarte = meineKarten.get(meineKarten.size()-1);
			}

			// Unentschieden
			else if (meineKarten.size() < 15 && getHdg().letzterZug(0) == getHdg().letzterZug(1)
					&& (naechsteKarte + 
							spielKartenGespielt.get(spielKartenGespielt.size()-1)) > 12 ){
				spieleKarte = meineKarten.get(meineKarten.size()-1);
			}
			// Zufallskarte
			else if (zufall.size() != 0 ){
				do{
					// ermittle eine Zufallszahl aus der ArrayList-Größe 
					zufallsZahl = Math.random()*zufall.size();
					// Zufallszahl gerade machen
					zufallsBereich = (int) zufallsZahl;
					// hole dir die Zahl an der Stelle der Zufallszahl
					spieleKarte = zufall.get(zufallsBereich);
				}
				// Ist diese Karte noch in unserem Deck?
				while(!zufall.contains(spieleKarte));
			}
			// Hohe Karte 
			else{
				spieleKarte = meineKarten.get(meineKarten.size()-1);
			}
		}
		// Wurde schon eine Karte gespielt?
		if (spieleKarte > 0){
			// lösche diese aus unserem Deck
			meineKarten.remove(meineKarten.indexOf(spieleKarte));
			// speichere die gespielte Karte
			spielKartenGespielt.add(naechsteKarte);
			// In ZufallsArray enthalten?
			if (spieleKarte < 8)
				// lösche aus ZufallsArray
				zufall.remove(zufall.indexOf(spieleKarte));
		}
		// spiele die Karte
		return spieleKarte;
	}
}