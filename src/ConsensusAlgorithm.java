import java.util.List;

public interface ConsensusAlgorithm {
	
	public String getConsensus(List<Mutation> mutations);
}
