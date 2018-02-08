import java.util.List;
/**
 *  Interface for all ways to generate history of edits from mutations
 * @author LukeStacey
 *
 */
public interface HistoryGenerator {
	
	public List<Caption> getHistory(List<Mutation> mutations);

}
