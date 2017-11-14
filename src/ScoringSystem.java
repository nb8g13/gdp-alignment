import java.util.List;

public interface ScoringSystem {
	
	public double score(List<Sequence> seqs1, List<Sequence> seqs2, SubstitutionMatrix subs, int col1, int col2);

}
