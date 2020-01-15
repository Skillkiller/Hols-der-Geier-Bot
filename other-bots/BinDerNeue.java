public class BinDerNeue extends HolsDerGeierSpieler {
	
	
	public void reset(){
		
	}
	
	public int gibKarte(int naechsteKarte){
		switch(naechsteKarte){
		case -5:
			return 8;
		case -4:
			return 9;
		case -3:
			return 1;
		case -2:
			return 2;
		case -1:
			return 3;
		case 1:
			return 4;
		case 2:
			return 5;
		case 3:
			return 6;
		case 4:
			return 7;
		case 5:
			return 10;
		case 6:
			return 11;
		case 7:
			return 12;
		case 8:
			return 13;
		case 9:
			return 15;
		case 10:
			return 14;
		default:
			return 5;
		}
	}
	
}
