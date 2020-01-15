

import java.util.ArrayList;


public class AltenhofSpiessBot extends HolsDerGeierSpieler{
	private Integer memUnsereZuege=0;
	private Integer memLetztePunkteKarte=0;
    private ArrayList<Integer> memSpiel=new ArrayList<Integer>();
    private ArrayList<Integer> memGegner=new ArrayList<Integer>();
    private ArrayList<Integer> memEigene=new ArrayList<Integer>();
    private ArrayList<Integer> memEigeneHoch=new ArrayList<Integer>();
    private ArrayList<Integer> memEigeneMittel=new ArrayList<Integer>();
    private ArrayList<Integer> memEigeneNiedrig=new ArrayList<Integer>();
    
    // Hilfsinformationen zum Debugen (JA/NEIN):
    private boolean errlog=false;
    
    /*     
    Festlegen der Grenzwerte für die Strategien:
    Hoch-Strategie: spielt Strategie wenn Punktekarte größer als grenzSpielKarten[0]
    Mittel-Strategie: spielt Strategie wenn Punktekarte zwischen grenzSpielKarten[0] und 1
    Geier-Strategie: spielt Strategie bei allen Geierkarten 
    				 Wenn Geierkarte kleiner ist als grenzPunkteKarten[1] --> spielt Karten aus mittlerem Bereich
    				 Wenn Geierkarte größer gleich grenzPunkteKarten[1] --> spielt Karten aus niederem Bereich
    */
    private int[] grenzPunkteKarten = {7, -2};
    
    
    /*
    Festlegen des Spielkartenraums:
    Hoher Spielkartenraum =  von 15 bis exklusiv grenzSpielKarten[0]
    Mittlerer Spielkartenraum = Von inklusiv grenzSpielKarten[0] bis  exklusiv grenzSpielKarten[1]
    Niederer Spielkartenraum = Von inklusiv grenzSpielKarten[1] bis 1
    Taktik bewährt mit folgenden Werten: (7,-2) (12,6) (vs. Fredare); (7,-2) (9,4) (vs. FloRon)  
    */
    private int[] grenzSpielKarten = {9, 4};
    
    public AltenhofSpiessBot() {
    	reset();
    }
    
    public void reset() {
        memSpiel.clear();
        memGegner.clear();
        memEigene.clear();
        memEigeneNiedrig.clear();
        memEigeneMittel.clear();
        memEigeneHoch.clear();
        
        for (int i=10;i>-6;i--)
            memSpiel.add(i);
        for (int i=15;i>0;i--){
            memGegner.add(i);           
            memEigene.add(i);  
        }
        for (int i=15;i>grenzSpielKarten[0];i--)
        	memEigeneHoch.add(i);
        for (int i=grenzSpielKarten[0];i>grenzSpielKarten[1];i--)
        	memEigeneMittel.add(i);
        for (int i=grenzSpielKarten[1];i>0;i--)
        	memEigeneNiedrig.add(i);
    }
    
    private int zufallsKarte(int min, int max) {
    //  Eine Zufallszahl zwischen max und min:       
        int erg = (int)  ((Math.random()* (max-min))+ min);
        return erg;
    };
        
    

    private int hochStrategie() {
	    int erg = 0;
	    int hKarte = 0; // Rückgabe Karte
	    
		   // Wenn meine höchste Karte größer ist als die des Gegners:
	    if (memEigene.get(0)>memGegner.get(0)) {
	    	if (memEigene.size() > 1){
		    	while (memEigene.get(hKarte) > memGegner.get(0)){
		    		hKarte++;
		    		if ((memEigene.size()-1) == hKarte) {
		    			break;
		    		}
		    	}
	    	}
	    	
	    	// gehe zur Karte zurück die höher war als die höchste des Gegners
	    	if (hKarte > 0) hKarte--;
	 
	    	fehlerLog("ErrorID: 11");
	    	erg = memEigene.get(hKarte);
	    
	    // Wenn die höchste Gegnerkarte gleich meiner höchsten Karte ist
	    } else if (memEigene.get(0)== memGegner.get(0)) {
	    	fehlerLog("ErrorID: 12");
	    	erg = memEigene.get(0);
	    	
	    // Wenn Gegner die höhere Karte hat
	    } else { // spiele höchste Karte im MittlerenBereich
	    	if (memEigeneMittel.size() > 0) {
	    		fehlerLog("ErrorID: 13");
	    		erg = memEigeneMittel.get(0);
	    	} else {
	    		erg = this.ausnahme(4);
	    	}
	    }	    
	    return erg;
    }
    
