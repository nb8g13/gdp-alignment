import java.util.ArrayList;
import java.util.List;



public class UPGMATreeBuilder implements TreeBuilder {
	
	SimilarityMetric<Double> metric;
	DistanceMatrix<Double> distanceMatrix;
	List<Profile> profiles;
	ProfileMerger merger;
	
	public UPGMATreeBuilder(String[] strings, SimilarityMetric<Double> metric, ProfileMerger merger) {
		
		this.metric = metric;
		//Fix capacity here?
		this.profiles = new ArrayList<Profile>();
		// I don't like this dependency...
		this.merger = merger;
		
		List<Sequence> seqs = new ArrayList<Sequence>();
		for (int i = 0; i < strings.length; i++) {
			seqs.add(new Sequence(strings[i]));
		}
		
		this.distanceMatrix = metric.getSimilarities(seqs);
		
		this.profiles = new ArrayList<Profile>();
		
		for (int i = 0; i < strings.length; i++) {
			profiles.add(new Profile(seqs.get(i)));
		}
	}
	

	@Override
	public Profile nextCluster() {
		
		DistanceMatrix<Double>.Value mv = this.distanceMatrix.findMax();
		
		List<Double> row1 = this.distanceMatrix.getMatrix().get(mv.getRow());
		List<Double> row2 = this.distanceMatrix.getMatrix().get(mv.getCol());
		
		row1.remove(mv.getCol());
		row1.remove(mv.getRow());
		
		row2.remove(mv.getCol());
		row2.remove(mv.getRow());
		
		List<Double> newRow = new ArrayList<Double>();
		
		for (int i = 0; i < row1.size(); i++) {
			double val = (row1.get(i) + row2.get(i)) / 2.0;
			newRow.add(val);
		}
		
		Profile pro1 = profiles.get(mv.getRow());
		Profile pro2 = profiles.get(mv.getCol());
		
		if(mv.getCol() < mv.getRow()) {
			this.distanceMatrix.removeIndex(mv.getRow());
			this.distanceMatrix.removeIndex(mv.getCol());
			this.profiles.remove(mv.getRow());
			this.profiles.remove(mv.getCol());
		}
		
		else {
			this.distanceMatrix.removeIndex(mv.getCol());
			this.distanceMatrix.removeIndex(mv.getRow());
			this.profiles.remove(mv.getCol());
			this.profiles.remove(mv.getRow());
		}
		
		this.distanceMatrix.addElement(newRow);
		
		Profile nextCluster = merger.merge(pro1,  pro2);
		
		this.profiles.add(0, nextCluster);
		
		return nextCluster();
	}
	
	public boolean finished() {
		return profiles.size() < 1;
	}
	
	public Profile clusterToCompletion() {
		
		while(profiles.size() > 1) {
			nextCluster();
		}
		
		return profiles.get(0);
	}
	
	
}
