import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class NMW implements ProfileMerger {
	
	ScoringSystem scoring;
	SubstitutionMatrix subs;
	
	public NMW(ScoringSystem scoring, SubstitutionMatrix subs) {
		this.scoring = scoring;
		this.subs = subs;
	}
	
	// This does NOT deal w/ gap penalties of different length yet
	// This may difficult to implement in backwards pass :(
	@Override
	public Profile merge(Profile left, Profile right) {
		
		int rows = left.getSequences().size();
		int cols = right.getSequences().size();
		
		double[][] scores = new double[rows+1][cols+1];
		
		scores[0][0] = 0;
		
		// i = 0; two choices
		for (int j = 1; j <= cols; j++) {
			scores[0][j] = scores[0][j-1] - subs.deletionCost(); 
		}
		
		//j = 0; two choices
		for (int i = 1; i <= rows; i++) {
			scores[i][0] = scores[i][0] - subs.deletionCost();
		}
		
		// three choices
		for (int i = 1; i <= rows; i++) {
			for (int j = 1; j <= cols; j++) {
				
				double sideways = scores[i][j-1] - subs.deletionCost();
				double vertical = scores[i-1][j] - subs.deletionCost();
				double substitute = scores[i-1][j-1] + scoring.score(left.getSequences(), right.getSequences(), subs, i, j);
				
				if (sideways >= vertical && sideways >= substitute) {
					scores[i][j] = sideways;
				}
				
				else if (vertical >= substitute) {
					scores[i][j] = vertical;
				}
				
				else {
					scores[i][j] = substitute;
				}
			}
		}
		
		//Backwards pass
		
		List<Sequence> newSeqs1 = new ArrayList<Sequence>();
		List<Sequence> newSeqs2 = new ArrayList<Sequence>();
		
		//Populate sequence lists
		for (int i = 0; i < left.getSequences().size(); i++) {
			newSeqs1.add(new Sequence(""));
		}
		
		for (int j = 0; j < right.getSequences().size(); j++) {
			newSeqs2.add(new Sequence(""));
		}
		
		int crow = rows;
		int ccol = cols;
		
		while (crow != 0 || ccol != 0) {
			
			//sideways move
			if (ccol != 0 && scores[crow][ccol] == scores[crow][ccol-1] - subs.deletionCost()) {
				for (int i = 0; i < newSeqs1.size(); i++) {
					newSeqs1.get(i).prependSeq('-');
				}
				
				for (int j = 0; j < newSeqs2.size(); j++) {
					newSeqs2.get(j).prependSeq(right.getSequences().get(j).getSeq().charAt(ccol));
				}
				
				ccol--;
			}
			
			//vertical move
			else if(crow != 0 && scores[crow][ccol] == scores[crow-1][ccol] - subs.deletionCost()) {
				for(int i = 0; i < newSeqs1.size(); i++) {
					newSeqs1.get(i).prependSeq(left.getSequences().get(i).getSeq().charAt(crow));
				}
				
				for(int j = 0; j < newSeqs2.size(); j++) {
					newSeqs2.get(j).prependSeq('-');
				}
				
				crow--;
				
			}
			
			//diagonal move
			else {
				for(int i = 0; i < newSeqs1.size(); i++) {
					newSeqs1.get(i).prependSeq(left.getSequences().get(i).getSeq().charAt(crow));
				}
				
				for (int j = 0; j < newSeqs2.size(); j++) {
					newSeqs2.get(j).prependSeq(right.getSequences().get(j).getSeq().charAt(ccol));
				}
				
				crow--;
				ccol--;
			}
		}
		
		List<Sequence> alignments = Stream.concat(newSeqs1.stream(), newSeqs2.stream()).collect(Collectors.toList());
		
		return new Profile(left, right, alignments);
	}
}
