

import java.util.ArrayList;

/**
 * Autoren: T.Kaiser und O.Hagemeyer
 * Datum: 07.01.2011
 * Klasse zur Implementierung unserer Spiellogik
 */
public class SpielerKaiserHagemeyer extends HolsDerGeierSpieler {

    /* In diesem Array stehen die von uns noch nicht gespielten Karten*/
    private ArrayList<Integer> nochNichtGespieltKH=new ArrayList<Integer>();
    /* In diesem Array stehen die vom Gegner noch nicht gespielten Karten*/
    private ArrayList<Integer> vomGegnerNochNichtGelegt=new ArrayList<Integer>();
    /* In diesem Array stehen die noch zu gewinnenden Karten */
    private ArrayList<Integer> nochZuGewinnen=new ArrayList<Integer>();

    /* Spielbereich1 */
    private ArrayList<Integer> spielbereich1=new ArrayList<Integer>();
    /* Spielbereich2 */
    private ArrayList<Integer> spielbereich2=new ArrayList<Integer>();
    /* Spielbereich3 */
    private ArrayList<Integer> spielbereich3=new ArrayList<Integer>();
    /* Spielbereich4 */
    private ArrayList<Integer> spielbereich4=new ArrayList<Integer>();
    /* Spielbereich5 */
    private ArrayList<Integer> spielbereich5=new ArrayList<Integer>();

    // Konstruktor
    public SpielerKaiserHagemeyer() {
    }

    public void reset() {
        // noch zu gewinnende Karten füllen (size: 16)
        nochZuGewinnen.clear();
        for (int i=10;i>-6;i--) {
            nochZuGewinnen.add(i);
        }

        // vom Gegner noch nicht gelegte Karten
        vomGegnerNochNichtGelegt.clear();
        for (int i=15;i>0;i--) {
            vomGegnerNochNichtGelegt.add(i);
        }

        // von Kaiser/Hagemeyer noch nicht gespielt
        for (int i=1;i<16;i++){
            nochNichtGespieltKH.add(i);
        }
    }

    public int gibKarte(int naechsteKarte) {

    /* SPIELLOGIK
    1. Priorisierung in einzelne Spielbereiche nach "Wichtigkeit der zu gewinnenden Karten" -->
    "Statische" Zuordnung von den gespielten Geier- und Mäusekarten (naechste Karte)
    zu unseren Karten im Array "nochNichtGespieltKH"
    --> Ablage in jeweils 5 Arrays (spielbereich1 bis spielbereich5)

    STATISCHE ORDNUNG - Spielbereiche definieren:
    naechsteKarte           nochNichtGespieltKH
    10, 9, 8                10, 11, 12
    7, 6, 5                 13, 14, 15
    4, 3, 2                 7, 8, 9
    1, -1, -2               1, 2, 3
    -3, -4, -5              4, 5, 6

    2. Zugriff auf eines der bis zu drei gespielten Karten per RANDOM-Zugriff
    */

    // Variable für die von uns gespielte Karte
    int ret=-99;

    // 1. Nur vor dem ersten ausgeführten Spielzug: Füllen der Random Arrays
    if (nochZuGewinnen.size()== 16) {
        //Spielbereich 1
        for (int j=nochNichtGespieltKH.size()-4; j>=nochNichtGespieltKH.size()-6;j--) {
            spielbereich1.add(nochNichtGespieltKH.get(j));
        }

        //Spielbereich 2
        for (int j=nochNichtGespieltKH.size()-1; j>=nochNichtGespieltKH.size()-3;j--) {
            spielbereich2.add(nochNichtGespieltKH.get(j));
        }

        //Spielbereich 3
        for (int j=nochNichtGespieltKH.size()-7; j>=nochNichtGespieltKH.size()-9;j--) {
            spielbereich3.add(nochNichtGespieltKH.get(j));
        }

        //Spielbereich 4
        for (int j=nochNichtGespieltKH.size()-10; j>=nochNichtGespieltKH.size()-12;j--) {
            spielbereich4.add(nochNichtGespieltKH.get(j));
        }

        //Spielbereich 5
        for (int j=nochNichtGespieltKH.size()-13; j>=nochNichtGespieltKH.size()-15;j--) {
            //Random-Array - STATISCHE ZUORDNUNG
            spielbereich5.add(nochNichtGespieltKH.get(j));
        }
    }

    /* 2. Zugriff auf Spielbereiche + Abfrage der nächsten Karte */
    // Rückgabe auf 0 setzen
    ret = 0;
    if (naechsteKarte<11 && naechsteKarte>7) {
        // Solange Rückgabe = 0 --> neue Zahl
        while (ret == 0) {
            //Random-Zahl aus den Zahlen ermitteln
            int index= (int) (Math.random() *spielbereich1.size());
            ret = spielbereich1.get(index);
        }
        spielbereich1.set(spielbereich1.indexOf(ret), 0);
        nochNichtGespieltKH.set(ret-1, 0);
    } else if (naechsteKarte <8 && naechsteKarte >4) {
        // Solange Rückgabe = 0 --> neue Zahl
        while (ret == 0) {
            //Random-Zahl aus den Zahlen ermitteln
            int index= (int) (Math.random() *spielbereich2.size());
            ret = spielbereich2.get(index);
        }
        spielbereich2.set(spielbereich2.indexOf(ret), 0);
        nochNichtGespieltKH.set(ret-1, 0);
    } else if (naechsteKarte <5 && naechsteKarte >1) {
        // Solange Rückgabe = 0 --> neue Zahl
        while (ret == 0) {
            //Random-Zahl aus den Zahlen ermitteln
            int index= (int) (Math.random() *spielbereich3.size());
            ret = spielbereich3.get(index);
        }
        spielbereich3.set(spielbereich3.indexOf(ret), 0);
        nochNichtGespieltKH.set(ret-1, 0);
    } else if (naechsteKarte <-2 && naechsteKarte >-6) {
        // Solange Rückgabe = 0 --> neue Zahl
        while (ret == 0) {
            //Random-Zahl aus den Zahlen ermitteln
            int index= (int) (Math.random() *spielbereich4.size());
            ret = spielbereich4.get(index);
        }
        spielbereich4.set(spielbereich4.indexOf(ret), 0);
        nochNichtGespieltKH.set(ret-1, 0);
    } else if (naechsteKarte <2 && naechsteKarte >-3) {
        // Solange Rückgabe = 0 --> neue Zahl
        while (ret == 0) {
            //Random-Zahl aus den Zahlen ermitteln
            int index= (int) (Math.random() *spielbereich5.size());
            ret = spielbereich5.get(index);
        }
        spielbereich5.set(spielbereich5.indexOf(ret), 0);
        nochNichtGespieltKH.set(ret-1, 0);
    }

   //letzte gespielte Karte löschen
   nochZuGewinnen.remove(nochZuGewinnen.indexOf(naechsteKarte));
   // Rückgabe unserer Karte
   return ret;
   }
}
