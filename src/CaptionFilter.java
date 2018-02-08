import java.util.List;
/**
 * Interface for classes that implement different ways to filter captions
 * @author LukeStacey
 *
 */
public interface CaptionFilter {
	public List<Caption> filter(List<Mutation> mutations, List<Caption> captions);
	
}
