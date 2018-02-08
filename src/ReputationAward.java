import java.util.List;

public interface ReputationAward {
	
	public void award(String winner, List<Mutation> mutations, List<Caption> caption);
	
}
