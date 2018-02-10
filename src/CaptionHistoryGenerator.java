import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
/**
 * Class for turning mutations into captions
 * @author LukeStacey
 *
 */
public class CaptionHistoryGenerator implements HistoryGenerator {
	
	ReputationAssigner ra;
	/**
	 * Constructor
	 * @param ra A ReputationAssigner in charge of giving the captions reputations
	 */
	public CaptionHistoryGenerator(ReputationAssigner ra) {
		this.ra = ra;
	}
	
	/**
	 * Constructs a list of captions from a list of mutations
	 * @param mutations a list of mutations
	 * @return a list of captions built from the mutations
	 */
	public List<Caption> getHistory(List<Mutation> mutations) {
		Iterator<Mutation> iter = mutations.iterator();
		List<Caption> captions = new ArrayList<Caption>();
		
		Caption cap = new Caption("", 0.0);
		while(iter.hasNext()) {
			Mutation current = iter.next();
			double rep = ra.calculateReputation(current);
			cap = current.getO().applyEdit(cap, rep);
			captions.add(cap);
		}
		
		return captions;
	}

}
