import java.util.List;
import java.util.Map;

public interface ReputationAward {
	
	public void award(String winner, List<Mutation> mutations, List<Caption> caption, Map<String, Double> reputations);
	
}
