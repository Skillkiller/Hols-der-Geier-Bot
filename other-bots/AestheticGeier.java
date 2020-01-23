import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @see AestheticGeier implementiert zwei Strategien:
 * In der ersten Strategie wird nach einem Wertigkeitssystem gehandelt (weitere Infos am Methodenkopf){@link #valenceStrategy(int)}
 * Nachdem der Mindestpunktestand zum Gewinnen (21Punkte) erreicht wurde, wechselt der Geier in die 2. Strategie,
 * bei dem alle Wahrscheinlichkeiten, die momentane Punktekarte zu gewinnen, berechnet werden und je nachdem ob die
 * Wahrscheinlichkeit hoch oder niedrig ist wird die Karte gelegt (weitere Infos am Methodenkopf){@link #probabilityDistributionStrategy(int)}
 *
 * Nutzt Java 13
 *
 * @author Daniel
 * @version 3.2_FINAL
 * @since 30.12.2019
 */

public class AestheticGeier extends HolsDerGeierSpieler {

	/**
	 * @see #ownCardsAvailableList beinhaltet alle Spielkarten welche von uns vorhanden sind.
	 * Alle Integerwerte in der Liste spiegeln die Punktzahlen auf den Karten wieder.
	 * @see #updateCardDecks(int, int, int), verwaltet und aktualisiert die Einträge in der ArrayList.
	 */
	private List<Integer> ownCardsAvailableList = new ArrayList<Integer>();

	/**
	 * @see #enemyCardsAvailableList beinhaltet alle Spielkarten welche von dem Gegner vorhanden sind.
	 * Alle Integerwerte in der Liste spiegeln die Punktzahlen auf den Karten wieder.
	 * @see #updateCardDecks(int, int, int), verwaltet und aktualisiert die Einträge in der ArrayList.
	 */
	private List<Integer> enemyCardsAvailableList = new ArrayList<Integer>();

	/**
	 * @see #pointCardsAvailableList beinhaltet alle Punktekarten welche noch in dem Spiel erscheinen können.
	 * Alle Integerwerte in der Liste spiegeln den Punktwerte auf den Karten wieder.
	 * @see #updateCardDecks(int, int, int), verwaltet und aktualisiert die Einträge in der ArrayList.
	 */
	private List<Integer> pointCardsAvailableList = new ArrayList<Integer>();

	/**
	 * @see #ownPlayerNumber und {@link #enemyPlayerNumber} bestimmt die interne Spielernummer
	 * und werden durch die Methode {@link #reset()} mit einem Wert bestimmt.
	 */
	private int ownPlayerNumber, enemyPlayerNumber = 0;

	/**
	 * @see #ownScore und {@link #enemyScore} bestimmen den momentanen Punktestand der jeweiligen Spieler.
	 * Diese Werte werden durch die Methode {@link #updateDataset(int)} aktualisiert.
	 */
	private int ownScore, enemyScore;

	/**
	 * @see #isDraw zwischenspeichert, ob der momentane Zug ein Unentschieden ist. In der {@link #gibKarte(int)}
	 * Methode wird der Wert abgefragt und in der Methode {@link #updateDataset(int)} wird der boolsche Wert fuer jeden Zug
	 * aktualisiert.
	 */
	private boolean isDraw = false;

	/**
	 * @see #POINT_LIM ist ein konstanter Wert an dem die Strategie wechselt. Er beträgt den Wert 21.
	 * Da: (1+2+3+4+5+6+7+8+9+10) - (1+2+3+4+5) = 40/2 = 20.
	 * Man benötigt 20Punkte für ein Unentschieden und mindestens 21 zum gewinnen.
	 */
	private final int POINT_LIM = 21; // Punkte wo die Strat gewechselt wird

	/**
	 * Überschreibt die Methode {@link HolsDerGeierSpieler#reset()} und wird in jedem Spiel anfangs aufgerufen.
	 * Die Methode setzt die Spielernummer fest, leert erstmal und füllt dann alle ArrayListen
	 * mit den Punktekarten & Spielkarten. Sie setzt dazu {@link #lastCard}=0, da diese Variable
	 * in der {@link #updateDataset(int)} benötigt wird und vor jedem Spiel null gesetzt werden muss. Dazu setzt es den
	 * eigenen und gegnerischen Punktestand zurück.
	 */
	@Override
	public void reset () {
		//Legt die Spieler Nummern fest.
		ownPlayerNumber = this.getNummer();
		if(ownPlayerNumber==0){
			enemyPlayerNumber = 1;
		}else{
			enemyPlayerNumber = 0;
		}

		//Füllt die KartenArrayListen.
		ownCardsAvailableList.clear();
		for (int i = 1; i < 16; i++) ownCardsAvailableList.add(i);
		enemyCardsAvailableList.clear();
		for (int i = 1; i < 16; i++) enemyCardsAvailableList.add(i);
		pointCardsAvailableList.clear();
		for (int i = -5; i < 11; i++) if(i!=0) pointCardsAvailableList.add(i);

		//Setzt lastCard fuer updateDataset() zurück auf 0.
		lastCard=0;

		//Legt den Spielstand zurück auf 0.
		ownScore = 0;
		enemyScore = 0;
	}

	/**
	 * Überschreibt die Methode {@link HolsDerGeierSpieler#gibKarte(int)} und wird nach jedem Zug aufgerufen.
	 * Die Methode errechnet die Punkte, um die gespielt werden, dass heißt, es werden auch Unentschieden beachtet,
	 * dafür ist die {@link #isDraw} Variable zuständig, sie prüft, ob ein unentschieden stattfindet und addiert
	 * die Punkte hinzu.
	 * @param naechsteKarte bestimmt die naechsteKarte welche gespielt wird.
	 * @return der Wert der Karte welche gespielt wird, dieser wird von dem {@link #strategyHandler(int, int)} von einer
	 * der beiden Strategien ausgewählt.
	 */
	@Override
	public int gibKarte(int naechsteKarte) {
		int points = naechsteKarte; //Punkte um die gespielt werden in dem Zug.
		updateDataset(naechsteKarte);

		//Draw Handler.
		if(isDraw) for (int drawPoints : drawCardsList) points+=drawPoints;
		//Strategy Handler.
		return strategyHandler(points, ownScore);
	}

	/**
	 * @see #strategyHandler(int, int) entscheidet welche Strategie genutzt wird.
	 * Ist der momentane Punktstand {@link #ownScore} unter dem Mindestwert zum gewinnen {@link #POINT_LIM}
	 * Wählt dieser die erste Strategie aus {@link #valenceStrategy(int)} ist der Wert über dem Mindestwert
	 * wählt dieser die zweite Strategie aus {@link #probabilityDistributionStrategy(int)}.
	 * Wenn bei den Strategien ein Fehler (eine Exception) auftritt, gibt dieser die kleinste momentan verfügbare Karte
	 * zurück.
	 * @param points die Punkte um die gespielt wird
	 * @param ownScore den eignen momentanen Punktestand
	 * @return gibt den Wert der Karte zurück, welche die jeweilige Strategie bestimmt hat.
	 */
	private int strategyHandler(int points, int ownScore){
		try {
			if(ownScore<= POINT_LIM) return valenceStrategy(points);
			else return  probabilityDistributionStrategy(points);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			return Collections.min(ownCardsAvailableList);
		}
	}

	/**
	 * @see #valenceStrategy(int) Strategie #1
	 *
	 * Diese Strategie funktioniert nach einem Wertigkeitssystem. Das heißt, dass eine Punktekarte dem Wert der eigenen
	 * und gegnerischen Spielkarten zugeschrieben wird.
	 * Das Wertigkeitssystem ist in sechs Wertigkeiten eingestuft.
	 *
	 * - Die höchste Wertigkeit (VALENCE 1*): Bei Punktkartenwerten über 10 oder unter -5 welche bei einem Unentschieden
	 * - entstehen können, wird die höchste momentan vorhandene Karte ausgespielt.
	 *
	 * - Die erste Wertigkeit (VALENCE 1): Bei Punktekartenwerten von 10 & 9 wird geschaut, ob der eigene Spieler die
	 * - hohe Karte nicht besitzt oder der Gegner sie besitzt, wenn dies eintrifft wird die niedrigste noch vorhandene
	 * - Karte gespielt, wenn nicht, wird die hohe Karte gespielt.
	 *
	 * - Die zweite Wertigkeit (VALENCE 2): Bei Punktekartenwerten von 8,7 & 6 wird der Verfahren von VALENCE 1 genutzt
	 * - und bei 5 & -5 wird die Methode {@link #takeCardBetween(int, int)} aufgerufen, welche versucht aus dem
	 * - Wertebereich von 10 und 6 die höchste zu legen. Wenn jedoch keine von diesen (in diesem Fall 10,9,8,7,6)
	 * - Spielkarten vorhanden ist, legt er die niedrigste vorhandene Spielkarte.
	 *
	 * - Die dritte Wertigkeit (VALENCE 3): Bei Punktekartenwerten von 4,3,2 & -4,-3,-2 wird wieder die Methode
	 * - {@link #takeCardBetween(int, int)} aufgerufen, welche den Wert der jeweiligen Karten übernimmt.
	 * - Hier gilt: Die Wertigkeit = max=(Punktekartenwert*2) bis min=((Punktekartenwert*2) -/+2 )
	 *
	 * - Die vierte Wertigkeit (VALENCE 4): Bei Punktekartenwerten von 1 & -1 wird wieder die Methode
	 * - {@link #takeCardBetween(int, int)} Wert wie bei VALENCE 3.
	 * - Hier gilt: Die Wertigkeit = max=(Punktekartenwert*2) bis min=(Punktekartenwert)
	 *
	 * - Die niedrigste Wertigkeit (VALENCE 5): Wenn kein Punktekartenwert besteht also z.B. ein Unentschieden auftritt
	 * - wobei die erste Punktekarte +5 ist und die zweite Punktekarte -5, ergibt sich ein Wert von 0.
	 * - Hier wird direkt die kleinste Momentan verfügbare Karte gespielt.
	 *
	 * - Nutzt Java 13 Switch Case Statement
	 *
	 * @param points Der Punktkartenwert um den gespielt wird.
	 * @return Gibt die Spielkarte zurück welche gelegt wird.
	 * @throws IllegalArgumentException Wirft die Exception wenn ein ungültiger Punktewert als Parameter uebergeben wurde.
	 */
	private int valenceStrategy(int points) throws IllegalArgumentException {
		int returnValue;

		//Exception
		if(points > 55 || points < -15) throw new IllegalArgumentException("Ungueltiger Wert !");

		//VALENCE 1*
		if(points > 10) returnValue = Collections.max(ownCardsAvailableList);
		else if(points < -5) returnValue = Collections.max(ownCardsAvailableList);
		else {
			returnValue = switch (points) {
				//VALENCE 1
				case 10 -> (!ownCardsAvailableList.contains(15) || enemyCardsAvailableList.contains(15)) ? Collections.min(ownCardsAvailableList) : 15;
				case 9 -> (!ownCardsAvailableList.contains(14) || enemyCardsAvailableList.contains(14)) ? Collections.min(ownCardsAvailableList) : 14;
				//VALENCE 2
				case 8 -> (ownCardsAvailableList.contains(13)) ? 13 : Collections.min(ownCardsAvailableList);
				case 7 -> (ownCardsAvailableList.contains(12)) ? 12 : Collections.min(ownCardsAvailableList);
				case 6 -> (ownCardsAvailableList.contains(11)) ? 11 : Collections.min(ownCardsAvailableList);
				case 5, -5 -> takeCardBetween(10, 6);
				//VALENCE 3
				case 4, -4 -> takeCardBetween(8, 6);
				case 3, -3 -> takeCardBetween(6, 4);
				case 2, -2 -> takeCardBetween(4, 2);
				//Valence 4
				case 1, -1 -> takeCardBetween(2, 1);
				//Valence 5
				case 0 -> Collections.min(ownCardsAvailableList);
				//Default
				default -> throw new IllegalArgumentException("Ungueltiger Wert !");
			};
		}

		return returnValue;
	}

	/**
	 * @see #takeCardBetween(int, int) versucht eine möglichst hohe Spielkarte zurückzugeben.
	 * Versucht von der max bis min alle Spielkarten zurückzugeben, und fängt bei der höchsten an.
	 * Wenn keine spielbare Spielkarte zwischen max & min gefunden wurde, gibt sie die niedrigste Spielkarte
	 * im eigenen Deck zurück.
	 * @param max Die höchste Spielkarte, die gegeben werden kann.
	 * @param min Die niedrigste Spielkarte, die gegeben werden kann.
	 * @return Die Spielkarte welche indexiert wurde.
	 */
	private int takeCardBetween(int max, int min){
		//Zählt runter von max bis min
		for (int i=max; max >= min; max--){
			if (ownCardsAvailableList.contains(i)) return i;
		}
		return Collections.min(ownCardsAvailableList);
	}

	/**
	 * @see #probabilityDistributionStrategy(int) Strategie #2
	 *
	 * Dies ist die zweite Strategie, welche, nachdem man das {@link #POINT_LIM} überschritten hat, genutzt wird.
	 * Sie funktioniert nach dem Wahrscheinlickeitsverteilungssystem. Das heißt, dass alle Wahrscheinlichkeiten, welche von
	 * {@link #getAllCombinations(List, List, List)} errechnet werden, überprüft werden und geschaut wird, ob die
	 * Wahrscheinlichkeit, dass der eigene Geier die Karte gewinnen kann, höher ist, als die von dem gegnerischen Geier.
	 * Wenn die Wahrscheinlichkeit höher liegt, als die vom gegnerischen Geier, wird geschaut welchen Wert die Punktekarte
	 * hat und darauf hin wird eine Spielkarte mit der gleichen Wertigkeit gelegt. Wenn keine dieser Spielkarten
	 * vorhanden ist (Da sie über die {@link #takeCardBetween(int, int)} Methode) oder die Wahrscheinlichkeit niedriger
	 * ist, wird die kleinste noch verfügbare Spielkarte zurückgegeben. Wenn die Wahrscheinlichkeit, die Punktekarte zu
	 * gewinnen gleich der Wahrscheinlichkeit des Gegners ist, wird die {@link #valenceStrategy(int)} genutzt.
	 * Sie gibt wie bei der #1 Strategie bei dem Punktekartenwert über 10 oder unter -5 die höchste momentan verfügbare
	 * Punktekarte zurück und gibt eine Exception wenn ein Wert übergeben wird, welcher rein spieltechnisch nicht
	 * möglich ist (größer als 55 oder kleiner als -15).
	 * @param points Die Punkte um die gespielt wird.
	 * @return Die Spielkarte welche ausgespielt wird.
	 * @throws IllegalArgumentException Wenn die Punkte um die gespielt wird, einen unrealistischen Wert haben.
	 */
	private int probabilityDistributionStrategy(int points) throws IllegalArgumentException {
		//Exception
		if (points > 55 || points < -15) throw new IllegalArgumentException("Ungueltiger Wert !");
		if(points > 10) return Collections.max(ownCardsAvailableList);
		else if(points < -5) return Collections.max(ownCardsAvailableList);
		else{
			//Strategy Start

			//Get all Combinations
			Combination[] allCombinations = getAllCombinations(ownCardsAvailableList,enemyCardsAvailableList,pointCardsAvailableList);
			Combination[] allCombinationsForPointsCard = getCombinationsForPointsCard(allCombinations, points);

			//Get all Probabilities
			int myScore=0,enemyScore=0;
			for (int i=0; i < allCombinationsForPointsCard.length; i++){
				if(allCombinationsForPointsCard[i].getMyCard() > allCombinationsForPointsCard[i].getEnemyCard()){
					myScore++;
				}else if(allCombinationsForPointsCard[i].getMyCard() < allCombinationsForPointsCard[i].getEnemyCard()){
					enemyScore++;
				}
			}

			//When Own Probability is higher, use a Valence System for the assignment
			if (myScore>enemyScore){
				if (points >= 7 ) return takeCardBetween(15,10);
				else if (points >= 4) return takeCardBetween(10,5);
				else if (points <= -1) return takeCardBetween(12,5);
				else return takeCardBetween(1,5);
			}
			//When Enemy Probability is higher, return lowest card.
			else if(enemyScore>myScore){
				return Collections.min(ownCardsAvailableList);
			}
			//When Own Probability and Enemy Probability is the same, use the ValenceStrategy.
			else{
				return valenceStrategy(points);
			}
		}
	}


	/**
	 * @see #getAllCombinations(List, List, List) Berechnet alle möglichen Kombinationen von eignene Karten,
	 * gegnerischen Karten und Punktekarten. Speichert diese in einer Arrayliste und gibt sie als Combination[] zurück.
	 * @param myCards Die eigenen noch verfügbaren Karten.
	 * @param enemyCards Die gegnerischen noch verfügbaren Karten.
	 * @param pointCards Die noch verfügbaren Punktekarten.
	 * @return gibt alle möglichen Kombinationen zurück.
	 */
	private Combination[] getAllCombinations(List<Integer> myCards, List<Integer> enemyCards, List<Integer> pointCards) {
		List<Combination> returnList = new ArrayList<Combination>();

		//Get all Combinations. Combination Size is = (Size^3)
		for (int i = 0; i < myCards.size(); i++) {
			for (int j = 0; j < enemyCards.size(); j++) {
				for (int k = 0; k < pointCards.size(); k++) {
					returnList.add(new Combination(myCards.get(i), enemyCards.get(j), pointCards.get(k)));
				}
			}
		}

		//Parse from ArrayList to Combination[]
		Combination[] returnValue = new Combination[returnList.size()];
		for (int i = 0; i < returnList.size(); i++) {
			returnValue[i] = returnList.get(i);
		}
		return returnValue;
	}

	/**
	 * @see #getCombinationsForPointsCard(Combination[], int) Extrahiert aus allen möglichen Kombinationen, nur die
	 * Kombinationen heraus, welche relevant sind für die bestimmte Punktekarte.
	 * @param allCombinations Alle Kombinationen welche fuer die noch verfuegbaren Karten moeglich sind.
	 * @param pointCard Die Punktekarte nach denen extrahiert wird.
	 * @return Alle Kombinationen für die Punktekarte
	 */
	private Combination[] getCombinationsForPointsCard(Combination[] allCombinations, int pointCard){

		List<Combination> combinationList = new ArrayList<Combination>();
		for (int i = 0; i < allCombinations.length; i++) {
			if(allCombinations[i].getPointCard() == pointCard) {
				combinationList.add(allCombinations[i]);
			}
		}
		Combination[] returnValue = new Combination[combinationList.size()];
		for (int i = 0; i < combinationList.size(); i++) {
			returnValue[i] = combinationList.get(i);
		}
		return returnValue;
	}

	/**
	 * @see Combination ist eine eingebettete Klasse, welche nur einen
	 * Konstruktor {@link Combination#Combination(int, int, int)} hat, welcher die drei Variablen festsetzt.
	 * Diese Klasse dient dazu eine möglich Kombination darzustellt, sie kann eine eigene Karte,
	 * eine gegnerische Karte und eine Punktekarte bereitstellen, welche ein 3-Tupel (Tripel) darstellt.
	 * Sie dient als einfachere Speicherung von Kombinationen
	 * für Strategie 2 {@link #probabilityDistributionStrategy(int)}.
	 */
	class Combination{
		private int myCard, enemyCard, pointCard;
		public Combination(int myCard, int enemyCard, int pointCard) {
			this.myCard = myCard;
			this.enemyCard = enemyCard;
			this.pointCard = pointCard;
		}

		/**
		 * @return gibt die Eigenekarte zurück.
		 */
		public int getMyCard() {
			return myCard;
		}

		/**
		 * @return gibt die Gegnerkarte zurück.
		 */
		public int getEnemyCard() {
			return enemyCard;
		}

		/**
		 * @return gibt die Punktekarte zurück.
		 */
		public int getPointCard() {
			return pointCard;
		}
	}


	/**
	 * @see #updateCardDecks(int, int, int) entfernt die übergebenen Karten aus den noch vorhandenen KartenArrayListen.
	 * @param myCard Meine Karte, die entfernt wird.
	 * @param enemyCard Gegnerische Karte, die entfernt wird.
	 * @param pointCard Punktekarte, die entfernt wird.
	 */
	private void updateCardDecks(int myCard, int enemyCard, int pointCard) {
		ownCardsAvailableList.remove(ownCardsAvailableList.indexOf(myCard));
		enemyCardsAvailableList.remove(enemyCardsAvailableList.indexOf(enemyCard));
		pointCardsAvailableList.remove(pointCardsAvailableList.indexOf(pointCard));
	}

	/**
	 * @see #drawCardsList Diese Liste beinhaltet alle Karten, welche bei einem Unentschieden anfallen.
	 * @see #lastCard Dient zum Speichern der Punktekarte der letzten Runde. Wird daraufhin in die {@link #drawCardsList}
	 * hinzugefuegt.
	 * */
	private List<Integer> drawCardsList = new ArrayList<Integer>();
	private int lastCard=0;

	/**
	 * @see #updateDataset(int) aktualisiert und kontrolliert folgende ArrayListen und Attribute nach jedem Zug:
	 * @see #drawCardsList,
	 * @see #enemyCardsAvailableList,
	 * @see #ownCardsAvailableList,
	 * @see #pointCardsAvailableList,
	 * @see #isDraw,
	 * @see #ownScore,
	 * @see #enemyScore,
	 * @see #lastCard
	 * @param naechsteKarte die Punktekarte welche momentan gespielt wird.
	 */
	private void updateDataset(int naechsteKarte){
		if(!(getHdg().letzterZug(ownPlayerNumber)==-99)) updateCardDecks(getHdg().letzterZug(ownPlayerNumber), getHdg().letzterZug(enemyPlayerNumber), lastCard);
		if (!(getHdg().letzterZug(ownPlayerNumber)==-99)){
			if (getHdg().letzterZug(enemyPlayerNumber) == getHdg().letzterZug(ownPlayerNumber)){
				if(drawCardsList.size() == 2) drawCardsList.remove(0);
				drawCardsList.add(lastCard);
				isDraw=true;
			}
			if(getHdg().letzterZug(enemyPlayerNumber) > getHdg().letzterZug(ownPlayerNumber)  ){
				if (!drawCardsList.isEmpty()){
					if(drawCardsList.size() == 2) drawCardsList.remove(0);
					drawCardsList.add(lastCard);

					int count = 0;
					for (Integer i: drawCardsList){
						count+=i;
					}
					for (Integer i: drawCardsList){
						if (count>0){
							ownScore+=i;
						}else{
							enemyScore+=i;
						}
					}
					drawCardsList.clear();
					isDraw=false;
				}else{
					if(lastCard >0) ownScore +=lastCard;
					else enemyScore +=lastCard;
				}

			}else if(getHdg().letzterZug(enemyPlayerNumber) < getHdg().letzterZug(ownPlayerNumber)){
				if (!drawCardsList.isEmpty()){
					if(drawCardsList.size() == 2) drawCardsList.remove(0);
					drawCardsList.add(lastCard);

					int count = 0;
					for (Integer i: drawCardsList){
						count+=i;
					}
					for (Integer i: drawCardsList){
						if (count>0){
							enemyScore+=i;
						}else{
							ownScore+=i;
						}
					}
					drawCardsList.clear();
					isDraw=false;
				}else {
					if (lastCard > 0) enemyScore += lastCard;
					else ownScore += lastCard;
				}
			}
		}

		lastCard = naechsteKarte;
	}

}