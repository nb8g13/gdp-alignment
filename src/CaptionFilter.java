import java.util.List;

public interface CaptionFilter {
	public List<Caption> filter(List<Mutation> mutations, List<Caption> captions);
	
}
