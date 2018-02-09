import java.util.List;
/**
 * Interface for various treebuilding algorithms
 * @author LukeStacey
 *
 */
public interface TreeBuilder {
	
	// Performs the next clustering
	public Profile nextCluster();
	
	public boolean finished();
	
	//Skeptical as to whether we need this
	public Profile clusterToCompletion();
}
