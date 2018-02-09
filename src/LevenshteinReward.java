import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.text.similarity.LevenshteinDistance;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.DatabaseReference.CompletionListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LevenshteinReward implements ReputationAward {

	@Override
	public void award(String winner, List<Mutation> mutations, List<Caption> captions) {
		
		Map<String, Double> reputationGain = new HashMap<String, Double>();
		
		String current = "";
		
		LevenshteinDistance ld = new LevenshteinDistance();
		
		for(int i = 0; i < captions.size(); i++) {
			
			if(!mutations.get(i).getA().equals("admin")) {
				Caption caption = captions.get(i);
				String withMutation = caption.getText();
				
				int dist1 = ld.apply(winner, current);
				int dist2 = ld.apply(winner, withMutation);
				
				String author = mutations.get(i).getA();
				
				if(reputationGain.containsKey(author)) {
					reputationGain.put(author, reputationGain.get(i) + dist1 - dist2);
				}
				
				else {
					reputationGain.put(author, (double) dist1 - dist2);
				}
			}
		}
			
		Iterator<Entry<String, Double>> iter = reputationGain.entrySet().iterator();
		List<ReputationTransaction> rts = new ArrayList<ReputationTransaction>();
		
		while(iter.hasNext()) {
			Entry<String, Double> entry = iter.next();
				
			if(!entry.getKey().equals("admin")) {
				DatabaseReference ref = FirebaseDatabase
						.getInstance()
						.getReference("/users/" + entry.getKey());
					
				ReputationTransaction rt = new ReputationTransaction(entry.getValue());
				ref.runTransaction(rt);
				rts.add(rt);
			}
		}
			
		boolean done = false;
			
		while(!done) {
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			done = true;
			for(int i = 0; i < rts.size(); i++) {
				if(!rts.get(i).isDone()) {
					done = false;
					break;
				}
			}
		}		
	}
	
}
