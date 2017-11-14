import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class KmerMetric implements SimilarityMetric<Double> {
	
	int k;
	
	public KmerMetric(int k) {
		this.k = k;
	}
	
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

	@Override
	public double getSimilarity(Sequence seq1, Sequence seq2) {
		Map<String, Integer> kmap1 = kCount(seq1);
		Map<String, Integer> kmap2 = kCount(seq2);
		
		return normalize(seq1, seq2, countComparison(kmap1, kmap2));
	}
	
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

	@Override
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
