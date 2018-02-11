import java.util.List;
import java.util.Map;

public class Middleware {
	
	List<Mutation> mutations;
	Map<String, Double> reputations;
	String alphabet;
	SubstitutionMatrix subs;
	
	public Middleware(String alphabet, SubstitutionMatrix subs) {
		this.alphabet = alphabet;
		this.subs = subs;
	}
	
	public void updateBackend() {
		MutationReader mr = new MutationReader();
		List<Mutation> mutations = mr.getMutations("tmp\\history.json");
		
		ReputationReader rr = new ReputationReader();
		Map<String, Double> reputations = rr.getRepMap("tmp\\reputations.json");
		
		HistoryGenerator hg = new CaptionHistoryGenerator(new AverageReputation(reputations, 0.9));
		List<Caption> captions = hg.getHistory(mutations, reputations);
		
		ConsensusAlgorithm ca = new PluralityConsensus(subs, new CapsNoPunctuation(), new PluralityPicker());
		
		String winner = ca.getConsensus(mutations, captions);
		
		ReputationAward awarder = new LevenshteinReward();
		
		awarder.award(winner, mutations, captions, reputations);
		
		ReputationWriter rw = new ReputationWriter();
		rw.writeReputations("tmp\\newReputations.json", reputations);
		
		WinnerWriter ww = new WinnerWriter();
		ww.writeWinner("tmp\\winner.json", winner);
	}
	
}
