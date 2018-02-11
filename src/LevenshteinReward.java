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
	public void award(String winner, List<Mutation> mutations, List<Caption> captions, Map<String, Double> reputations) {
		
		String current = "";
		
		LevenshteinDistance ld = new LevenshteinDistance();
		
		for(int i = 0; i < captions.size(); i++) {
			
			Mutation mut = mutations.get(i);
			Caption caption = captions.get(i);
			
			if(!mut.getA().equals("admin")) {

				String withMutation = caption.getText();
				
				int dist1 = ld.apply(winner, current);
				int dist2 = ld.apply(winner, withMutation);
				
				String author = mutations.get(i).getA();
				
				reputations.put(author, reputations.get(author) + dist1 - dist2);
			}
			
			current = caption.getText();
		}
	}
}
