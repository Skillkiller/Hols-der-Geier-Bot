import java.util.ArrayList;
import java.util.LinkedList;

public class Template extends HolsDerGeierSpieler {

    ArrayList<Integer> ownCards = new ArrayList<>();
    LinkedList<Integer> ownCardsPlaced = new LinkedList<>();

    ArrayList<Integer> enemyCards = new ArrayList<>();
    LinkedList<Integer> enemyCardsPlaced = new LinkedList<>();

    ArrayList<Integer> pointCards = new ArrayList<>();
    LinkedList<Integer> pointCardsPlaced = new LinkedList<>();
    ArrayList<Integer> pointCardsToDistribute = new ArrayList<>();

    int ownPoints = 0;
    int enemyPoints = 0;
    
    public void reset() {
        ownPoints = 0;
        enemyPoints = 0;
        enemyCards.clear();
        enemyCardsPlaced.clear();
        ownCards.clear();
        ownCardsPlaced.clear();

        for (int i = 1; i < 16; i++) {
            ownCards.add(i);
            enemyCards.add(i);
        }

        for (int i = 0; i < 5; i++) {
            pointCards.add(-5 + i);
        }

        for (int i = 1; i < 11; i++) {
            pointCards.add(i);
        }

    }

    public int gibKarte(int naechsteKarte) {
        //Berechne gelegte Karten und verfügbare Karten /Vorheriger Zug
        if (getHdg().letzterZug(0) != -99) {
            int points = pointCardsPlaced.getLast();
            int enemyPlaced, ownPlaced;
            ownPlaced = getHdg().letzterZug(getNummer());
            enemyPlaced = getHdg().letzterZug(getNummer() == 1 ? 0 : 1);
            ownCardsPlaced.add(ownPlaced);
            ownCards.remove(ownCards.indexOf(ownPlaced));
            enemyCardsPlaced.add(enemyPlaced);
            enemyCards.remove(enemyCards.indexOf(enemyPlaced));

            //Berechne Punktestand // Vorheriger Zug
            if (ownPlaced == enemyPlaced) {
                pointCardsToDistribute.add(pointCardsPlaced.getLast());
            } else {
                if (!pointCardsToDistribute.isEmpty()) {
                    for (Integer integer : pointCardsToDistribute) {
                        points += integer;
                    }
                    pointCardsToDistribute.clear();
                }

                if (points > 0) {
                    //Der Spieler mit der höchsten Karte bekommt die Punkte
                    if (ownPlaced > enemyPlaced) {
                        ownPoints += points;
                    } else {
                        enemyPoints += points;
                    }
                } else {
                    //Der Spieler mit der niedrigsten Karte bekommt die Punkte
                    if (ownPlaced < enemyPlaced) {
                        ownPoints += points;
                    } else {
                        enemyPoints += points;
                    }
                }
            }
        }


        //Entscheiden welche Spielkarte gespielt werden soll.
        // returnCard = [Karte die gespielt werden soll]
        int returnCard = 0;

        //Berechne gelegt und offene Punkte Karten
        pointCardsPlaced.add(naechsteKarte);
        pointCards.remove(pointCards.indexOf(naechsteKarte));

        return returnCard;

    }


}
