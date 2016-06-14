package selfOrganizingMap;

import java.util.Comparator;

public class Helper implements Comparator<double []> {
	
	public int compare(double [] fst, double [] snd){
		/*
		for(int i=fst.length-1; i>=0;i--){
			if(fst[i]>snd[i]) return 1;
			if(fst[i]<snd[i]) return -1;
		}

		for(int i=0; i<fst.length;i++){
			if(fst[i]>snd[i]) return 1;
			if(fst[i]<snd[i]) return -1;
		}*/
		int l = fst.length;
		double length_fst;
		double length_snd;
		double scalar =0;
		double sum=0;
		for(int i =0; i<l;i++)sum+= Math.pow(fst[i], 2);
		length_fst=Math.sqrt(sum);
		sum=0;
		for(int i =0; i<l;i++)sum+= Math.pow(snd[i], 2);
		length_snd=Math.sqrt(sum);
		for(int i=0;i<l;i++)scalar+=fst[i]*snd[i];
		if()
		return 0;
	}
}
