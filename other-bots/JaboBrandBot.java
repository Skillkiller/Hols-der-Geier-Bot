

import java.util.ArrayList;

public class JaboBrandBot extends HolsDerGeierSpieler{
	
	public ArrayList<Integer> nochNichtGespielt=new ArrayList<Integer>();
	private int zufallskarte;
	// Taktiken: 1-3 = JaboBrand-Taktiken, 4 = Zufall
	private int taktik = 2;
	private int[] gegnerVariante = new int[3];
	private ArrayList<Integer> KartenMitte=new ArrayList<Integer>();
	private ArrayList<Integer> KartenGegner=new ArrayList<Integer>();
	
	public void reset(){	
		//Auswertung Gegnerstrategie
		if (KartenMitte.size() != 0){
			auswertenGegnerTaktik();
			//System.out.println("########################   1: " + gegnerVariante[0] + "    2: " + gegnerVariante[1] + "    3: " + gegnerVariante[2]);
		}
		
		// ArrayLists leeren, bzw. initialisieren
		nochNichtGespielt.clear();
		for (int i=15; i>0; i--)            
            nochNichtGespielt.add(i);
        KartenMitte.clear();
        KartenGegner.clear();
		
        // Methode aendereTaktik aufrufen
		if (nochNichtGespielt.size() != 0){
			aendereTaktik();
		}
             
		// Array zur Ermittlung der Gegner-Variante zurücksetzen
        for (int i = 0; i < 3; i++){
        	gegnerVariante[i] = 0;
        }      
	}
	
	// Methode zum Ändern der Taktik
	private void aendereTaktik(){
		if ((gegnerVariante[0] == 5)){
			this.taktik = 2;
			//System.out.println(":::::::::::::::::::::::::::::::::::::::::::::::::::::Taktik = 2");
		}
		if ((gegnerVariante[1] == 5)){
			this.taktik = 3;
			//System.out.println(":::::::::::::::::::::::::::::::::::::::::::::::::::::Taktik = 3");
		}
		if ((gegnerVariante[2] == 5)){
			this.taktik = 4;
			//System.out.println(":::::::::::::::::::::::::::::::::::::::::::::::::::::Taktik = 4");
		}
	}
	
	// Methode zum Legen einer Karte
	public int gibKarte(int naechsteKarte){
		if (nochNichtGespielt.size() == 15){
			aendereTaktik();
		}
		int karte = 0;
		
		// Geier-Karten merken
		if (KartenMitte.size() < 16){
			KartenMitte.add(naechsteKarte);
		}
		
		// Gegner-Karten merken
		if (getGegnerLetzterZug() != -1001){
			KartenGegner.add(getGegnerLetzterZug());
		}
		
		// zuletzt gespielte Gegner-Karte ermitteln
		if (KartenGegner.size() == 14){
			for (int i = 1; i < 16; i++){
				if (!KartenGegner.contains(i)){
					KartenGegner.add(i);
				}
			}
		}
		
		// Eigene Taktik auswählen
		
		if (taktik <= 3){
		
		switch(naechsteKarte){
		
			case -5:
				switch(taktik){
				case 1:
					karte = waehleKarte(10); break;
				case 2:
					karte = waehleKarte(11); break;
				case 3:
					karte = waehleKarte(12); break;
				} break;
			
			case -4:
				switch(taktik){
				case 1:
					karte = waehleKarte(8); break;
				case 2:
					karte = waehleKarte(9); break;
				case 3:
					karte = waehleKarte(10); break;
				} break;
			
			case -3:
				switch(taktik){
				case 1:
					karte = waehleKarte(6); break;
				case 2:
					karte = waehleKarte(7); break;
				case 3:
					karte = waehleKarte(8); break;
				} break;
			
			case -2:
				switch(taktik){
				case 1:
					karte = waehleKarte(4); break;
				case 2:
					karte = waehleKarte(5); break;
				case 3:
					karte = waehleKarte(6); break;
				} break;
			
			case -1:	
				switch(taktik){
				case 1:
					karte = waehleKarte(2); break;
				case 2:
					karte = waehleKarte(3); break;
				case 3:
					karte = waehleKarte(4); break;
				} break;
			
			case 1:
				switch(taktik){
				case 1:
					karte = waehleKarte(2); break;
				case 2:
					karte = waehleKarte(3); break;
				case 3:
					karte = waehleKarte(4); break;
				} break;
			
			case 2:
				switch(taktik){
				case 1:
					karte = waehleKarte(4); break;
				case 2:
					karte = waehleKarte(5); break;
				case 3:
					karte = waehleKarte(6); break;
				} break;
			
			case 3:
				switch(taktik){
				case 1:
					karte = waehleKarte(6); break;
				case 2:
					karte = waehleKarte(7); break;
				case 3:
					karte = waehleKarte(8); break;
				} break;
			
			case 4:
				switch(taktik){
				case 1:
					karte = waehleKarte(8); break;
				case 2:
					karte = waehleKarte(9); break;
				case 3:
					karte = waehleKarte(10); break;
				} break;
			
			case 5:
				switch(taktik){
				case 1:
					karte = waehleKarte(10); break;
				case 2:
					karte = waehleKarte(11); break;
				case 3:
					karte = waehleKarte(12); break;
				} break;
			
			case 6:
				switch(taktik){
				case 1:
					karte = 12; break;
				case 2:
					karte = 13; break;
				case 3:
					karte = 14; break;
				} break;
			
			case 7:
				switch(taktik){
				case 1:
					karte = 13; break;
				case 2:
					karte = 14; break;
				case 3:
					karte = 15; break;
				} break;
			
			case 8:
				switch(taktik){
				case 1:
					karte = 14; break;
				case 2:
					karte = 15; break;
				case 3:
					karte = 1; break;
				} break;
			
			case 9:
				switch(taktik){
				case 1:
					karte = 15; break;
				case 2:
					karte = 1; break;
				case 3:
					karte = 2; break;
				} break;
			
			case 10:
				switch(taktik){
				case 1:
					karte = 1; break;
				case 2:
					karte = 2; break;
				case 3:
					karte = 3; break;
				} break;
			
			}
		}
		
		// Zufallskarte ermitteln
		if (taktik == 4){
			karte = gibKarteZufall(1);
		}
	
		//System.out.println(taktik);
		return karte;
		
	}
	
