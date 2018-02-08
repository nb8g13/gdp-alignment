import java.util.List;

public class PluralityConsensus implements ConsensusAlgorithm {
	
	HistoryGenerator hg = new CaptionHistoryGenerator(new LastEditorReputation());
	CaptionFilter cf = new FinishedCaptionFilter();
	Aligner aligner;
	CaptionCleaner cleaner;
	VotingAlgorithm va;
	
	public PluralityConsensus(SubstitutionMatrix subs, CaptionCleaner cleaner, VotingAlgorithm va) {
		this.aligner = new Stage1Aligner(subs);
		this.cleaner = cleaner;
		this.va = va;
	}
	
	@Override
	public String getConsensus(List<Mutation> mutations) {
		List<Caption> captions = hg.getHistory(mutations);
		List<Caption> candidates = cf.filter(mutations, captions);
		String[] text = new String[candidates.size()];
		double[] reps = new double[candidates.size()];
		
		for (int i = 0; i < candidates.size(); i++) {
			Caption current = candidates.get(i);
			text[i] = cleaner.clean(current);
			reps[i] = current.getReputation();
		}
		
		System.out.println("Arrays made");
		
		String[] alignments = aligner.align(text, reps);
		
		System.out.println("Alignment finished");
		String winner = va.vote(alignments);
		
		for(int i = 0; i < captions.size(); i++) {
			String cleaned = cleaner.clean(captions.get(i));
			if (cleaned.equals(winner)) return captions.get(i).getText();
		}
		
		return null;
	}
	
}
