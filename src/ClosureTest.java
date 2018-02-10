import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ClosureTest {
	
	//DatabaseReference ref2;
	
	/*public ClosureTest(DatabaseReference ref2) {
		this.ref2 = ref2;
	}*/
	
	public void doStuff() {
		DatabaseReference ref2 = FirebaseDatabase
				.getInstance()
				.getReference("users/" + "MysEHXjohdVecyS8bPrDC9BBLxv2" + "/repScore");
		System.out.println("in do stuff");
		ref2.addListenerForSingleValueEvent(new ValueEventListener() {

			@Override
			public void onDataChange(DataSnapshot snapshot) {
				System.out.println("Closure is irrelevant");
			}

			@Override
			public void onCancelled(DatabaseError error) {
				// TODO Auto-generated method stub
				
			}
		});
	}
}
