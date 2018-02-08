import java.util.List;
/**
 * Interface for various scoring systems for profile mergers
 * @author LukeStacey
 *
 */
public interface ScoringSystem {
	
	public double score(List<Sequence> seqs1, List<Sequence> seqs2, SubstitutionMatrix subs, int col1, int col2);

}
