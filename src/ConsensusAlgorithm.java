import java.util.List;
/**
 * Interface for Algorithms that implement a consensus algorithm, one that gets a single string from a list of mutations
 * @author LukeStacey
 *
 */
public interface ConsensusAlgorithm {
	
	public String getConsensus(List<Mutation> mutations);
}
