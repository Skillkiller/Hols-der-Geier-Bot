
import java.util.ArrayList;

public class DanielUndJanekmitMerken extends HolsDerGeierSpieler{

	private ArrayList<Integer> gegnerKarten				= new ArrayList<Integer>();
	private ArrayList<Integer> meineKarten 				= new ArrayList<Integer>();
	private ArrayList<ArrayList<Integer>>containerPack 	= new ArrayList<ArrayList<Integer>>();
	private int runden, letzteKarte, aktuelleaKarte;

	public DanielUndJanekmitMerken() {
		this.reset();
	}

	public int gibKarte(int naechsteKarte) {
		int temp = 0;
		//Speicher die aktuelle Karte
		aktuelleaKarte = naechsteKarte;
		//Letzte Karte des Gegner?
		gegnerKarten.add(super.letzterZug());
		if(			naechsteKarte<4	&&	naechsteKarte>0){	
			temp = this.spieleKarte(naechsteKarte, 			containerPack.get(0));						
			//Pack von 1 bis 3
		}else if(	naechsteKarte>3	&&	naechsteKarte<8){ 
			temp = this.spieleKarte(naechsteKarte+5, 		containerPack.get(1));				
			//Pack von 4 bis 7
		}else if(	naechsteKarte>7	&&	naechsteKarte<11){ 
			temp = this.spieleKarte(naechsteKarte+5, 		containerPack.get(2));				
			//Pack von 8 bis 10
		}else if(	naechsteKarte<0 && naechsteKarte >-4){ 
			temp = this.spieleKarte((naechsteKarte*(-1))+3, containerPack.get(3));		
			//Pack von -1 bis -3
		}else
			temp = this.spieleKarte((naechsteKarte*(-1))+3, containerPack.get(4)); 		
		//Pack von -4 und -5
		//Meine letzte Karte?
		meineKarten.add(temp);
		//speicher die Karte des letzten zugs
		letzteKarte = naechsteKarte;
		//Nächste Runde
		runden++;
		return temp;
	}

	private int spieleKarte(int naechsteKarte, ArrayList<Integer> templiste){
		//Gab es ein Unentschieden?
		if ((runden>0)&&(gegnerKarten.get(gegnerKarten.size()-1)==(meineKarten.get(meineKarten.size()-1)))){ 
			//Ist summe letzter und aktuellen Karte < 0?
			if((letzteKarte+aktuelleaKarte)<0)
				//JA: Spiele Kleinste
				return templiste.remove(0);
			//NEIN: Spiele Größte
			return templiste.remove((templiste.size()-1));
		}
		if(templiste.contains(naechsteKarte)&& gegnerKarten.contains(naechsteKarte))
			return templiste.remove(templiste.indexOf(naechsteKarte));
		if ((templiste.get(0) < 7)&&templiste.size()>1) {
			if(templiste.size()>2)
				return templiste.remove((int)(Math.random()*3));
			return templiste.remove((int)(Math.random()*2));
		}else
			return templiste.remove((templiste.size()-1));
	}

	public void reset(){
		letzteKarte =0;
		runden = 0;
		containerPack.clear();
		gegnerKarten.clear();
		meineKarten.clear();
		//Anlegen der einzelnen Packs
		for(int i = 0; i<=4;i++)						
			containerPack.add(new ArrayList<Integer>());
		//Füllen des Containers
		for(int i=0;i<3;i++){							
			containerPack.get(0).add(1+i);
			containerPack.get(2).add(13+i);
			containerPack.get(3).add(4+i);
			containerPack.get(1).add(9+i);
			if(i!=0)
				containerPack.get(4).add(6+i);
		}
		containerPack.get(1).add(12);	   
	}
}
