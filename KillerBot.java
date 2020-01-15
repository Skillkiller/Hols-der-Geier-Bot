import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Diese Klasse verwendet eine simple Strategie.
 * Die Punket Karten werden in 2 Bereiche eingeteilt, einen niedrigen und einen hohen Punktzahl Bereich.
 * Im niedrigen Punktzahl Bereicht (Punkte < 7) wird immer die eigene niedrigste Karte gespielt.
 * Im hohen Punktzahl bereicht wird geprüft ob die eigenen Karten über die Karten vom Gegner sind.
 * Wenn mehr als eine Karte höher ist als die Gegner Karten, wird die eigene höchste Karte gespielt.
 * Wenn nicht wird geprüft ob die Punkte Karte in den höheren 50% der verfügbaren Punkte Karten sind. Wenn Ja dann wird die eigene höchste Karte gespielt.
 * Wenn nicht wird der Index der Punkte Karte genommen und dieser Index wird dann auf die eigenen Karten angewendet um eine Spielkarte zu bekommen.
 *
 * @author Skillkiller
 * @version 2.4
 */
public class KillerBot extends HolsDerGeierSpieler {
    //Beinhalte alle noch spielbaren Karten von eigenen Spieler
    ArrayList<Integer> ownCards = new ArrayList<>();

    //Beinhalte alle noch spielbaren Karten von Gegner Spieler
    ArrayList<Integer> enemyCards = new ArrayList<>();

    //Beinhalte alle noch kommende Punkte Karten
    ArrayList<Integer> pointCards = new ArrayList<>();
    //Beinhalte alle schon gelegten Punkte Karten
    LinkedList<Integer> pointCardsPlaced = new LinkedList<>();
    //Beinhalte schon gespielte Punkte Karten von denen aber die Punkte noch nicht verteilt wurden --> Bei Stechen
    ArrayList<Integer> pointCardsToDistribute = new ArrayList<>();

    /**
     * Setzt alle nötigen Variblen auf den Spielanangszustand zurück
     */
    public void reset() {
        enemyCards.clear();
        ownCards.clear();

        //Fügt alle spielbaren Karten für die Spieler hinzu
        for (int i = 1; i < 16; i++) {
            ownCards.add(i);
            enemyCards.add(i);
        }

        //Fügt die Punkte Karten von -5 bis -1 hinzu
        for (int i = 0; i < 5; i++) {
            pointCards.add(-5 + i);
        }

        //Fügt die Punkte Karten von 1 bis 10 hinzu
        for (int i = 1; i < 11; i++) {
            pointCards.add(i);
        }
    }

    public int gibKarte(int naechsteKarte) {
        calculateUpdateFromLastMove();
        int returnCard;

        //Berechne Punkte um die gespielt werden
        int points = naechsteKarte;
        for (Integer undecidedPoints : pointCardsToDistribute) {
            points += undecidedPoints;
        }

        //Mache negative Punkte Positiv. Negativer Wert hat den gleichen Wert wie der Positive in meinem Modell
        if (points < 0) {
            points = points * -1;
        }

        if (points < 7) {
            //Spiele niedrigste Karte
            returnCard = ownCards.get(0);

        } else {
            //Spiele Strategie
            int maxOpponentCard = getHighestCard(enemyCards);

            //Prüfe ob wieviele Karten vom eigenen Spieler höher sind als die höchste vom Gegner
            LinkedList<Integer> higherThanOpponent = new LinkedList<>();
            for (Integer ownCard : ownCards) {
                if (ownCard > maxOpponentCard) {
                    higherThanOpponent.add(ownCard);
                }
            }
            //Prüfe ob höhere Karte vorhanden ist
            if (higherThanOpponent.isEmpty()) {
                //Keine höhere Karte als der Gegner zur Verfügung
                //Spiele Karte welche dem jeweiligen Punkte-Karten Index gleich ist
                returnCard = ownCards.get(pointCards.indexOf(naechsteKarte));
            } else {
                if (higherThanOpponent.size() > 1) {
                    //Mehr als eine Karte die höher ist als der Gegner steht zur Verfügung
                    returnCard = higherThanOpponent.getLast();
                } else {
                    //Nur eine höhere Karte steht zur Verfügung
                    if (points >= 9 || pointCards.indexOf(naechsteKarte) > pointCards.size() / 2) {
                        //Spiele höchste Karte|
                        returnCard = higherThanOpponent.getLast();
                    } else {
                        //Punkte Karte ist nicht hoch genug um höchste Karte zu spielen
                        //Spiele Karte welche dem jeweiligen Punkte-Karten Index gleich ist
                        returnCard = ownCards.get(pointCards.indexOf(naechsteKarte));
                    }
                }
            }
        }

        //Berechne gelegt und offene Punkte Karten
        pointCardsPlaced.add(naechsteKarte);
        pointCards.remove((Integer) naechsteKarte);

        return returnCard;

    }

    /**
     * Verarbeitet die Informationen aus der letzten gespielten Runde um alle nötigen Variablen für die Entscheidungsstrategie zu befüllen.
     * Dabei werden die zuletzten gespielen Karten als auch die Punkte welche verteilt werden ausgelesen und verarbeitet.
     */
    private void calculateUpdateFromLastMove() {
        //Berechne gelegte Karten und verfügbare Karten // Vorheriger Zug
        if (getHdg().letzterZug(0) != -99) {
            int opponentPlayed, ownPlayed;
            ownPlayed = getHdg().letzterZug(getNummer());
            opponentPlayed = getHdg().letzterZug(getNummer() == 1 ? 0 : 1);
            ownCards.remove((Integer) ownPlayed);
            enemyCards.remove((Integer) opponentPlayed);

            //Berechne ob Stechen war oder nicht // Vorheriger Zug
            if (ownPlayed == opponentPlayed) {
                //Stechen
                //Entferne nicht verteilte Punkte Karten welche nicht mehr verteilt werden. Punkte Karten zu alt -> HolsDerGeier Framework Zeile 112
                if (pointCardsToDistribute.size() > 1) {
                    pointCardsToDistribute.remove(0);
                }
                pointCardsToDistribute.add(pointCardsPlaced.getLast());
            } else {
                //Kein Stechen
                if (!pointCardsToDistribute.isEmpty()) {
                    pointCardsToDistribute.clear();
                }
            }
        }
    }

    /**
     * Diese Methode gibt die höchste Zahl aus einer übergebenen Liste zurück.
     *
     * @param listCards Liste in der die hächste Zahl gesucht werden soll
     * @return Gibt die hächste Zahl aus der übergebenen Liste zurück
     */
    private int getHighestCard(ArrayList<Integer> listCards) {
        if (listCards.isEmpty()) {
            return -1;
        }
        int maxOwnCard = listCards.get(0);

        for (Integer enemyCard : listCards) {
            if (enemyCard > maxOwnCard) {
                maxOwnCard = enemyCard;
            }
        }

        return maxOwnCard;
    }

}