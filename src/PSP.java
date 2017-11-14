import java.util.List;

public class PSP implements ScoringSystem {


	@Override
	public double score(List<Sequence> seqs1, List<Sequence> seqs2, SubstitutionMatrix subs, int col1, int col2) {
		int[][] fxy = new int[subs.alphabetSize()][2];
		
		for(int i = 0; i < seqs1.size(); i++) {
			fxy[subs.charToIndex(seqs1.get(i).getSeq().charAt(col1))][0]++;
		}
		
		for(int i = 0; i < seqs2.size(); i++) {
			fxy[subs.charToIndex(seqs2.get(i).getSeq().charAt(col2))][1]++;
		}
		
		double sum = 0;
		
		for (int i = 0; i < seqs1.size(); i++) {
			for (int j = 0; j < seqs2.size(); j++) {
				sum += fxy[i][0] * fxy[j][1] * subs.getSubScore(i, j);
			}
		}
		return sum;
	}

}
