import java.util.List;
/**
 * Interface for various ways to assign reputations
 * @author LukeStacey
 *
 */
public interface ReputationAssigner {
	public double calculateReputation(List<Mutation> mutations);
}
