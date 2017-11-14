import java.util.List;

public interface TreeBuilder {
	
	// Performs the next clustering
	public Profile nextCluster();
	
	public boolean finished();
	
	//Skeptical as to whether we need this
	public Profile clusterToCompletion();
}
