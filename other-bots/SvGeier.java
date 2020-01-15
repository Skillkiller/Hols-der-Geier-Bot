
import java.util.ArrayList;

/**
 * von Janis Albersmeier und Jakob Gömpel
 * Taktik SvGeier!
 * 
 */
public class SvGeier extends HolsDerGeierSpieler {
    private ArrayList<Integer> enemyCards=new ArrayList<Integer>();		//Karten die der Gegner gespielt hat
    private ArrayList<Integer> myCards=new ArrayList<Integer>();		//Mein eigenes Deck
    private ArrayList<Integer> pointCards=new ArrayList<Integer>();		//Karten aus der Mitte
    private int myPoints = 0;
    private int enemyPoints = 0;

    /**
     * ArrayListen der Kartendecks werden neu gemischt, bzw. geleert!
     */
   public void reset () {
	   pointCards.clear();
	   enemyCards.clear();
	   myCards.clear();
	   for(int i=1;i<=15;i++){			//Deck wird neu aufgesetzt
		   myCards.add(i);
       }
   }
   
   /**
    * Gebt den einzelnen Karten einen Rang und wählt nach gegebener Taktik eine aus!
    * 
    * @param naechsteKarte - Die Geier/Mäusekarte welche "in der Mitte" liegt und um die gespielt wird 
    * @return wahl - Die gespielte Geierkarte
    */
    public int gibKarte(int naechsteKarte) {    	
    	if(myCards.size()!=15){
    		enemyCards.add(getHdg().letzterZug(getNummer() == 1 ? 0 : 1));
    	}else{
    		enemyCards.add(0);
    	}
        pointCards.add(naechsteKarte);
          
        int a;
        int b;
        int c;
        int wahl = 0;     	
        	if(naechsteKarte==10){
            	a=15;
            	b=14;
            	c=13;
            	wahl = waehleKarte(a, b, c);
            }
            if(naechsteKarte==9){
            	a=14;
            	b=15;
            	c=13;
            	wahl = waehleKarte(a, b, c);
            }
            if(naechsteKarte==8){
            	a=13;
            	b=14;
            	c=15;
            	wahl = waehleKarte(a, b, c);
            }
            if(naechsteKarte==7){
            	a=12;
            	b=11;
            	c=10;
            	wahl = waehleKarte(a, b, c);
            }
            if(naechsteKarte==6){
            	a=11;
            	b=12;
            	c=10;
            	wahl = waehleKarte(a, b, c);
            }
            if(naechsteKarte==5){
            	a=10;
            	b=11;
            	c=12;
            	wahl = waehleKarte(a, b, c);
            }
            if(naechsteKarte==4){
            	a=9;
            	b=5;
            	c=4;
            	wahl = waehleKarte(a, b, c);
            }
            if(naechsteKarte==3){
            	a=5;
            	b=9;
            	c=4;
            	wahl = waehleKarte(a, b, c);
            }
            if(naechsteKarte==2){
            	a=4;
            	b=5;
            	c=9;
            	wahl = waehleKarte(a, b, c);
            }
            if(naechsteKarte==1){
            	a=3;
            	b=2;
            	c=1;
            	wahl = waehleKarte(a, b, c);
            }
            if(naechsteKarte==-1){
            	a=2;
            	b=1;
            	c=3;
            	wahl = waehleKarte(a, b, c);
            }
            if(naechsteKarte==-2){
            	a=1;
            	b=3;
            	c=2;
            	wahl = waehleKarte(a, b, c);
            }
            if(naechsteKarte==-3){
            	a=6;
            	b=7;
            	c=8;
            	wahl = waehleKarte(a, b, c);
            }
            if(naechsteKarte==-4){
            	a=7;
            	b=8;
            	c=6;
            	wahl = waehleKarte(a, b, c);
            }
            if(naechsteKarte==-5){
            	a=8;
            	b=7;
            	c=6;
            	wahl = waehleKarte(a, b, c);
            }                   
        myCards.set(wahl-1, 0);
        return wahl;
    }
    
    /**
     * @deprecated
     * Wird von unserer Taktik nach langem überlegen nicht verwendet und wurde deshalb nicht mehr geprüft!
     * Soll den aktuellen Punktestand berechnen.
     */
    public void berechnePunktestand(){
    	int lastCard = pointCards.get(pointCards.size()-1);
    	int lastEnemyCard = enemyCards.get(enemyCards.size()-1);
    	int lastMyCard = myCards.get(myCards.size()-1);
    	int merkePoints = 0;
    	
    	if(lastCard<0){
    		if(lastEnemyCard==lastMyCard){
    			merkePoints = merkePoints + lastCard;
    		}else{
    			if(lastCard<0){
            		if(lastEnemyCard<lastMyCard){
            			setEnemyPoints(getEnemyPoints()+lastCard+merkePoints);
            			merkePoints = 0;
            		}else{
            			setMyPoints(getMyPoints()+lastCard+merkePoints);
            			merkePoints = 0;
            		}
            	}else{
            		if(lastEnemyCard>lastMyCard){
            			setEnemyPoints(getEnemyPoints()+lastCard+merkePoints);
            			merkePoints = 0;
            		}else{
            			setMyPoints(getMyPoints()+lastCard+merkePoints);
            			merkePoints = 0;
            		}
            	}
    		}
    	}
    }

    /**
     * Hier wird die Karte ausgewählt! Die eigentliche Taktik wird angewandt...
     * 
     * @param a - 1. Wahl
     * @param b - 2. Wahl
     * @param c - 3. Wahl
     * @return a, b oder c - Je nach Fall!
     */
    public int waehleKarte(int a, int b, int c){
   
    		if(enemyCards.contains(a)){
    			if(myCards.contains(a)){
    				return a;
    			}
    		}
    		if(enemyCards.contains(b)){
    			if(myCards.contains(b)){
    				return b;
    			}
    		}
    		if(enemyCards.contains(c)){
    			if(myCards.contains(c)){
    				return c;
    			}
    		}
    		if(myCards.contains(a)){
				return a;
			}
    		if(myCards.contains(b)){
				return b;
			}
    		if(myCards.contains(c)){
				return c;
			}
    	return 0;
    }

	public void setMyPoints(int myPoints) {
		this.myPoints = myPoints;
	}

	public int getMyPoints() {
		return myPoints;
	}

	public void setEnemyPoints(int enemyPoints) {
		this.enemyPoints = enemyPoints;
	}

	public int getEnemyPoints() {
		return enemyPoints;
	}
    
}
