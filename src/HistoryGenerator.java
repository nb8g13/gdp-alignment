import java.util.List;

public interface HistoryGenerator {
	
	public List<Caption> getHistory(List<Mutation> mutations);

}
