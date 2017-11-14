import java.util.List;

public interface SimilarityMetric<T extends Comparable<T>> {
	
	public double getSimilarity(Sequence seq1, Sequence seq2);
	
	public DistanceMatrix<T> getSimilarities(List<Sequence> seqs);

}
