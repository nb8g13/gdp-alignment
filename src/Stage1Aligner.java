
public class Stage1Aligner implements Aligner {
	
	SubstitutionMatrix sub;
	ProfileMerger pm;
	ScoringSystem ss;
	SimilarityMetric<Double> sim;
	
	public Stage1Aligner(SubstitutionMatrix sub) {
		this.sub = sub;
		this.ss = new PSP();
		//nmw -> MuscleMerger
		this.pm = new MUSCLEMerge(ss, sub, new MUSCLEPenaliser(-8, 1));
		this.sim = new KmerMetric(3);
	}
	
	@Override
	public String[] align(String[] strs) {
		
		TreeBuilder tb = new UPGMATreeBuilder(strs, this.sim, this.pm);
		
		
		
		Profile finalProfile = tb.clusterToCompletion();
		
		for (int i=0; i<finalProfile.getSequences().size(); i++) {
			System.out.println(finalProfile.getSequences().get(i).getIndex());
		}
		
		String[] alignments = new String[finalProfile.getSequences().size()];
		
		for(int i = 0; i < alignments.length; i++) {
			alignments[i] = finalProfile.getSequences().get(i).getSeq();
		}
		
		return alignments;
		
	}
	
}
