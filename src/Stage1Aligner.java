/**
 * Class for aligning a set of strings
 * @author LukeStacey
 *
 */
public class Stage1Aligner implements Aligner {
	
	SubstitutionMatrix sub;
	ProfileMerger pm;
	ScoringSystem ss;
	SimilarityMetric<Double> sim;
	/**
	 * Constructor
	 * @param sub substitution matrix to be used
	 */
	public Stage1Aligner(SubstitutionMatrix sub) {
		this.sub = sub;
		this.ss = new PSP();
		//nmw -> MuscleMerger
		this.pm = new MUSCLEMerge(ss, sub, new MUSCLEPenaliser(-1, 1));
		this.sim = new KmerMetric(3);
	}
	
	/**
	 * Aligns a set of strings
	 * @param strs array of strings to be aligned
	 * @param reps array of reputations associated with strings
	 * @return  list of aligned strings with preserved ordering
	 */
	public String[] align(String[] strs, double[] reps) {
		
		TreeBuilder tb = new UPGMATreeBuilder(strs, this.sim, this.pm, reps);
		
		
		
		Profile finalProfile = tb.clusterToCompletion();
		
		/*
		for (int i=0; i<finalProfile.getSequences().size(); i++) {
			System.out.println(finalProfile.getSequences().get(i).getIndex());
		}*/
		
		String[] alignments = new String[finalProfile.getSequences().size()];
		
		for(int i = 0; i < alignments.length; i++) {
			alignments[i] = finalProfile.getSequences().get(i).getSeq();
		}
		
		return alignments;
		
	}
	
}
