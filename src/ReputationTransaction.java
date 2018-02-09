import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.Transaction.Result;

public class ReputationTransaction implements Transaction.Handler  {
	
	double toAdd;
	boolean done;
	
	public ReputationTransaction(double toAdd) {
		this.toAdd = toAdd;
	}
	
	@Override
	public Result doTransaction(MutableData snap) {
		
		double current = snap.getValue(Double.class);
		double updated = current + this.toAdd;
		snap.setValue(updated);
		
		return Transaction.success(snap);
	}

	@Override
	public void onComplete(DatabaseError arg0, boolean arg1, DataSnapshot arg2) {
		this.done = true;
	}
	
	public boolean isDone() {
		return done;
	}

}
