import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class RepListener implements ValueEventListener {
	
	boolean done = false;
	double average = 0.0;
	double decay = 0.9;
	Mutation mutation;
	
	public RepListener(double decay, Mutation mutation) {
		this.decay = decay;
		this.mutation = mutation;
	}

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
