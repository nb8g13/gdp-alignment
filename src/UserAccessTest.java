import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserAccessTest {
	
	public static void main(String[] args) {
		StartFirebase.connect();
		DatabaseReference ref = FirebaseDatabase
				.getInstance()
				.getReference("/users/MysEHXjohdVecyS8bPrDC9BBLxv2/repScore");
		
		ref.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot snapshot) {
				DatabaseReference ref = FirebaseDatabase
						.getInstance()
						.getReference("/users/MysEHXjohdVecyS8bPrDC9BBLxv2/repScore");
				
				ref.addListenerForSingleValueEvent(new ValueEventListener() {

					@Override
					public void onDataChange(DataSnapshot snapshot) {
						System.out.println("We must go d33per");
						
					}

					@Override
					public void onCancelled(DatabaseError error) {
						// TODO Auto-generated method stub
						
					}
					
				});
			}

			@Override
			public void onCancelled(DatabaseError error) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		while(true);
	}
	
	
	
}
