

import java.util.ArrayList;

public class Sascha extends HolsDerGeierSpieler {

	private ArrayList<Integer> nochZuGewinnen = new ArrayList<Integer>();
	private ArrayList<Integer> vomGegnerNochNichtGelegt = new ArrayList<Integer>();
	private ArrayList<Integer> nochNichtGespielt = new ArrayList<Integer>();
	private ArrayList<Integer> hoheKarten = new ArrayList<Integer>();
	private ArrayList<Integer> niedrigeKarten = new ArrayList<Integer>();
	private ArrayList<Integer> mittlereKarten = new ArrayList<Integer>();
	private ArrayList<Integer> niedrigsteKarten = new ArrayList<Integer>();
	private ArrayList<Integer> hoechsteKarten = new ArrayList<Integer>();
	

	private ArrayList<Integer> letzteKarte = new ArrayList<Integer>();
	private ArrayList<Integer> bereitsGespielt = new ArrayList<Integer>();

	int i;
	int meineLetzteKarte;
	int zuGewinnen;

	public Sascha() {
		reset();

	}

	public boolean unentschiedenPruefung() {
		if (bereitsGespielt.size() != 0) {
			meineLetzteKarte = bereitsGespielt.get(bereitsGespielt.size() - 1);
		}
		if (meineLetzteKarte == letzterZug()) {
			return true;
		} else
			return false;
	}

	@Override
	public int gibKarte(int naechsteKarte) {



		i = nochNichtGespielt.get(0);

		letzteKarte.add(naechsteKarte);

		//System.out.println("remis : "+unentschiedenPruefung());
		if (unentschiedenPruefung()) {
			zuGewinnen = zuGewinnen + naechsteKarte;
		} else {
			zuGewinnen = naechsteKarte;
		}
		if (zuGewinnen < 0) {
			zuGewinnen = zuGewinnen * -1;
		}

		switch (einordnung(zuGewinnen)) {

		case 1:
			if (niedrigsteKarten.size() != 0) {
				i = niedrigsteKarten.get((int) Math.random() * niedrigsteKarten.size());
			};
			//System.out.println("Case 1 " + i);
			break;
		case 2:
			if (niedrigeKarten.size() != 0) {
				i = niedrigeKarten.get((int) Math.random() * niedrigeKarten.size());
			};
			//System.out.println("Case 2 " + i);
			break;
		case 3:
			if (mittlereKarten.size() != 0) {
				i = mittlereKarten.get(mittlereKarten.size()-1);
			}
			else if(hoheKarten.size()!=0){
				i=hoheKarten.get(0);
			}
			else if(hoechsteKarten.size()!=0){
				i=hoechsteKarten.get(0);
			}
			
			//System.out.println("Case 3 " + i);
			
			break;
		case 4:
			if (hoheKarten.size() != 0) {
				i = hoheKarten.get(hoheKarten.size()-1);
			}
			else{
				i=nochNichtGespielt.get(nochNichtGespielt.size()-1);
			}
			//System.out.println("Case 4 " + i);
			
			break;
		case 5:
			
			i=nochNichtGespielt.get(nochNichtGespielt.size()-1);
			//System.out.println("Case 5 " + i);
			break;
		}

		bereitsGespielt.add(i);

		if (niedrigsteKarten.contains(i)) {
			niedrigsteKarten.remove(niedrigsteKarten.indexOf(i));
		} else if (niedrigeKarten.contains(i)) {
			niedrigeKarten.remove(niedrigeKarten.indexOf(i));
		} else if (mittlereKarten.contains(i)) {
			mittlereKarten.remove(mittlereKarten.indexOf(i));
		} else if (hoheKarten.contains(i)) {
			hoheKarten.remove(hoheKarten.indexOf(i));
		}else{
			hoechsteKarten.remove(hoechsteKarten.indexOf(i));
		}

		nochNichtGespielt.remove(nochNichtGespielt.indexOf(i));
		return i;
	}

	public int einordnung(int zuGewinnen) {
		//System.out.println(zuGewinnen);
		if (zuGewinnen <= 2)
			return 1;
		else if (zuGewinnen <= 4)
			return 2;
		else if (zuGewinnen <= 6)
			return 3;
		else if(zuGewinnen<=8)
			return 4;
		else
			return 5;
	}

	@Override
	public void reset() {
		nochZuGewinnen.clear();
		for (int i = 10; i > -6; i--)
			nochZuGewinnen.add(i);
		vomGegnerNochNichtGelegt.clear();
		for (int i = 15; i > 0; i--)
			vomGegnerNochNichtGelegt.add(i);
		nochNichtGespielt.clear();
		for (int i = 1; i < 16; i++)
			nochNichtGespielt.add(i);
		niedrigsteKarten.clear();
		for (int i = 1; i <= 3; i++)
			niedrigsteKarten.add(i);
		niedrigeKarten.clear();
		for (int i = 4; i <= 6; i++)
			niedrigeKarten.add(i);
		mittlereKarten.clear();
		for (int i = 7; i <= 9; i++)
			mittlereKarten.add(i);
		hoheKarten.clear();
		for (int i = 10; i <= 12; i++)
			hoheKarten.add(i);
		hoechsteKarten.clear();
		for (int i = 13; i <= 15; i++)
			hoechsteKarten.add(i);

	}

}
