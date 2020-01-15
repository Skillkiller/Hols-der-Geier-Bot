

import java.util.ArrayList;


public class RosaHase extends HolsDerGeierSpieler
		//Die auskommentierten System.out.Println im Quelltext dienten und dienen der Bugbeseitigung
{

		//ArrayListen 
		private ArrayList<Integer> nochZuGewinnen=new ArrayList<Integer>();
	    private ArrayList<Integer> vomGegnerNochNichtGelegt=new ArrayList<Integer>();
	    private ArrayList<Integer> nochNichtGespielt=new ArrayList<Integer>();
	    
	    /**
	     * Hier definieren Sie den Konstruktor fuer Objekte Ihrer Klasse (falls Sie einen eigenen brauchen) IntelligentererGeier
	    */
	    
	    //Konstruktor
	    public RosaHase() 
	    {
	    	reset();
	    }
	       
	    
	    
	    //Reset Methode + Arrays füllen
	    public void reset() 
	    {
	        nochZuGewinnen.clear();
	        for (int i=10;i>-6;i--)
	            nochZuGewinnen.add(i);
	        		vomGegnerNochNichtGelegt.clear();        
	        for (int i=15;i>0;i--)
	            vomGegnerNochNichtGelegt.add(i);
	        		nochNichtGespielt.clear();
	        for (int i=15; i>0; i--)
	        	nochNichtGespielt.add(i);
	     
	    }
	    
	   //Unsere "Hauptmethode, die entscheidet was letzendlich gespielt wird und 
	   //entsprechendes aus den Arrays entfernt
	    public int gibKarte(int naechsteKarte)
	    
	    {
	    	
	    //Letzter Zug Gegner aus Arrayliste vomGegnerNochNichtgelegt werfen	
	    int letzteKarteGeg = letzterZug();
	    if (letzteKarteGeg !=-99)
	    {
	    	vomGegnerNochNichtGelegt.remove(vomGegnerNochNichtGelegt.indexOf(letzteKarteGeg));
	    }
	    						//System.out.println(vomGegnerNochNichtGelegt);

	    int ausgabe = 999999;
	    
	    //Da es für uns keien Rolle spielt ob eine 1 oder eine -1 liegt werden 
	    //hier alle negativen Werte durch *(-1) zu positiven umgewandelt
	    if (naechsteKarte < 0) {
	    	naechsteKarte = naechsteKarte * -1;
	    }
	    
	    //Hier wird die ausgabe für alle Karten von -5 bis 5 bestimmt
		if (naechsteKarte <= 5){
			//Random Würfeln i ist entweder 0 oder 1 daraus entscheidet sich was gespielt wird
			int i = ((int)(Math.random()*2));
								//System.out.println("i: " +i);
				
				//Hier entscheidet sich letzendlich für diesen Bereich die Ausgabe
				//Karte*2+1 oder 2 je nachdem ob der Random Wert i 1 oder 0 ist
				if (i==1) {
					if (nochNichtGespielt.contains(naechsteKarte*2+1)) {
						
						ausgabe = naechsteKarte*2+1;
					//das else dient dazu den Fall abzufangen falls der erste schon gespielt wurde
					} else {
						ausgabe = naechsteKarte*2+2;
					}
				}
				if (i==0) {
					if (nochNichtGespielt.contains(naechsteKarte*2+2)) {
						ausgabe = naechsteKarte*2+2;
					} else {
						ausgabe = naechsteKarte*2+1;
					}
				}
								//System.out.println("spiele: " + ausgabe);	
		}else{
			//die Werte 6-10 sind Sonderfälle und werden im Folgenden einzeln 
			//in if-Bedingungen ausprogrammiert
			
			//Wenn 10 liegt soll falls wir sie noch auf der "Hand" haben
			//und der Gegner die 10 schon gespielt hat die 15 gelegt
			//werden ansonsten die 2 
			if (naechsteKarte == 10 && !vomGegnerNochNichtGelegt.contains(15) && nochNichtGespielt.contains(15) ){
				ausgabe = 15; //System.out.println("bin im 10 fall");
				 }else if (naechsteKarte ==10 &&nochNichtGespielt.contains(2) ){
				ausgabe = 2;
				}
			
			//selbe Fall wir bei der 10 nur mit 14 und 1 
			if (naechsteKarte == 9 && !vomGegnerNochNichtGelegt.contains(14) && nochNichtGespielt.contains(14) ){
				ausgabe = 14; //System.out.println("bin im 9 fall");
				}else if (naechsteKarte ==9 && nochNichtGespielt.contains(1) ){
				ausgabe = 1;
				}
			
			//Bei der 6 wird immer die 13 gespielt 
			if (naechsteKarte == 6) { 
								//System.out.println("bin in if 6 Bedingung");
				ausgabe = 13;
								//System.out.println("ausgabe" + ausgabe);
				}	
			
			//Normal wird bei der 8 die 15 gespielt, nur wenn der oben genannte
			//Fall eingetreten ist das bei der 10 die 15 gespielt wird muss hier
			//die 2 gespielt werden
			if (naechsteKarte == 8 && nochNichtGespielt.contains(15)){
				ausgabe = naechsteKarte +7; //System.out.println("bin im 8 fall");
			}else if(naechsteKarte == 8 && nochNichtGespielt.contains(2)){
				ausgabe = 2;
				}
			
			//Selbe Fall wie bei der 8 nur für 14 und 1 
			if (naechsteKarte == 7 && nochNichtGespielt.contains(14)){
				ausgabe = naechsteKarte +7;//System.out.println("bin im 7 fall");
			}else if(naechsteKarte == 7 && nochNichtGespielt.contains(1)){
				ausgabe = 1;
				}
		}	
		
		
		//allgemeine Ausgabe
		
	    	//System.out.println("spiele: " + ausgabe);
	    	//gespielteKarte aus der Arrayliste entfernen
		    nochNichtGespielt.remove(nochNichtGespielt.indexOf(ausgabe));
		    return ausgabe;
	    	
		
		}
}
	