    private int mittelStrategie() {
	    int erg = 0;
	    int hKarte = 0; // Rückgabe Karte
	    
		   // Wenn meine höchste Karte des mittleren Sektors größer ist als die höchste Karte des Gegners:
	    if (memEigeneMittel.size() > 0 && memEigeneMittel.get(0)>memGegner.get(0)) {
	    	if(memEigeneMittel.size() > 1) {
		    	while (memEigeneMittel.get(hKarte) > memGegner.get(0)){
		    		hKarte++;
		    		if ((memEigeneMittel.size()-1) == hKarte) {
		    			break;
		    		}
		    	}
	    	}
	    	
	    	// gehe zur Karte zurück die höher war als die höchste des Gegners
	    	if (hKarte > 0) hKarte--;
	    	
	    	fehlerLog("ErrorID: 14");
	    	
	    	erg = memEigeneMittel.get(hKarte);
	    	
	    	/* Wenn die höchste Karte des mittleren Spielraums größer als die höchste Karte des Gegners ist
	    	   spiele aus dem mittleren Bereich die höchste Karte */
	    } else if (memEigeneMittel.size() > 0 && memEigeneMittel.get(0)== memGegner.get(0)) {
	    	fehlerLog("ErrorID: 15");
	    	erg = memEigeneMittel.get(0);
	    } else {
	    	// Wenn Gegner die höhere Karte hat spiele aus dem niederen Bereich die höchste Karte
	    	if (memEigeneNiedrig.size() > 0) {
	    		fehlerLog("ErrorID: 16");
	    		erg = memEigeneNiedrig.get(0);
	    	} else {
	    		erg = this.ausnahme(5);
	    	} 
	    }	    
	    return erg;
    }
    
    private int geierStrategie(int naechsteKarte) {
    int erg = 0;
 	   // Geierkarten
 	   if (naechsteKarte<grenzPunkteKarten[1]) {
		   	   // viele Minuspunkte: 
 		   if(memEigeneMittel.size()>0)	{
 			   fehlerLog("ErrorID: 17");
 			   erg = memEigeneMittel.get(zufallsKarte(0, memEigeneMittel.size()));
 		   } else {
 			   erg = this.ausnahme(2);
 		   }
 		   
 	   } else {
		   	   // wenig Minuspunkte    		   
 		   if(memEigeneNiedrig.size()>0)	{
 			   fehlerLog("ErrorID: 18");
 			   erg = memEigeneNiedrig.get(zufallsKarte(0, memEigeneNiedrig.size()));
 		   } else {
 			   erg = this.ausnahme(1);
 		   }
 		   
 	   }  
 	   return erg;
    }
    
    public int gibKarte(int naechsteKarte) {
       int ret=-99;
       int letzeKarteGegner = letzterZug();
       int letzteKarteGegner=letzeKarteGegner;
       
       if (letzteKarteGegner!=-99)
          memGegner.remove(memGegner.indexOf(letzteKarteGegner));
       // Lösche diese Karte
       memSpiel.remove(memSpiel.indexOf(naechsteKarte));
       
       // Wenn unentschieden gespielt wurde:
   
       if(letzeKarteGegner == memUnsereZuege){	       	   
    	   naechsteKarte += memLetztePunkteKarte;
       } 
    	   
  
       // Anfang der Entscheidungen des Bots     
   
       /*memEigene = Stapel meiner noch nicht gespielten Karten
         memGegner = Stapel der vom Gegner noch nicht gelegten Karten
         memSpiel = Verbleibende zu gewinnende Karten im Spiel
         memEigeneHoch = Eigene Karte aus hohen Spielraum
         memEigeneMittel = Eigene Karte aus mittlerem Spielraum
         memEigeneNiedrig = Eigene Karte aus niedrigem Spielraum
         ret = RückgabeKarte
         naechsteKarte = die aktuell zu gewinnende Karte
         memUnsereZuege = Unser letzter Zug
         memNaechsteKarte = zwischenspeichern des naechsteKarte Wertes*/ 
   
       if (naechsteKarte>0) {
    	   // Mäusekarten
    	   if (naechsteKarte>grenzPunkteKarten[0]) {   
    		   ret=hochStrategie();  
    	   } else {
    		   ret=mittelStrategie();  
    	   }	   
       } else {
    	   ret=geierStrategie(naechsteKarte);   	
       }
              
       //
       // Ende der Entscheidungen des Bots
       //
       rundenInfo();
       
       // Aktualisieren der Array Felder
       if(memEigeneHoch.contains(ret)){memEigeneHoch.remove(memEigeneHoch.indexOf(ret));}
       if(memEigeneMittel.contains(ret)){memEigeneMittel.remove(memEigeneMittel.indexOf(ret));}
       if(memEigeneNiedrig.contains(ret)){memEigeneNiedrig.remove(memEigeneNiedrig.indexOf(ret));}
       memEigene.remove(memEigene.indexOf(ret));
       
       memUnsereZuege = ret;
	   memLetztePunkteKarte = naechsteKarte;
       
       return ret;
    }
    
