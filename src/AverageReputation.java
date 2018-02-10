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
		
		if(mutation.getA().equals("admin")) {
			System.out.println("Author is admin");
			return 0.5;
		}
		
		System.out.println("Getting rep reference: " + mutation.getA());
		
		DatabaseReference ref2 = FirebaseDatabase
				.getInstance()
				.getReference("users/" + mutation.getA() + "/repScore");
		
		System.out.println("reference is: " + ref2.toString());
		
		
		class RepListener implements ValueEventListener {
			
			boolean done = false;
			
			@Override
			public void onCancelled(DatabaseError arg0) {
				System.out.println("Could not recover reputation for user: " + mutation.getA());
			}

			@Override
			public void onDataChange(DataSnapshot snap) {
				System.out.println("DATA CHANGE");
				double rep = snap.getValue(Double.class);
				if(average == 0.0) {
					average = rep;
				}
				
				else {
					average = decay*average + (1-decay)*rep;
				}
				
				System.out.println("Triggered");
				done = true;
			}
			
			public boolean getDone() {
				return done;
			}
		}
		
		
		RepListener rl = new RepListener();
		
		System.out.println("Adding rep listener");
		ref2.addValueEventListener(rl);
		System.out.println("rep listener added");
		
		while(!rl.getDone()) {
			//System.out.println("waiting...");
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return average;
	}

}
