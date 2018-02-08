import java.util.List;
/**
 * Interface for making various classes comparable
 * @author LukeStacey
 *
 * @param <T> the type of objecs it can be comparable
 */
public interface SimilarityMetric<T extends Comparable<T>> {
	
	public double getSimilarity(Sequence seq1, Sequence seq2);
	
	public DistanceMatrix<T> getSimilarities(List<Sequence> seqs);

}