    public void printArray(ArrayList<Integer> dl){
    // Gibt Inhalte eines Arrayfelds aus
    	for(Integer i : dl){
    		System.out.print(i + " | ");
    	}
    	System.out.println();
    }
    
    private void fehlerLog(String errMsg){
    	if(errlog){
    		System.out.println();
    		System.out.println(errMsg);
    		System.out.println();
    	}
    }
  
    private void rundenInfo(){
    	if(errlog){
	      System.out.println();
	      System.out.println("memEigene: ");
	      printArray(memEigene);
	      System.out.println("memEigeneNiedrig: ");
	      printArray(memEigeneNiedrig);
	      System.out.println("memEigeneMittel: ");
	      printArray(memEigeneMittel);
	      System.out.println("memEigeneHoch: ");
	      printArray(memEigeneHoch);
	      System.out.println("memGegner: ");
	      printArray(memGegner);
    	}
    }
    
    private int ausnahme(int start) {
    	int ret=0;
    	//start-parameter (Reihenfolge der Spielraum suche falls leer):
    	//1: Niedrig: - Mittel(höchste) - Alle(höchste)
    	//2: Mittel: - Hoch - Alle
    	//3: Hoch: - Mittel - Alle
    	//4: Mittel: - Niedrig - Alle
    	//5: Niedrig: - Mittel(zufällig) - Alle(zufällig)
    	switch(start) {
		case 1:{ 
			//Falls Kartenraum leer:
				if(memEigeneMittel.size()>0) {
					fehlerLog("ErrorID: 1");
					ret = memEigeneMittel.get(memEigeneMittel.size()-1);
				} else {
			//Falls auch dieser Kartenraum leer:
					fehlerLog("ErrorID: 2");
					ret = memEigene.get(memEigene.size()-1);  
				}
				break;
			  }
		case 2:{
	   //Falls Kartenraum leer:
			   if(memEigeneHoch.size()>0) {
				   fehlerLog("ErrorID: 3");
				   ret = memEigeneHoch.get(memEigeneHoch.size()-1);
			   } else {
				   //Falls auch dieser Kartenraum leer:
				   fehlerLog("ErrorID: 4");
				   ret = memEigene.get(memEigene.size()-1);  
			   }
			   break;
	    	}
		case 3:{
			   //Falls Kartenraum leer:
			   if(memEigeneMittel.size()>0) {
				   fehlerLog("ErrorID: 5");
				   ret = memEigeneMittel.get(0);
			   } else {
				   //Falls auch dieser Kartenraum leer:
				   fehlerLog("ErrorID: 6");
				   ret = memEigene.get(0);  
			   }   
			   break;
	     	} 	
		case 4:{
	 		   //Falls Kartenraum leer:
	 		   if(memEigeneMittel.size()>0) {
	 			  fehlerLog("ErrorID: 7");
	 			   ret = memEigeneMittel.get(zufallsKarte(0, memEigeneMittel.size()));
	 		   } else {
	 			   //Falls auch dieser Kartenraum leer:
	 			  fehlerLog("ErrorID: 8");
	 			   ret = memEigene.get(zufallsKarte(0, memEigene.size()));  
	 		   }   
	 		  break;
	     	}	
		case 5:{
	 		   //Falls Kartenraum leer:
	 		   if(memEigeneMittel.size()>0) {
	 			  fehlerLog("ErrorID: 9");
	 			   ret = memEigeneMittel.get(zufallsKarte(0, memEigeneMittel.size()));
	 		   } else {
	 			   //Falls auch dieser Kartenraum leer:
	 			  fehlerLog("ErrorID: 10");
	 			   ret = memEigene.get(zufallsKarte(0, memEigene.size()));  
	 		   }
	 		  break;
	     	}
	}
	return ret;

    }
}
