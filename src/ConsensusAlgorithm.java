import java.util.List;

public interface ConsensusAlgorithm {
	
	public Mutation getConsensus(List<Mutation> mutations);
}
