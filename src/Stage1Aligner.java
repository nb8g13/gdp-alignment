
public class Stage1Aligner implements Aligner {
	
	SubstitutionMatrix sub;
	ProfileMerger pm;
	ScoringSystem ss;
	SimilarityMetric<Double> sim;
	
	public Stage1Aligner(SubstitutionMatrix sub) {
		this.sub = sub;
		this.ss = new PSP();
		this.pm = new NMW(ss, sub);
		this.sim = new KmerMetric(3);
	}
	
	// This code needs a clean up
	@Override
	public String[] align(String[] strs) {
		
		TreeBuilder tb = new UPGMATreeBuilder(strs, this.sim, this.pm);
		
		Profile finalProfile = tb.clusterToCompletion();
		
		String[] alignments = new String[finalProfile.getSequences().size()];
		
		for(int i = 0; i < alignments.length; i++) {
			alignments[i] = finalProfile.getSequences().get(i).getSeq();
		}
		
		return alignments;
		
	}
	
}