	// Methode zum Ermitteln einer zufälligen Karte
	public int gibKarteZufall(int naechsteKarte){
		boolean karteGefunden = false;
		while (! karteGefunden){
			zufallskarte = (int)(Math.random()*15)+1;
			for (int i: nochNichtGespielt){
				if (i == zufallskarte){
					karteGefunden = true;
				}
			}
		}
		nochNichtGespielt.remove(nochNichtGespielt.indexOf(zufallskarte));
		return zufallskarte;
	}
	
	// zu spielende Karte aus den zwei Möglichkeiten auswählen
	private int waehleKarte(int karte1){
		boolean karteGefunden = false;
		while (! karteGefunden) {
			zufallskarte = (int)(Math.random()*2)+karte1;
			for (int i: nochNichtGespielt)
			{
				if (i == zufallskarte){
					karteGefunden = true;
				}
			}
		} 
		nochNichtGespielt.remove(nochNichtGespielt.indexOf(zufallskarte));
		
		return zufallskarte;
	}
	
	// letzten Zug des Gegners ermitteln
	private int getGegnerLetzterZug(){
		return getHdg().letzterZug(getNummer() == 1 ? 0 : 1);
	}
	
	// Methode zum Auswerten der Gegner-Taktik
	private void auswertenGegnerTaktik(){
		int counter = 0;
		for (int i: KartenMitte){
			switch (i){
			
			case 6:
				switch (KartenGegner.get(counter)){
				case 12:
					gegnerVariante[0]++; break;
				case 13:
					gegnerVariante[1]++; break;
				case 14:
					gegnerVariante[2]++; break;
				} break;
				
			case 7:
				switch (KartenGegner.get(counter)){
				case 13:
					gegnerVariante[0]++; break;
				case 14:
					gegnerVariante[1]++; break;
				case 15:
					gegnerVariante[2]++; break;		
				} break;
			
			case 8:
				switch (KartenGegner.get(counter)){
				case 14:
					gegnerVariante[0]++; break;
				case 15:
					gegnerVariante[1]++; break;
				case 1:
					gegnerVariante[2]++; break;
				} break;
			
			case 9:
				switch (KartenGegner.get(counter)){
				case 15:
					gegnerVariante[0]++; break;
				case 1:
					gegnerVariante[1]++; break;
				case 2:
					gegnerVariante[2]++; break;
				} break;
				
			case 10:
				switch (KartenGegner.get(counter)){
				case 1:
					gegnerVariante[0]++; break;
				case 2:
					gegnerVariante[1]++; break;
				case 3:
					gegnerVariante[2]++; break;
				} break;
				
			}
			counter++;
		}
	}
	
}
