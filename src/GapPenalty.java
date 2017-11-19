import java.util.List;

public interface GapPenalty {
	
	public List<Double> openCost(Profile prof);
	
	public List<Double> closeCost(Profile prof);
	
	//Should be a list but lazy 
	public double extenstionCost();
}
