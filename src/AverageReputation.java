import java.util.List;
import java.util.Map;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AverageReputation implements ReputationAssigner {
	
	double average = 0.0;
	double decay = 0.9;
	Map<String, Double> reputations;
	
	public AverageReputation(Map<String, Double> reputations, double decay) {
		this.reputations = reputations;
		this.decay = decay;
	}
	
	@Override
	public double calculateReputation(Mutation mutation) {
		
		if(mutation.getA().equals("admin")) {
			System.out.println("Author is admin");
			average = 0.5;
		}
		else {
			average = decay*average + (1 - decay)*reputations.get(mutation.getA());
		}
		
		return average;
	}

}
