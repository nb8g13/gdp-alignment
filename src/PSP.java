import java.util.Arrays;
import java.util.List;

public class PSP implements ScoringSystem {


	@Override
	public double score(List<Sequence> seqs1, List<Sequence> seqs2, SubstitutionMatrix subs, int col1, int col2) {
		double[][] fxy = new double[subs.alphabetSize()][2];
		
		for(int i = 0; i < seqs1.size(); i++) {
			char letter = seqs1.get(i).getSeq().charAt(col1);
			if (letter != '-') {
				fxy[subs.charToIndex(letter)][0] += 1.0 / seqs1.size();
			}
		}
		
		for(int i = 0; i < seqs2.size(); i++) {
			char letter = seqs2.get(i).getSeq().charAt(col2);
			if (letter != '-') {
				fxy[subs.charToIndex(letter)][1]+= 1.0 / seqs2.size();
			}
		}
		
		double sum = 0;
		
		for (int i = 0; i < subs.alphabetSize(); i++) {
			for (int j = 0; j < subs.alphabetSize(); j++) {
				sum += fxy[i][0] * fxy[j][1] * subs.getSubScore(i, j);
			}
		}
		
		//System.out.println("Printing X frequencies: " + fxy[0][0] +", " + fxy[1][0] +", "+ fxy[2][0] +","+  fxy[3][0]);
		//System.out.println("Printing Y frequencies: " + fxy[0][1] +", " + fxy[1][1] +", "+ fxy[2][1] +","+  fxy[3][1]);
		//System.out.println("Printing substitution cost for (" + col1 +", " + col2 + "): " + sum);
		return sum;
	}

}
