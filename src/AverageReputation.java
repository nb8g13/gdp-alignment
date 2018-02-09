import java.util.List;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AverageReputation implements ReputationAssigner {
	
	double average = 0.0;
	double decay = 0.9;
	
	@Override
	public double calculateReputation(Mutation mutation) {
		
		if (mutation.getA().equals("admin")) {
			return 0.5;
		}
		
		DatabaseReference ref = FirebaseDatabase
				.getInstance()
				.getReference("/users/" + mutation.getA() + "/repScore");
		
		
		class RepListener implements ValueEventListener {
			
			boolean done = false;
			
			@Override
			public void onCancelled(DatabaseError arg0) {
				System.err.println("Could not recover reputation for user: " + mutation.getA());
			}

			@Override
			public void onDataChange(DataSnapshot snap) {
				double rep = snap.getValue(Double.class);
				if(average == 0.0) {
					average = rep;
				}
				
				else {
					average = decay*average + (1-decay)*rep;
				}
				
				done = true;
			}
			
			public boolean getDone() {
				return done;
			}
			
		}
		
		RepListener rl = new RepListener();
		
		ref.addListenerForSingleValueEvent(rl);
		
		while(!rl.getDone()) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return average;
	}

}
