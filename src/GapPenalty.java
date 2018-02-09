import java.util.List;
/**
 * Interface for gap penalties
 * @author LukeStacey
 *
 */
public interface GapPenalty {
	
	public List<Double> openCost(Profile prof);
	
	public List<Double> closeCost(Profile prof);
	
	//Should be a list but lazy 
	public double extenstionCost();
}
