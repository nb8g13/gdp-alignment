import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.similarity.LevenshteinDistance;

public class PluralityPicker implements VotingAlgorithm {
		
	public String vote(String[] alignments) {
		
		String consensus = new PluralityVoter().vote(alignments);
		LevenshteinDistance ld = new LevenshteinDistance();
		
		int min = Integer.MAX_VALUE;
		String winning = null;
		
		for(int i = 0; i < alignments.length; i++) {
			int dist = ld.apply(consensus, alignments[i]);
			if(dist <= min) {
				min = dist;
				winning = alignments[i];
			}
		}
		
		return winning;
	}
	
}
