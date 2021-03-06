import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
/**
 * Class for calculating the kMer distances
 * @author LukeStacey
 *
 */
public class KmerMetric implements SimilarityMetric<Double> {
	
	int k;
	/**
	 * Constructor
	 * @param k length of kmers
	 */
	public KmerMetric(int k) {
		this.k = k;
	}
	/**
	 * counts each kmer in a sequence
	 * @param seq1 the sequence to be checked for kmers
	 * @return A map from all substrings in sequence to the number of times they occur
	 */
	public Map<String, Integer> kCount(Sequence seq1) {
		String str = seq1.getSeq();
		int len = str.length();
		Map<String, Integer> kmap = new HashMap<String, Integer>();
		
		if (len > this.k) {
			for (int i = 0; i < len - this.k + 1; i++) {
				String substr = str.substring(i, this.k+i);
				if (kmap.containsKey(substr)) {
					kmap.put(substr, kmap.get(substr) + 1);
				}
				
				else {
					kmap.put(substr, 1);
				}
			}
		}
		
		return kmap;
	}
	/**
	 * Gets the Kmer distance between 2 strings
	 * @param seq1 first sequence to be compared
	 * @param seq2 second sequence to be compared
	 * @return the normalised kmer distance between 2 sequences
	 */
	public double getSimilarity(Sequence seq1, Sequence seq2) {
		Map<String, Integer> kmap1 = kCount(seq1);
		Map<String, Integer> kmap2 = kCount(seq2);
		
		return normalize(seq1, seq2, countComparison(kmap1, kmap2));
	}
	/**
	 * Takes two kmer counts and compares them
	 * @param kmap1 kmer count of first sequence
	 * @param kmap2 kmer count of second sequence
	 * @return number of common kmers
	 */
	public double countComparison(Map<String, Integer> kmap1, Map<String, Integer> kmap2) {
		double accum = 0.0;
		
		Iterator<Map.Entry<String, Integer>> iter = kmap1.entrySet().iterator();
		
		while(iter.hasNext()) {
			Map.Entry<String, Integer> entry = iter.next();
			int count1 = entry.getValue();
			
			if (kmap2.containsKey(entry.getKey())) {
				int count2 = kmap2.get(entry.getKey());
				
				if(count1 >= count2) {
					accum += count2;
				}
				
				else {
					accum += count1;
				}
			}
		}
		
		return accum;
	}
	/**
	 * Normalises kmer distance with respect to length of sequence
	 * @param seq1 sequence 1
	 * @param seq2 sequence 2
	 * @param val the number of shared kmers
	 * @return normalised kmer distance
	 */
	public double normalize(Sequence seq1, Sequence seq2, double val) {
		int len1 = seq1.getSeq().length();
		int len2 = seq2.getSeq().length();
		
		if (len1 > len2) {
			return val / len2;
		}
		
		else {
			return val / len1;
		}
	}
	
	/**
	 * Calculates the distance matrix between a list of sequences
	 * @param seqs a list of sequences
	 * @return The distance matrix between all the sequences
	 */
	public DistanceMatrix<Double> getSimilarities(List<Sequence> seqs) {
		List<Map<String, Integer>> kmaps = new ArrayList<Map<String, Integer>>();
		List<List<Double>> matrix = new ArrayList<List<Double>>();
		
		for(int i = 0; i < seqs.size(); i++) {
			matrix.add(new ArrayList<Double>());
		}
		
		for (int i = 0; i < seqs.size(); i++) {
			kmaps.add(kCount(seqs.get(i)));
		}
		
		for (int i = 0; i < kmaps.size(); i++) {
			for( int j = i; j < kmaps.size(); j++) {
				double val = countComparison(kmaps.get(i), kmaps.get(j));
				matrix.get(i).add(normalize(seqs.get(i), seqs.get(j), val));
			}
		}
		
		for(int i = 0; i < seqs.size(); i++) {
			for (int j = 0; j < i; j++) {
				matrix.get(i).add(j, matrix.get(j).get(i));
			}
		}
		
		return new DistanceMatrix<Double>(matrix, -1.0);
	}
}
