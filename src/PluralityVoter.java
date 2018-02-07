import java.util.HashMap;
import java.util.Map;

public class PluralityVoter implements VotingAlgorithm{
	
	public String vote(String[] alignments) {
		String consensus="";
		for (int j = 0; j < alignments[0].length(); j++) {
			Map<Character, Integer> seen = new HashMap<Character, Integer>();
			int max = 0;
			Character winning = null;
			
			for(int i = 0; i < alignments.length; i++) {
				String current = alignments[i];
				Character c = current.charAt(j);
				int newCount = 0;
				
				if(seen.containsKey(c) ) {
					newCount = seen.get(c)+1;
				}
				
				else {
					newCount = 1;
				}
				
				seen.put(c, newCount);
				
				if (newCount >= max) {
					max = newCount;
					winning = c;
				}
			}
			
			consensus = consensus + winning;
		}

		
		return consensus;
	}

}

				