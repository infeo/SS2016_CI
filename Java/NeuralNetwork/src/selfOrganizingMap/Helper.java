package selfOrganizingMap;

import java.util.Comparator;

public class Helper implements Comparator<double []> {
	
	public int compare(double [] fst, double [] snd){
		/*
		for(int i=fst.length-1; i>=0;i--){
			if(fst[i]>snd[i]) return 1;
			if(fst[i]<snd[i]) return -1;
		}*/

		for(int i=0; i<fst.length;i++){
			if(fst[i]>snd[i]) return 1;
			if(fst[i]<snd[i]) return -1;
		}
		return 0;
	}
}
