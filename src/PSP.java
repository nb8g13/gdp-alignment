import java.util.Arrays;
import java.util.List;
/**
 * Class for calculating the substitution costs with PSP
 * @author LukeStacey
 *
 */
public class PSP implements ScoringSystem {


	/**
	 * Calculates the cost of doing a substitution between two sequences at a given character in both
	 * @param seqs1 First profile's list of sequences
	 * @param seqs2 second profile's list of sequences
	 * @param subs substitution costs
	 * @param col1 column to be subbed in seqs1
	 * @param col2 column to be subbed in seqs2
	 * @return Cost of a substitution between those two columns
	 * 
	 */
	public double score(List<Sequence> seqs1, List<Sequence> seqs2, SubstitutionMatrix subs, int col1, int col2) {
		double[][] fxy = new double[subs.alphabetSize()][2];
		double sumReps1=sumReps(seqs1);
		double sumReps2=sumReps(seqs2);
		for(int i = 0; i < seqs1.size(); i++) {
			char letter = seqs1.get(i).getSeq().charAt(col1);
			if (letter != '-') {
				fxy[subs.charToIndex(letter)][0] += (1.0+seqs1.get(i).getRep()) / (seqs1.size()+sumReps1);
			}
		}
		
		for(int i = 0; i < seqs2.size(); i++) {
			char letter = seqs2.get(i).getSeq().charAt(col2);
			if (letter != '-') {
				fxy[subs.charToIndex(letter)][1]+= (1.0+seqs2.get(i).getRep()) / (seqs2.size()+sumReps2);
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
	/**
	 * Adds together all the reputations in a sequence list
	 * @param seqs list of sequences
	 * @return sum of reputations
	 */
	public double sumReps(List<Sequence> seqs) {
		double sum=0;
		for (int i=0; i<seqs.size(); i++) {
			sum+=seqs.get(i).getRep();
		}
		return sum;
	}

}
