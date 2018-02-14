import java.util.HashMap;
import java.util.Map;

public class FrequencyVoter implements VotingAlgorithm {
	public String vote(String[] alignments) {
		
		int[] scores = new int[alignments.length];
		
		int max = 0;
		int ind = 0;
		
		for(int i = 0; i < alignments[0].length(); i++) {
			for (int j = 0; j < alignments.length; j++) {
				for (int k = 0; k < alignments.length; k++) {
					if (alignments[j].charAt(i) == alignments[k].charAt(i)) {
						scores[j]++;
						if(scores[j] > max) {
							max = scores[j];
							ind = j;
						}
					}
				}
			}
		}
		
		
		return alignments[ind];
	}

}
