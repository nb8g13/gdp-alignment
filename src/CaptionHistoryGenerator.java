import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CaptionHistoryGenerator implements HistoryGenerator {
	
	ReputationAssigner ra;
	
	public CaptionHistoryGenerator(ReputationAssigner ra) {
		this.ra = ra;
	}
	
	@Override
	public List<Caption> getHistory(List<Mutation> mutations) {
		Iterator<Mutation> iter = mutations.iterator();
		//List<Mutation> seen = new ArrayList<Mutation>();
		List<Caption> captions = new ArrayList<Caption>();
		
		Caption cap = new Caption("", 0.0);
		while(iter.hasNext()) {
			Mutation current = iter.next();
			//seen.add(current);
			cap = current.getO().applyEdit(cap, ra.calculateReputation(current));
			captions.add(cap);
		}
		
		return captions;
	}

}
